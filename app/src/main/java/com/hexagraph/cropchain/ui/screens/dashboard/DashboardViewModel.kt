package com.hexagraph.cropchain.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import com.hexagraph.cropchain.domain.repository.CropRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    cropRepository: CropRepository
) : ViewModel() {

}