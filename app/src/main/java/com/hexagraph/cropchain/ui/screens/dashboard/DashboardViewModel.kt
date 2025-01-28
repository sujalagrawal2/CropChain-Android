package com.hexagraph.cropchain.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import com.hexagraph.cropchain.Web3J
import com.hexagraph.cropchain.domain.repository.CropRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow


@HiltViewModel
class DashboardViewModel @Inject constructor(
    cropRepository: CropRepository,
    private val web3j: Web3J
) : ViewModel() {
    var imagesss = MutableStateFlow(listOf(""))
    fun getUploadedImages(){
        imagesss.value = web3j.provideImages()
    }


}