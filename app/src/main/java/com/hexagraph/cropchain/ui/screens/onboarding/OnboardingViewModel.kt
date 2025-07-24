package com.hexagraph.cropchain.ui.screens.onboarding

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.toMutableStateList
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.MainActivity
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.domain.model.SupportedLanguages
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : BaseViewModel<OnboardingUIState>() {
    private val onboardingUiStateFlow = MutableStateFlow(OnboardingUIState())
    val areAllPermissionsAllowedFlow = appPreferences.areAllPermissionsGranted.getFlow()
    val visiblePermissionDialogQueue = PermissionsRequired.entries.toMutableStateList()


//    init {
//        viewModelScope.launch {
//            appPreferences.appLanguage.getFlow().collectLatest {
//                onboardingUiStateFlow.emit(
//                    uiState.value.copy(
//                        currentAppLanguage = it
//                    )
//                )
//            }
//            appPreferences.aadharID.getFlow().collectLatest {
//                onboardingUiStateFlow.emit(
//                    onboardingUiStateFlow.value.copy(
//                        storedAadharId = it
//                    )
//                )
//            }
//        }
//    }

    fun initializeScreen(allowedScreens: List<OnboardingScreens>, onCompletion: () -> Unit) {
        if (allowedScreens.isEmpty()) {
            onCompletion()
            return
        }
        viewModelScope.launch {
            onboardingUiStateFlow.emit(
                uiState.value.copy(
                    allowedScreens = allowedScreens,
                    onCompletion = onCompletion
                )
            )
        }
    }

    fun checkMissingPermissions(context: Context) {
        viewModelScope.launch {
            PermissionsRequired.entries.forEach { permission ->
                if (ContextCompat.checkSelfPermission(
                        context,
                        permission.permission
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    visiblePermissionDialogQueue.remove(permission)
                } else {
                    if (!visiblePermissionDialogQueue.contains(permission)) {
                        visiblePermissionDialogQueue.add(permission)
                    }
                }
            }
            if (visiblePermissionDialogQueue.isEmpty() && uiState.value.currentScreenName == OnboardingScreens.PERMISSIONS_SCREEN) {
                appPreferences.areAllPermissionsGranted.set(true)
//                moveForwardWithinOnboarding()
            }
            if (visiblePermissionDialogQueue.isEmpty()) {
                onboardingUiStateFlow.emit(
                    uiState.value.copy(
                        allowedScreens = uiState.value.allowedScreens.toMutableList() - OnboardingScreens.PERMISSIONS_SCREEN
                    )
                )
            } else {
                if (!uiState.value.allowedScreens.contains(OnboardingScreens.PERMISSIONS_SCREEN))
                    onboardingUiStateFlow.emit(
                        uiState.value.copy(
                            allowedScreens = uiState.value.allowedScreens.toMutableList() + OnboardingScreens.PERMISSIONS_SCREEN
                        )
                    )
            }
        }
    }

    suspend fun checkAndSaveAadhaarAndPassword(
        aadhaar: String,
        password: String,
        name: String
    ): Boolean {
        if (aadhaar.isEmpty()) {
            emitError(R.string.aadhaar_id_cannot_be_empty)
            return false
        }
        if (password.isEmpty()) {
            emitError(R.string.password_cannot_be_empty)
            return false
        }
        if (name.isEmpty()) {
            emitError(R.string.name_cannot_be_empty)
            return false
        }
        if (aadhaar.length != 12) {
            emitError(R.string.aadhaar_id_must_be_12_digits)
            return false
        }
        if (!aadhaar.all { it.isDigit() }) {
            emitError(R.string.aadhaar_id_must_be_numeric)
            return false
        }
        appPreferences.aadharID.set(aadhaar)
        appPreferences.username.set(name)
        return true
    }

    fun nextButtonAction(context: Context) {

        viewModelScope.launch {
//            appPreferences.username.set(name ?: "")
            when (uiState.value.currentScreenName) {
                OnboardingScreens.LANGUAGE_SELECTION -> {
                    appPreferences.appLanguage.set(uiState.value.selectedLanguageQuery)
                    (context as MainActivity).recreate()
                    moveForwardWithinOnboarding()
                }

                OnboardingScreens.INTRODUCTION_SCREEN_1 -> {
                    moveForwardWithinOnboarding()
                }

                OnboardingScreens.INTRODUCTION_SCREEN_2 -> {
                    moveForwardWithinOnboarding()
                }

                OnboardingScreens.INTRODUCTION_SCREEN_3 -> {
                    moveForwardWithinOnboarding()
                }

                OnboardingScreens.ROLE_SELECTION -> {
                    appPreferences.isCurrentUserFarmer.set(uiState.value.isUserFarmerQuery)
                    moveForwardWithinOnboarding()
                }

                OnboardingScreens.PERMISSIONS_SCREEN -> {
                    //No button here
                }

                OnboardingScreens.AADHAR_INPUT -> {
                    val aadhaar = uiState.value.aadhaarQuery
                    val password = uiState.value.password
                    if (checkAndSaveAadhaarAndPassword(
                            aadhaar,
                            password,
                            uiState.value.nameQuery
                        )
                    ) {
                        moveForwardWithinOnboarding()
                    }
                }

                OnboardingScreens.VERIFICATION_REQUEST_SENT -> {
                    moveForwardWithinOnboarding()
                }
            }

        }
    }

    fun moveForwardWithinOnboarding() {
        if (uiState.value.currentScreenIndex >= uiState.value.allowedScreens.size - 1) {
            uiState.value.onCompletion()
            return
        }
        viewModelScope.launch {
            onboardingUiStateFlow.emit(
                uiState.value.copy(
                    currentScreenIndex = uiState.value.currentScreenIndex + 1
                )
            )
        }
    }

    fun moveBackwardWithinOnboarding() {
        if (uiState.value.currentScreenIndex <= 0) return
        viewModelScope.launch {
            onboardingUiStateFlow.emit(
                uiState.value.copy(
                    currentScreenIndex = uiState.value.currentScreenIndex - 1
                )
            )
        }
    }

    fun onLanguageSelected(selectedLanguages: SupportedLanguages) {
        viewModelScope.launch {
            onboardingUiStateFlow.emit(
                uiState.value.copy(
                    selectedLanguageQuery = selectedLanguages
                )
            )
        }
    }

    fun onIsUserFarmerSelected(isFarmer: Boolean) {
        viewModelScope.launch {
            onboardingUiStateFlow.emit(
                uiState.value.copy(
                    isUserFarmerQuery = isFarmer
                )
            )
        }
    }

    fun onLoginQueryChange(
        aadhaar: String? = null,
        password: String? = null,
        name: String? = null
    ) {
        viewModelScope.launch {
            onboardingUiStateFlow.emit(
                uiState.value.copy(
                    aadhaarQuery = aadhaar ?: uiState.value.aadhaarQuery,
                    password = password ?: uiState.value.password,
                    nameQuery = name ?: uiState.value.nameQuery
                )
            )
//            appPreferences.username.set(name ?: "")

        }
    }

    override val uiState: StateFlow<OnboardingUIState> = createUiStateFlow()

    override fun createUiStateFlow(): StateFlow<OnboardingUIState> {
        return onboardingUiStateFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = OnboardingUIState()
        )
    }
}