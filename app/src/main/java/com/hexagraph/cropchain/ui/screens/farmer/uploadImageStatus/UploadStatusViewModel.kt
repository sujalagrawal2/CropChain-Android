package com.hexagraph.cropchain.ui.screens.farmer.uploadImageStatus

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.repository.CropRepository
import com.hexagraph.cropchain.domain.repository.MetaMaskRepository
import com.hexagraph.cropchain.domain.repository.MetaMaskSDKRepository
import com.hexagraph.cropchain.workManager.WorkManagerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


data class StatusScreenUIState(
    val cropList: List<Crop> = emptyList(),
    val progress: Int = 0
)

enum class ScreenStatus {
    LOADING, COMPLETED, ERROR
}

@HiltViewModel
class UploadStatusViewModel @Inject constructor(
    private val cropRepository: CropRepository,
    private val metaMaskSDKRepository: MetaMaskSDKRepository,
    private val workManager: WorkManagerRepository,
    private val metaMaskRepository: MetaMaskRepository,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _uiState = mutableStateOf(StatusScreenUIState())
    val uiState: State<StatusScreenUIState> = _uiState

    val status = mutableStateOf(ScreenStatus.LOADING)

    fun uploadAllImages() {
        viewModelScope.launch {
            val crops = cropRepository.getPinataUploadCrops()
            crops.forEach { crop ->
                if (crop.uploadedToPinata == 0) {
                    crop.uploadedToPinata = -1
                    crop.uploadProgress = 0
                    cropRepository.updateCrop(crop)
                }
            }
            workManager.count()
        }
    }

    fun getAllCrops() {
        viewModelScope.launch {
            cropRepository.getAllCrop().collect { cropList ->
                _uiState.value = _uiState.value.copy(cropList = cropList)
                status.value = ScreenStatus.COMPLETED
            }
        }
    }

    fun setProgress(progress: Int) {
        _uiState.value = _uiState.value.copy(progress = progress)
    }

    fun isConnected(): Boolean {
        return metaMaskSDKRepository.walletAddress != ""
    }

    fun uploadToBlockChain() {
        metaMaskSDKRepository.connect(onError = {
        }) { connectedAccounts ->

            viewModelScope.launch {
                appPreferences.metaMaskMessage.set("Transaction is not Completed. Please be patient")
                val accounts = metaMaskRepository.getAllAccounts().first()
                accounts.forEach { account ->
                    var check = 0
                    connectedAccounts.forEach { connectedAccount ->
                        if (account.account == connectedAccount) check = 1
                    }
                    if (check == 1) {
                        if (!account.isConnected) {
                            metaMaskRepository.updateAccount(account.copy(isConnected = true))
                        }
                    } else {
                        if (account.isConnected) {
                            metaMaskRepository.updateAccount(account.copy(isConnected = false))
                        }
                    }
                }

                try {
                    val cropList = cropRepository.getBlockChainUploadCrops()

                    if (cropList.isEmpty()) {
                        return@launch
                    }

                    val crops = cropList.joinToString(separator = "$") { it.url ?: "" }

                    val result = metaMaskSDKRepository.uploadImage(crops)

                    result.onSuccess { txHash ->
                        appPreferences.metaMaskMessage.set("Transaction Completed. ")

                        cropList.forEach {
                            it.uploadedToBlockChain = true
                            it.transactionHash = txHash
                            cropRepository.updateCrop(it)
                        }
                        Log.d("MetaMask", txHash)
                        getAllCrops()
                    }

                    result.onFailure {
                        appPreferences.metaMaskMessage.set(it.message.toString())
                        Log.d("Upload Status ViewModel ", it.message.toString())
                    }
                } catch (e: Exception) {
                    appPreferences.metaMaskMessage.set(e.message.toString())
                    Log.d("Upload Status ViewModel ", e.message.toString())
                }
            }
        }

    }

}


