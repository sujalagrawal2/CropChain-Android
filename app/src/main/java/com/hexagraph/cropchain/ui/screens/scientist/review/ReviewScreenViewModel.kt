package com.hexagraph.cropchain.ui.screens.scientist.review

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import com.hexagraph.cropchain.domain.repository.Web3jRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReviewScreenUIState(
    val verifyImages: List<String> = emptyList(),
    val reviewImages: List<String> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ReviewScreenViewModel @Inject constructor(
    private val web3JRepository: Web3jRepository,
    private val appPreferences: AppPreferences,
) : ViewModel() {

    private val _uiState = mutableStateOf(ReviewScreenUIState())
    val uiState: State<ReviewScreenUIState> = _uiState

    init {
        viewModelScope.launch {
            appPreferences.accountSelected.getFlow().collect { account ->
                if (account == "") _uiState.value =
                    _uiState.value.copy(error = "No account selected")
                else {
                    web3JRepository.getOpenImages().onSuccess { images ->
                        _uiState.value = _uiState.value.copy(reviewImages = images)
                    }
                        .onFailure {
                            _uiState.value = _uiState.value.copy(error = it.message)
                        }

                    web3JRepository.getCloseImages().onSuccess { images ->
                        _uiState.value = _uiState.value.copy(verifyImages = images)
                    }
                        .onFailure {
                            _uiState.value = _uiState.value.copy(error = it.message)
                        }
                }
            }
        }
    }
}