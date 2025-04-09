package com.hexagraph.cropchain.ui.screens.verifier

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.Web3J
import com.hexagraph.cropchain.ui.screens.scientist.FarmerScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject

data class VerifierScreenUIState(
    val images: List<String> = emptyList(),
    val imageResult: List<ImageResult> = emptyList()
)

data class ImageResult(
    val aiSolution: String = "",
    val verificationCount: String = "0",
    val scientistSolution: String = ""
)

@HiltViewModel
class VerifierScreenViewModel @Inject constructor(private val web3j: Web3J) : ViewModel() {
    private val _uiState = mutableStateOf(VerifierScreenUIState())
    val uiState: State<VerifierScreenUIState> = _uiState

    init {
        getAllImages()
    }

    private fun getAllImages() {
        viewModelScope.launch {
            val images = web3j.getFinalImages()
            _uiState.value = _uiState.value.copy(images = images)
            val imageResultList: MutableList<ImageResult> = emptyList<ImageResult>().toMutableList()
            images.forEach { url ->
                val result = web3j.getCloseListDetails(url)
                var ans = ImageResult()
                result.onSuccess { data ->
                    val scientistSolution = data[4].value as String
                    val aiSol = data[2].value as String
                    val verificationCount = data[8].value as BigInteger
                    ans = ImageResult(
                        scientistSolution = scientistSolution,
                        aiSolution = aiSol,
                        verificationCount = verificationCount.toString()
                    )
                }.onFailure {
                    Log.e("Web3j", "Failed: ${it.message}")
                }
                println(ans)
                imageResultList.add(ans)
            }
        }
    }

    fun verifyImage(url: String, like: Boolean) {
        viewModelScope.launch {
            web3j.verifyImage(url, like)
        }
    }


}