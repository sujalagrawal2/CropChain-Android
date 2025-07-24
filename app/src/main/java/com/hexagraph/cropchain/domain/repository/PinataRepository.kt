package com.hexagraph.cropchain.domain.repository

import java.io.File

interface PinataRepository {

    suspend fun uploadImageToPinata(
        file: File,
        onProgress: (Int) -> Unit
    ): Result<String>

}