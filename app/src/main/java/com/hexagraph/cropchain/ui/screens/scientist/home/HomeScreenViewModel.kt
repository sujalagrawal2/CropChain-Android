package com.hexagraph.cropchain.ui.screens.scientist.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import com.hexagraph.cropchain.domain.repository.Web3jRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenUIState(
    val userName: String = "",
    val error: String? = null,
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val web3jRepository: Web3jRepository,
) : ViewModel() {


    private val _uiState = mutableStateOf(HomeScreenUIState())
    val uiState: State<HomeScreenUIState> = _uiState

    init {

        viewModelScope.launch {
            appPreferences.username.getFlow().collectLatest {
                _uiState.value = _uiState.value.copy(userName = it)
            }
        }
    }

    fun toastMessageShown() {
        _uiState.value = _uiState.value.copy(error = null)
    }




}