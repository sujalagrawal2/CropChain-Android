package com.hexagraph.cropchain.data.remote

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File

class ProgressRequestBody(
    private val file: File,
    private val contentType: String,
    private val onProgress: (percentage: Int) -> Unit
) : RequestBody() {

    override fun contentType(): MediaType? = contentType.toMediaTypeOrNull()

    override fun contentLength(): Long = file.length()

    override fun writeTo(sink: BufferedSink) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        file.inputStream().use { inputStream ->
            var uploaded = 0L
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                sink.write(buffer, 0, read)
                uploaded += read
                val progress = (100 * uploaded / contentLength()).toInt()
                onProgress(progress)
            }
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }
}


fun buildMultipartFile(file: File, onProgress: (Int) -> Unit): MultipartBody.Part {
    val requestBody = ProgressRequestBody(file, "image/*", onProgress)
    return MultipartBody.Part.createFormData("file", file.name, requestBody)
}
