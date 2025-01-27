package com.hexagraph.cropchain.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.hexagraph.cropchain.Web3J
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileScreenViewModel @Inject constructor(private val web3j: Web3J) : ViewModel() {
    val connectionState = web3j.connectionState
    fun connectToBlockChain() {
        web3j.connectWithLocalHost()
    }

}