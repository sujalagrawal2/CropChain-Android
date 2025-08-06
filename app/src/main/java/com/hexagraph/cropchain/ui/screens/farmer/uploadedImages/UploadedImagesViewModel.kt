package com.hexagraph.cropchain.ui.screens.farmer.uploadedImages

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.repository.Web3jRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CropItem(
    var id: Int = 0,
    val url: String = "",
    var title: String = "",
    var description: String = "",
)

data class UploadedImageUIState(
    val verifiedImages: List<CropItem> = emptyList(),
    val pendingImages: List<CropItem> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class UploadedImagesViewModel @Inject constructor(
    private val web3jRepository: Web3jRepository,
) : ViewModel() {

    private val _uiState = mutableStateOf(UploadedImageUIState())
    val uiState: State<UploadedImageUIState> = _uiState

    init {
        getVerifiedImages()
    }

    private fun getVerifiedImages() {
        viewModelScope.launch {

            web3jRepository.getFarmer(null)
                .onSuccess { farmerData ->
                    // Step 1: Map URLs to CropItem lists
                    val initialVerifiedList = farmerData.verifiedImages.map { CropItem(url = it) }
                    val initialPendingList = farmerData.unVerifiedImages.map { CropItem(url = it) }

                    // Step 2: Fetch detailed info for verified images
                    val updatedVerifiedList = initialVerifiedList.map { item ->
                        web3jRepository.getImageInfo(item.url).getOrNull()?.let { crop ->
                            item.copy(
                                id = crop.id,
                                title = crop.title,
                                description = crop.description
                            )
                        } ?: item
                    }

                    // Step 3: Fetch detailed info for pending images
                    val updatedPendingList = initialPendingList.map { item ->
                        web3jRepository.getImageInfo(item.url).getOrNull()?.let { crop ->
                            item.copy(
                                id = crop.id,
                                title = crop.title,
                                description = crop.description
                            )
                        } ?: item
                    }

                    // Step 4: Update state once
                    _uiState.value = _uiState.value.copy(
                        verifiedImages = updatedVerifiedList,
                        pendingImages = updatedPendingList,
                        error = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Unknown error",
                    )
                }
        }
    }



}