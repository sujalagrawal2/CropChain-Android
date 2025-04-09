package com.hexagraph.cropchain.ui.screens.upload

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.Web3J
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.repository.CropRepository
import com.hexagraph.cropchain.util.ScreenStatus
import com.hexagraph.cropchain.util.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StatusScreenUIState(
    val cropList: List<Crop> = emptyList(),
)

@HiltViewModel
class ImageStatusViewModel @Inject constructor(
    private val cropRepository: CropRepository,
    private val web3j: Web3J
) : ViewModel() {

    val status = mutableStateOf(ScreenStatus.LOADING)
    private val _uiState = mutableStateOf(StatusScreenUIState())
    val uiState: State<StatusScreenUIState> = _uiState

     fun getAllCrops() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                cropList = cropRepository.getAllCrop(),
            )
            status.value = ScreenStatus.COMPLETED
        }
    }


}