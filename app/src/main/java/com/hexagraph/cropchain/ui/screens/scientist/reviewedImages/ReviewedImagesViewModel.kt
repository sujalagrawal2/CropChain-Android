package com.hexagraph.cropchain.ui.screens.scientist.reviewedImages

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import com.hexagraph.cropchain.domain.repository.Web3jRepository
import com.hexagraph.cropchain.ui.screens.farmer.uploadedImages.CropItem
import com.hexagraph.cropchain.ui.screens.scientist.review.ReviewScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReviewedScreenUIState(
    val verifyImages: List<CropItem> = emptyList(),
    val reviewImages: List<CropItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ReviewedImagesViewModel @Inject constructor(
    private val web3JRepository: Web3jRepository,
    private val appPreferences: AppPreferences,
) : ViewModel() {

    private val _uiState = mutableStateOf(ReviewedScreenUIState())
    val uiState: State<ReviewedScreenUIState> = _uiState

    init {
        loadReviewedScreenData()
    }

    private fun loadReviewedScreenData() {
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

                web3JRepository.getScientist(account).onSuccess { scientist ->
                    val reviewedImages = scientist.reviewedImages
                    val verifiedImages = scientist.verifiedImages

                    val reviewItems = reviewedImages.map { url ->
                        web3JRepository.getImageInfo(url).getOrNull()?.let { info ->
                            CropItem(
                                url = url,
                                id = info.id,
                                title = info.title,
                                description = info.description
                            )
                        } ?: CropItem(url = url)
                    }

                    val verifyItems = verifiedImages.map { url ->
                        web3JRepository.getImageInfo(url).getOrNull()?.let { info ->
                            CropItem(
                                url = url,
                                id = info.id,
                                title = info.title,
                                description = info.description
                            )
                        } ?: CropItem(url = url)
                    }

                    _uiState.value = _uiState.value.copy(
                        reviewImages = reviewItems,
                        verifyImages = verifyItems,
                        isLoading = false
                    )

                }.onFailure {
                    _uiState.value = _uiState.value.copy(
                        error = it.message,
                        isLoading = false
                    )
                }
            }
        }
    }
}
