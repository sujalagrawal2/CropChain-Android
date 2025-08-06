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
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val url: String = "",
    val showConfirmationDialog: Boolean = false,
    val pendingAction: PendingAction? = null
)

enum class PendingAction {
    APPROVE, DISAPPROVE
}

@HiltViewModel
class ReviewImageScreenViewModel @Inject constructor(
    private val web3JRepository: Web3jRepository,
    private val metaMaskSDKRepository: MetaMaskSDKRepository,
    private val appPreferences: AppPreferences
) : ViewModel() {


    fun getInfo(id: Int, type: Int) {
        viewModelScope.launch {
            web3JRepository.getUrlById(id).onSuccess { url ->
                web3JRepository.getImageInfo(url).onSuccess { imageInfo ->
                    val imageList = imageInfo.imageUrl
                        .split("$")
                        .map { it.trim() }
                        .filter { it.isNotBlank() }

                    _uiState.value = _uiState.value.copy(
                        images = imageList,
                        reviewText = imageInfo.reviewerSol,
                        likeCount = imageInfo.trueCount,
                        dislikeCount = imageInfo.falseCount,
                        aiSolution = imageInfo.aiSol,
                        type = type,
                        url = imageInfo.imageUrl,
                        title = imageInfo.title,
                        description = imageInfo.description,
                        location = imageInfo.location
                    )
                }.onFailure {
                    _uiState.value = _uiState.value.copy(
                        error = "Failed to fetch image info: ${it.message}"
                    )
                }
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to fetch URL by ID: ${it.message}"
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
        _uiState.value = _uiState.value.copy(
            showConfirmationDialog = true,
            pendingAction = PendingAction.APPROVE
        )
    }

    fun onDisapproveClicked() {
        _uiState.value = _uiState.value.copy(
            showConfirmationDialog = true,
            pendingAction = PendingAction.DISAPPROVE
        )
    }

    fun onConfirmationResult(confirmed: Boolean) {
        val pending = _uiState.value.pendingAction
        if (confirmed && pending != null) {
            _uiState.value = _uiState.value.copy(
                liked = (pending == PendingAction.APPROVE),
                showConfirmationDialog = false,
                pendingAction = null
            )
            submit()
        } else {
            _uiState.value = _uiState.value.copy(
                showConfirmationDialog = false,
                pendingAction = null
            )
        }
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
