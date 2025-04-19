package com.hexagraph.cropchain.farmer.ui.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.repository.CropRepository
import com.hexagraph.cropchain.domain.repository.apppreferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenUIState(
    val uploadedImages: List<Crop> = emptyList(),
    val userName: String = ""
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val cropRepository: CropRepository,
    private val appPreferences: AppPreferences,
) : ViewModel() {


    private val _uiState = mutableStateOf(HomeScreenUIState())
    val uiState: State<HomeScreenUIState> = _uiState

    init {
        getUploadedImages()
        viewModelScope.launch {
            appPreferences.username.getFlow().collectLatest {
                _uiState.value = _uiState.value.copy(userName = it)
            }
        }
    }

    private fun getUploadedImages() {
        viewModelScope.launch {
            cropRepository.getAllUploadedCrops().collect { it ->
                _uiState.value = _uiState.value.copy(uploadedImages = it)
            }
        }
    }


}