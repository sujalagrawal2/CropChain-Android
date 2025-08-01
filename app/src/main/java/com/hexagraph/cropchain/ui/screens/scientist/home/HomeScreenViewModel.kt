package com.hexagraph.cropchain.ui.screens.scientist.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import com.hexagraph.cropchain.domain.repository.Web3jRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenUIState(
    val reviewedImages: List<List<String>> = emptyList(),
    val verifiedImages: List<List<String>> = emptyList(),
    val userName: String = "",
    val error: String? = null,
    val reviewedImagesOriginal : List<String> =emptyList(),
    val verifiedImagesOriginal : List<String> =emptyList()
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val web3jRepository: Web3jRepository,
) : ViewModel() {


    private val _uiState = mutableStateOf(HomeScreenUIState())
    val uiState: State<HomeScreenUIState> = _uiState

    init {
        getScientist()
        viewModelScope.launch {
            appPreferences.username.getFlow().collectLatest {
                _uiState.value = _uiState.value.copy(userName = it)
            }
        }
    }

    fun toastMessageShown() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun getScientist() {
        viewModelScope.launch {
            appPreferences.accountSelected.getFlow().collect { account ->
                if (account == "") _uiState.value =
                    _uiState.value.copy(error = "No account selected")
                else {
                    web3jRepository.getScientist(account).onSuccess { scientist ->
                        val reviewedImages = scientist.reviewedImages
                        val verifiedImages = scientist.verifiedImages
                        _uiState.value = _uiState.value.copy(
                            reviewedImages = reviewedImages.map { list ->
                                list.split("$").filter { it.isNotBlank() }
                            },
                            verifiedImages = verifiedImages.map { list ->
                                list.split("$").filter { it.isNotBlank() }
                            },
                            reviewedImagesOriginal = reviewedImages,
                            verifiedImagesOriginal = verifiedImages
                        )

                    }.onFailure {
                        _uiState.value = _uiState.value.copy(error = it.message)
                    }

                }
            }
        }
    }


}