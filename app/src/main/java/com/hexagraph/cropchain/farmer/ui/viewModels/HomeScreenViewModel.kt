package com.hexagraph.cropchain.farmer.ui.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.repository.CropRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenUIState(
    val uploadedImages : List<Crop> = emptyList()
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val cropRepository: CropRepository): ViewModel() {


    private val _uiState = mutableStateOf(HomeScreenUIState())
    val uiState: State<HomeScreenUIState> = _uiState
    init {
        getUploadedImages()
    }
    private fun getUploadedImages(){
        viewModelScope.launch {
            cropRepository.getAllUploadedCrops().collect {it->
                _uiState.value = _uiState.value.copy(uploadedImages = it)
            }
        }
    }



}