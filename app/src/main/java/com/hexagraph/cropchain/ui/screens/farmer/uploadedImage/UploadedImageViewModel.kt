package com.hexagraph.cropchain.ui.screens.farmer.uploadedImage

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.Web3J
import com.hexagraph.cropchain.domain.repository.MetaMaskSDKRepository
import com.hexagraph.cropchain.domain.repository.Web3jRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UploadedImageUIState(
    val verifiedImages: List<String> = emptyList(),
    val pendingImages: List<String> = emptyList()
)

@HiltViewModel
class UploadedImageViewModel @Inject constructor(
    private val web3jRepository: Web3jRepository,
) : ViewModel() {

    private val _uiState = mutableStateOf(UploadedImageUIState())
    val uiState: State<UploadedImageUIState> = _uiState

    init {
        getVerifiedImages()
    }

    private fun getVerifiedImages() {
        viewModelScope.launch {
            val verifiedImages = web3jRepository.getVerifiedImage()

            _uiState.value = _uiState.value.copy(verifiedImages = verifiedImages)
        }
    }



}