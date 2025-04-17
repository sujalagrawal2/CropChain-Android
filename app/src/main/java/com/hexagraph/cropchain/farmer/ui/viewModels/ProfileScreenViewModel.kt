package com.hexagraph.cropchain.farmer.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.MetaMask
import com.hexagraph.cropchain.domain.repository.apppreferences.AppPreferences
import com.hexagraph.cropchain.ui.screens.profile.ProfileUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.metamask.androidsdk.EthereumState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUIState(
    val currentUserName: String = "",
    val aadharId: String = ""
)

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val metaMask: MetaMask,
    private val appPreferences: AppPreferences
) : ViewModel() {
    private val _isConnected = MutableStateFlow(metaMask.isConnected())
    val isConnected: StateFlow<Boolean> = _isConnected
    val uiStateFlow = MutableStateFlow(ProfileUIState())

    private val _ethereumState = MutableStateFlow<EthereumState?>(null)
    val ethereumState: StateFlow<EthereumState?> = _ethereumState

    private val _balance = MutableStateFlow<String?>(null)
    val balance: StateFlow<String?> = _balance

    init {
        metaMask.details().observeForever { state ->
            _ethereumState.value = state
        }
        viewModelScope.launch {
            appPreferences.username.getFlow().collect(){
                uiStateFlow.value = uiStateFlow.value.copy(
                    currentUserName = it
                )
            }
            appPreferences.aadharID.getFlow().collect(){
                uiStateFlow.value = uiStateFlow.value.copy(
                    aadharId = it
                )
            }
        }
    }

    fun connect() {
        metaMask.connect(onError = {
            _isConnected.value = false

        }, onSuccess = {
            _isConnected.value = true

        })

    }

    fun fetchBalance() {
        viewModelScope.launch {
            metaMask.getAccountBalance(callback = {
                if (it != null) {
                    _balance.value = it
                } else {
                    _balance.value = "Error fetching balance"
                }
            })
        }
    }

    fun clearBalance() {
        _balance.value = null
    }

    fun isConnected() {
        _isConnected.value = metaMask.isConnected()
    }


}