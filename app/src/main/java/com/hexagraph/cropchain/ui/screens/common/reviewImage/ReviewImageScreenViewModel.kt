package com.hexagraph.cropchain.ui.screens.common.reviewImage

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import com.hexagraph.cropchain.domain.repository.MetaMaskSDKRepository
import com.hexagraph.cropchain.domain.repository.Web3jRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ReviewImageScreenUIState(
    val images: List<String> = emptyList(),
    val reviewText: String = "",
    val liked: Boolean? = null,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val likeCount: Int = 0,
    val dislikeCount: Int = 0,
    val aiSolution: String = "",
    val type: Int = -1,
    val url: String = ""
)

@HiltViewModel
class ReviewImageScreenViewModel @Inject constructor(
    private val web3JRepository: Web3jRepository,
    private val metaMaskSDKRepository: MetaMaskSDKRepository,
    private val appPreferences: AppPreferences
) : ViewModel() {


    fun getInfo(url: String, type: Int) {
        viewModelScope.launch {

            web3JRepository.getImageInfo(url).onSuccess { imageInfo ->
                val images = imageInfo.imageUrl.split("$").filter { it.isNotBlank() }
                _uiState.value = _uiState.value.copy(
                    images = images,
                    reviewText = imageInfo.reviewerSol,
                    likeCount = imageInfo.trueCount,
                    dislikeCount = imageInfo.falseCount,
                    aiSolution = imageInfo.aiSol,
                    type = type,
                    url = imageInfo.imageUrl
                )
            }

        }
    }

    private val _uiState = mutableStateOf(ReviewImageScreenUIState())
    val uiState: State<ReviewImageScreenUIState> = _uiState

    fun onReviewTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(reviewText = text)
    }

    fun onApproveClicked() {
        _uiState.value = _uiState.value.copy(liked = true)
    }

    fun onDisapproveClicked() {
        _uiState.value = _uiState.value.copy(liked = false)
    }

    fun submit() {
        val currentState = _uiState.value
        if (currentState.type == 1) submitReview(currentState.url, currentState.reviewText)
        else submitChoice(currentState.url, currentState.liked ?: false)
    }

    fun submitReview(url: String, review: String) {
        metaMaskSDKRepository.connect(onError = {
        }) {
            viewModelScope.launch {
                try {
                    appPreferences.metaMaskMessage.set("Transaction is not Completed. Please be patient")
                    metaMaskSDKRepository.reviewImage(url, review).onSuccess {
                        appPreferences.metaMaskMessage.set("Transaction Completed. ")
                    }
                        .onFailure {
                            appPreferences.metaMaskMessage.set(it.message.toString())
                        }
                } catch (e: Exception) {
                    appPreferences.metaMaskMessage.set(e.message.toString())
                    Log.d("Submit Choice ViewModel ", e.message.toString())
                }
            }
        }
    }

    fun submitChoice(url: String, choice: Boolean) {
        metaMaskSDKRepository.connect(onError = {
        }) {
            viewModelScope.launch {
                try {
                    appPreferences.metaMaskMessage.set("Transaction is not Completed. Please be patient")
                    metaMaskSDKRepository.verifyImage(url, choice).onSuccess {
                        appPreferences.metaMaskMessage.set("Transaction Completed. ")
                    }
                        .onFailure {
                            appPreferences.metaMaskMessage.set(it.message.toString())
                        }
                } catch (e: Exception) {
                    appPreferences.metaMaskMessage.set(e.message.toString())
                    Log.d("Submit Choice ViewModel ", e.message.toString())
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
