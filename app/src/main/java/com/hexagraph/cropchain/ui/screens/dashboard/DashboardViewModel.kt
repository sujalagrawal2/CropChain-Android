package com.hexagraph.cropchain.ui.screens.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.MetaMask
import com.hexagraph.cropchain.Web3J
import com.hexagraph.cropchain.domain.repository.CropRepository
import com.hexagraph.cropchain.domain.repository.apppreferences.AppPreferences
import com.hexagraph.cropchain.ui.screens.verifier.VerifierScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.math.BigInteger

data class DashBoardScreenUIState(
    val verifiedImage: List<String> = emptyList(),
    val userName: String = ""
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    cropRepository: CropRepository,
    private val appPreferences: AppPreferences,
    private val web3j: Web3J,
    private val metaMask: MetaMask
) : ViewModel() {

    private val _uiState = mutableStateOf(DashBoardScreenUIState())
    val uiState: State<DashBoardScreenUIState> = _uiState

    init {
        viewModelScope.launch {
            appPreferences.username.getFlow().collect {
                _uiState.value = _uiState.value.copy(userName = it)
            }
        }
    }

    fun connect() {
        metaMask.connect(onSuccess = {}, onError = {})


    }

    fun send() {
        viewModelScope.launch {
            metaMask.send()
        }
    }

    fun isConnected(): Boolean {
        return metaMask.isConnected()
    }

    fun getUploadedImages() {
        viewModelScope.launch {
            val verifiedImage = web3j.getVerifiedImage()
            _uiState.value = _uiState.value.copy(verifiedImage = verifiedImage)
        }
    }

    fun writeReview() {
        CoroutineScope(Dispatchers.IO).launch {
            val finalImages = web3j.getFinalImages()
            println(finalImages)
        }
    }

}