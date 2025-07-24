package com.hexagraph.cropchain.farmer.ui.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.MainActivity
import com.hexagraph.cropchain.MetaMask
import com.hexagraph.cropchain.domain.model.MetaMaskAccounts
import com.hexagraph.cropchain.domain.model.SupportedLanguages
import com.hexagraph.cropchain.domain.repository.MetaMaskRepository
import com.hexagraph.cropchain.domain.repository.apppreferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import io.metamask.androidsdk.EthereumState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUIState(
    val currentUserName: String = "",
    val aadharId: String = "",
    val selectedLanguage: SupportedLanguages = SupportedLanguages.ENGLISH,
    val isLanguageSelectionBottomSheetVisible: Boolean = false,
    val connectedAccounts: List<String> = emptyList(),
    val disconnectedAccounts: List<String> = emptyList(),
    val accountSelected: String = " "
)

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val metaMask: MetaMask,
    private val appPreferences: AppPreferences,
    private val metaMaskRepository: MetaMaskRepository
) : ViewModel() {

    val uiStateFlow = MutableStateFlow(ProfileUIState())

    private val _balance = MutableStateFlow<String?>(null)
    val balance: StateFlow<String?> = _balance

    init {

        viewModelScope.launch {
            getAllAccounts()
        }

        viewModelScope.launch {

            appPreferences.accountSelected.getFlow().collectLatest {
                Log.d("ProfileScreenViewModel", it)
                uiStateFlow.value = uiStateFlow.value.copy(
                    accountSelected = it
                )
            }
        }

        viewModelScope.launch {
            appPreferences.username.getFlow().collect() {
                uiStateFlow.value = uiStateFlow.value.copy(
                    currentUserName = it
                )
            }

            appPreferences.aadharID.getFlow().collect() {
                uiStateFlow.value = uiStateFlow.value.copy(
                    aadharId = it
                )
            }

            appPreferences.appLanguage.getFlow().collectLatest {
                uiStateFlow.value = uiStateFlow.value.copy(
                    selectedLanguage = it
                )
            }
        }
    }

    fun onAccountSelected(account: String) {
        viewModelScope.launch {
            appPreferences.accountSelected.set(account)
            metaMask.walletAddress = account
        }
    }

    private suspend fun getAllAccounts() {
        metaMaskRepository.getAllAccounts().collect { state ->
            val connectedAccounts: MutableList<String> = emptyList<String>().toMutableList()
            val disConnectedAccounts: MutableList<String> = emptyList<String>().toMutableList()
            state.forEach { it ->
                if (it.isConnected) connectedAccounts.add(it.account)
                else
                    disConnectedAccounts.add(it.account)
            }
            uiStateFlow.value = uiStateFlow.value.copy(
                disconnectedAccounts = disConnectedAccounts,
                connectedAccounts = connectedAccounts
            )
        }
    }

    fun connectWallet(onSuccess: () -> Unit) {
        metaMask.connect(onError = {

        }) { accounts ->
            viewModelScope.launch {
                insertAccounts(accounts)
                onSuccess()
            }
        }
    }

    fun updateWallet(onSuccess: () -> Unit) {
        viewModelScope.launch {
            clearExistingAccounts()
            appPreferences.accountSelected.set("")
            metaMask.walletAddress = ""
            onSuccess()
        }
    }

    private suspend fun insertAccounts(accounts: List<String>) {
        accounts.forEach { account ->
            metaMaskRepository.insertAccount(
                MetaMaskAccounts(
                    account = account,
                    isConnected = true
                )
            )
        }
    }

    private suspend fun clearExistingAccounts() {
        metaMaskRepository.deleteAllAccounts()
    }

    fun fetchBalance(account: String) {
        viewModelScope.launch {
            metaMask.getAccountBalance(address = account, callback = {
                if (it != null) {
                    _balance.value = it
                } else {
                    _balance.value = "Error fetching balance"
                }
            })
        }
    }

    fun clearBalance() {
        _balance.value = null
    }

    fun toggleVisibilityOfLanguagePreferenceBottomSheet() {
        viewModelScope.launch {
            uiStateFlow.value = uiStateFlow.value.copy(
                isLanguageSelectionBottomSheetVisible = !uiStateFlow.value.isLanguageSelectionBottomSheetVisible
            )
        }
    }

    fun changeSelectedLanguage(selectedLanguage: SupportedLanguages) {
        viewModelScope.launch {
            appPreferences.appLanguage.set(selectedLanguage)
            uiStateFlow.value = uiStateFlow.value.copy(
                selectedLanguage = selectedLanguage
            )
        }
    }

    fun saveSelectedLanguage(selectedLanguage: SupportedLanguages, context: Context) {
        viewModelScope.launch {
            appPreferences.appLanguage.set(selectedLanguage)
            (context as MainActivity).recreate()
        }
    }

    fun logOut() {
        viewModelScope.launch {
            appPreferences.isUserLoggedIn.set(false)
        }
    }
}