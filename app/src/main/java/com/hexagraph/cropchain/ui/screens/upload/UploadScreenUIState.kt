package com.hexagraph.cropchain.ui.screens.upload

import com.hexagraph.cropchain.util.UploadImageStatus

data class UploadScreenUIState(
    val uploadImageStatus: UploadImageStatus = UploadImageStatus.IDLE,
    val url: String? = null
)
