package com.hexagraph.cropchain.ui.screens.scientist.review

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import com.hexagraph.cropchain.domain.repository.Web3jRepository
import com.hexagraph.cropchain.ui.screens.farmer.uploadedImages.CropItem
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject


data class ReviewScreenUIState(
    val verifyImages: List<CropItem> = emptyList(),
    val reviewImages: List<CropItem> = emptyList(),
    val isLoading: Boolean = false,
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
        loadReviewScreenData()
    }

    private fun loadReviewScreenData() {
        viewModelScope.launch {
            appPreferences.accountSelected.getFlow().collect { account ->
                if (account.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        error = "No account selected",
                        isLoading = false
                    )
                    return@collect
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

                val reviewResult = web3JRepository.getOpenImages()
                val verifyResult = web3JRepository.getCloseImages()

                val reviewUrls = reviewResult.getOrNull() ?: emptyList()
                val verifyUrls = verifyResult.getOrNull() ?: emptyList()

                val reviewImages = reviewUrls.map { url ->
                    web3JRepository.getImageInfo(url).getOrNull()?.let { info ->
                        CropItem(
                            url = url,
                            id = info.id,
                            title = info.title,
                            description = info.description
                        )
                    } ?: CropItem(url = url)
                }

                val verifyImages = verifyUrls.map { url ->
                    web3JRepository.getImageInfo(url).getOrNull()?.let { info ->
                        CropItem(
                            url = url,
                            id = info.id,
                            title = info.title,
                            description = info.description
                        )
                    } ?: CropItem(url = url)
                }

                val errorMessage = reviewResult.exceptionOrNull()?.message
                    ?: verifyResult.exceptionOrNull()?.message

                _uiState.value = _uiState.value.copy(
                    reviewImages = reviewImages,
                    verifyImages = verifyImages,
                    error = errorMessage,
                    isLoading = false
                )
            }
        }
    }
}
