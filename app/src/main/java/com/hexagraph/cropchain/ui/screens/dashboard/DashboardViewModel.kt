package com.hexagraph.cropchain.ui.screens.dashboard

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.hexagraph.cropchain.Web3J
import com.hexagraph.cropchain.domain.repository.CropRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.math.BigInteger


@HiltViewModel
class DashboardViewModel @Inject constructor(
    cropRepository: CropRepository,
    private val web3j: Web3J
) : ViewModel() {
    var imagesss = MutableStateFlow(listOf(""))
    fun getUploadedImages() {
        val context = LocalContext
        CoroutineScope(Dispatchers.IO).launch {
            imagesss.value = web3j.getFarmers()
        }
    }

    fun writeReview() {
        CoroutineScope(Dispatchers.IO).launch {
            val finalImages = web3j.getFinalImages()
            println(finalImages)
        }
    }


}