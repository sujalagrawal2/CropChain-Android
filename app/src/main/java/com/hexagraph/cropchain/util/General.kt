package com.hexagraph.cropchain.util

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun uriToFile(context: Context, uri: String): File {
    val inputStream = context.contentResolver.openInputStream(uri.toUri())
    val file = File(context.cacheDir, "temp_image")
    file.outputStream().use { output ->
        inputStream?.copyTo(output)
    }
    return file
}

fun getCurrentTimestamp(): String {
    val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
    return sdf.format(Date())
}

fun saveImageToInternalStorage(context: Context, uri: Uri): Uri? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.filesDir, "image_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        file.toUri()
    } catch (e: Exception) {
        Log.e("SaveImage", "Error saving image", e)
        null
    }
}

fun deleteImageFile(context: Context, uri: Uri): Boolean {
    return try {
        val file = File(uri.path ?: return false)
        if (file.exists()) {
            file.delete()
        } else {
            context.contentResolver.delete(uri, null, null) > 0
        }
    } catch (e: Exception) {
        Log.e("DeleteImage", "Failed to delete image: $e")
        false
    }
}