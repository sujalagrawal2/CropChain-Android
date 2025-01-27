package com.hexagraph.cropchain.ui.screens.upload

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.RetrofitInstance
import com.hexagraph.cropchain.Web3J
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.repository.CropRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private var url: String? = null
    var uploadImageStatus = mutableStateOf(UploadImageStatus.IDLE)
        private set

    init{
        web3j.connectWithLocalHost()
        uploadImageToBlockChain()
    }
    private suspend fun insertCrop(crop: Crop) {
        cropRepository.insertCrop(crop)
    }

    fun updateState() {
        uploadImageStatus.value = UploadImageStatus.IDLE
    }

    fun uploadImageToPinata(file: File) {
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)
        uploadImageStatus.value = UploadImageStatus.LOADING
        viewModelScope.launch {
            val apiSecret =
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySW5mb3JtYXRpb24iOnsiaWQiOiI0YWE1MWYzOS03MmQ4LTQ5NTAtOGY2ZS1mMjcwNGY3YzA3YjAiLCJlbWFpbCI6InN1amFncmF3YWwxNzgzQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJwaW5fcG9saWN5Ijp7InJlZ2lvbnMiOlt7ImRlc2lyZWRSZXBsaWNhdGlvbkNvdW50IjoxLCJpZCI6IkZSQTEifSx7ImRlc2lyZWRSZXBsaWNhdGlvbkNvdW50IjoxLCJpZCI6Ik5ZQzEifV0sInZlcnNpb24iOjF9LCJtZmFfZW5hYmxlZCI6ZmFsc2UsInN0YXR1cyI6IkFDVElWRSJ9LCJhdXRoZW50aWNhdGlvblR5cGUiOiJzY29wZWRLZXkiLCJzY29wZWRLZXlLZXkiOiIzMzRmNzU3NWUxZmRkNjkxZGNlZSIsInNjb3BlZEtleVNlY3JldCI6IjdkNjI4NTU3NWJiMDhlMTEzYTgwOTFhZDkwNjBlNjA3NTE0ODA2YjY0YTI1YTFjYzYxMjVhZmU5YmQxNTI0ZjAiLCJleHAiOjE3Njk0MTAxMzZ9.1METZTl56XW3HGGs2QN243wa_k7UgmB_CXK8U-hP6no"
            try {
                val response = RetrofitInstance.api.uploadImage(
                    authorization = "Bearer $apiSecret",
                    file = filePart
                )
                if (response.isSuccessful) {
                    response.body()?.let { pinataResponse ->
                        Log.d("PR", pinataResponse.IpfsHash)
                        url = pinataResponse.IpfsHash
                        insertCrop(Crop(url = pinataResponse.IpfsHash, status = "Pending"))
                        uploadImageStatus.value = UploadImageStatus.COMPLETED
                    }
                } else {
                    Log.e("PinataUpload", "Failed: ${response.errorBody()?.string()}")
                    uploadImageStatus.value = UploadImageStatus.FAILED

                }
            } catch (e: Exception) {
                Log.e("PinataUpload", "Error: $e")
                uploadImageStatus.value = UploadImageStatus.ERROR

            }
        }
    }

    fun uploadImageToBlockChain() {
        web3j.uploadImages("ravennnnnn")
    }
}

enum class UploadImageStatus {
    IDLE,
    LOADING,
    COMPLETED,
    ERROR,
    FAILED
}