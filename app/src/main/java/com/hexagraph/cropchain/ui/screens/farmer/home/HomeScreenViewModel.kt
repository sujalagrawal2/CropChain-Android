package com.hexagraph.cropchain.ui.screens.farmer.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.domain.model.CropImages
import com.hexagraph.cropchain.domain.repository.CropRepository
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import com.hexagraph.cropchain.data.repository.MetaMaskSDKRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenUIState(
    val uploadedImages: List<CropImages> = emptyList(),
    val userName: String = ""
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val cropRepository: CropRepository,
    private val appPreferences: AppPreferences,
    private val metaMaskSDKRepositoryImpl: MetaMaskSDKRepositoryImpl
) : ViewModel() {


    fun getAccount(): String {
        return metaMaskSDKRepositoryImpl.walletAddress
    }


    private val _uiState = mutableStateOf(HomeScreenUIState())
    val uiState: State<HomeScreenUIState> = _uiState

    init {
        viewModelScope.launch {
            appPreferences.username.getFlow().collectLatest {
                _uiState.value = _uiState.value.copy(userName = it)
            }
        }
    }


}