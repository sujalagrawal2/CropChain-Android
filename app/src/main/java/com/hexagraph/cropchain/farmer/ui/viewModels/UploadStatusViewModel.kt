package com.hexagraph.cropchain.farmer.ui.viewModels

import android.util.Log
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.repository.CropRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import com.hexagraph.cropchain.MetaMask
import com.hexagraph.cropchain.domain.repository.MetaMaskRepository
import com.hexagraph.cropchain.domain.repository.apppreferences.AppPreferences
import com.hexagraph.cropchain.workManager.WorkManagerRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first


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
    private val metaMask: MetaMask,
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
        return metaMask.walletAddress != ""
    }

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage = _snackBarMessage.asSharedFlow()

    suspend fun showSnackBar(message: String) {
        _snackBarMessage.emit(message)
    }


    fun uploadToBlockChain() {

        metaMask.connect(onError = {

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
                    // 1. Get crops to upload
                    val cropList = cropRepository.getBlockChainUploadCrops()

                    if (cropList.isEmpty()) {
                        showSnackBar("No crops to upload to blockchain")
                        return@launch
                    }

                    // 2. Format the crop URLs (concatenate with $ after each one)
                    val crops = cropList.joinToString(separator = "$") { it.url ?: "" }

                    // 3. Call the `send()` function
                    val result = metaMask.send(crops)

                    // 4. Handle result
                    result.onSuccess { txHash ->
                        appPreferences.metaMaskMessage.set("Transaction Completed. ")

                        // Mark crops as uploaded in the DB
                        cropList.forEach {
                            it.uploadedToBlockChain = true
                            it.transactionHash = txHash
                            cropRepository.updateCrop(it)
                        }
                        Log.d("MetaMask", txHash)
                        // Show transaction hash or success message
                        showSnackBar("Crops uploaded to blockchain successfully 🚀")

                        // Refresh UI
                        getAllCrops()
                    }

                    result.onFailure {
                        appPreferences.metaMaskMessage.set(it.message.toString())

                        Log.d("Upload Status ViewModel ", it.message.toString())
                        showSnackBar("Blockchain upload failed: ${it.message}")
                    }
                } catch (e: Exception) {
                    appPreferences.metaMaskMessage.set(e.message.toString())

                    Log.d("Upload Status ViewModel ", e.message.toString())
                    showSnackBar("Unexpected error: ${e.message}")
                }
            }
        }

    }

}


