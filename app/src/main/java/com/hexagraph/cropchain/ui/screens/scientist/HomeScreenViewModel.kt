package com.hexagraph.cropchain.ui.screens.scientist

import android.provider.MediaStore.Images
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.Web3J
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenUIState(
    val images: List<List<String>> = emptyList(),
    val addresses: List<String> = emptyList()
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val web3j: Web3J) : ViewModel() {

    private val _uiState = mutableStateOf(HomeScreenUIState())
    val uiState: State<HomeScreenUIState> = _uiState

    init {
        getAllImages()
    }

    private fun getAllImages() {
        viewModelScope.launch {
            val farmers = web3j.getFarmers()
            println(farmers)
            _uiState.value = _uiState.value.copy(addresses = farmers)
            farmers.forEach {
                val images = web3j.getOpenImages(it)
                val newList: MutableList<List<String>> = _uiState.value.images.toMutableList()
                newList.add(images)
                _uiState.value = _uiState.value.copy(newList)
            }
        }
    }
}