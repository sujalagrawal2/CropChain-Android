package com.hexagraph.cropchain.farmer.ui.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.MetaMask
import com.hexagraph.cropchain.Web3J
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UploadedImageUIState(
    val verifiedImages: List<String> = emptyList(),
    val pendingImages: List<String> = emptyList()
)

@HiltViewModel
class UploadedImageViewModel @Inject constructor(
    private val web3J: Web3J,
    private val metaMask: MetaMask
) : ViewModel() {

    private val _uiState = mutableStateOf(UploadedImageUIState())
    val uiState: State<UploadedImageUIState> = _uiState

    init {
        getVerifiedImages()
    }

    private fun getVerifiedImages() {
        Log.d("UploadedImageViewModel", metaMask.walletAddress)
        viewModelScope.launch {
            val verifiedImages = web3J.getVerifiedImage(metaMask.walletAddress)

            _uiState.value = _uiState.value.copy(verifiedImages = verifiedImages)
        }
    }



}