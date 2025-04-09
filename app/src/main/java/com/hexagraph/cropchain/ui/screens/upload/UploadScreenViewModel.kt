package com.hexagraph.cropchain.ui.screens.upload

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.RetrofitInstance
import com.hexagraph.cropchain.Web3J
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.repository.CropRepository
import com.hexagraph.cropchain.util.UploadImageStatus
import com.hexagraph.cropchain.util.uploadImageToPinata
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class UploadScreenViewModel @Inject constructor(
    private val cropRepository: CropRepository,
    private val web3j: Web3J
) :
    ViewModel() {
    val uploadImageToBlockChainStatue = web3j.uploadImageState
//    private var url: String = ""
//    var uploadImageStatus = mutableStateOf(UploadImageStatus.IDLE)
//        private set

    var uploadUiState = MutableStateFlow(UploadScreenUIState())
        private set

//    init {
//        web3j.connectWithLocalHost()
//    }

    private suspend fun insertCrop(crop: Crop) {
        cropRepository.insertCrop(crop)
    }

    fun updateState() {
        uploadUiState.value = uploadUiState.value.copy(
            uploadImageStatus = UploadImageStatus.IDLE
        )
    }

    fun uploadImage(file: File) {
        uploadUiState.value = uploadUiState.value.copy(
            uploadImageStatus = UploadImageStatus.LOADING
        )
        viewModelScope.launch {
            val result = uploadImageToPinata(file)
            result.onSuccess {
                uploadUiState.value = uploadUiState.value.copy(
                    url = it
                )
                uploadUiState.value = uploadUiState.value.copy(
                    uploadImageStatus = UploadImageStatus.COMPLETED
                )
            }
                .onFailure {
                    uploadUiState.value = uploadUiState.value.copy(
                        uploadImageStatus = UploadImageStatus.FAILED
                    )
                }
        }
    }

    suspend fun uploadImageToBlockChain() {
        if (uploadUiState.value.url != null)
            web3j.uploadImage(uploadUiState.value.url!!)
        else
            Log.e("Error", "Url is null")
    }
}

