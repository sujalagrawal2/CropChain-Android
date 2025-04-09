package com.hexagraph.cropchain.ui.screens.scientist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hexagraph.cropchain.Web3J
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FarmerScreenUIState(
    val images: List<String> = emptyList()
)

@HiltViewModel
class FarmerScreenViewModel @Inject constructor(val web3j: Web3J) : ViewModel() {

    private val _uiState = mutableStateOf(FarmerScreenUIState())
    val uiState: State<FarmerScreenUIState> = _uiState
    fun getAllImages(address: String) {
        viewModelScope.launch {
            val images = web3j.getOpenImages(address)
            _uiState.value = _uiState.value.copy(images = images)
        }
    }

    fun writeReview(urls: List<String>) {
        viewModelScope.launch {
            urls.forEach {
                web3j.reviewImage(it)
            }
        }
    }

}