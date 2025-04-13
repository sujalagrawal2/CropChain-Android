package com.hexagraph.cropchain.farmer.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.MetaMask
import dagger.hilt.android.lifecycle.HiltViewModel
import io.metamask.androidsdk.EthereumState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(private val metaMask: MetaMask) : ViewModel() {
    private val _isConnected = MutableStateFlow(metaMask.isConnected())
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _ethereumState = MutableStateFlow<EthereumState?>(null)
    val ethereumState: StateFlow<EthereumState?> = _ethereumState

    private val _balance = MutableStateFlow<String?>(null)
    val balance: StateFlow<String?> = _balance

    init {
        metaMask.details().observeForever { state ->
            _ethereumState.value = state

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