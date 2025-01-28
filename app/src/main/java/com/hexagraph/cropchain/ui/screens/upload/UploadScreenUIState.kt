package com.hexagraph.cropchain.ui.screens.upload

data class UploadScreenUIState(
    val uploadImageStatus: UploadImageStatus = UploadImageStatus.IDLE,
    val url: String? = null
)
