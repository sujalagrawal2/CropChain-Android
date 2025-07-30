package com.hexagraph.cropchain.util

import android.content.Context
import android.util.Log
import com.hexagraph.cropchain.R
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun sendRegistrationToServer(
    token: String,
    aadhaarNumber: String,
    deviceId: String,
    context: Context
): Result<String> = suspendCancellableCoroutine { continuation ->
    val client = OkHttpClient()

    val json = JSONObject().apply {
        put("token", token)
        put("aadhaar_number", aadhaarNumber)
        put("device_id", deviceId)
    }

    val body = json.toString()
        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    val request = Request.Builder()
        .url("${context.getString(R.string.SERVER_API)}fcm/register/")
        .post(body)
        .build()

    val call = client.newCall(request)

    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("FCM", "Token upload failed", e)
            if (continuation.isActive) {
                continuation.resume(Result.failure(e))
            }
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            if (response.isSuccessful && responseBody != null) {
                Log.d("FCM", "Token upload successful: $responseBody")
                if (continuation.isActive) {
                    continuation.resume(Result.success(responseBody))
                }
            } else {
                val errorMessage =
                    "Token upload failed with code: ${response.code}, body: $responseBody"
                Log.e("FCM", errorMessage)
                if (continuation.isActive) {
                    continuation.resume(Result.failure(IOException(errorMessage)))
                }
            }
            response.close()
        }
    })

    continuation.invokeOnCancellation {
        call.cancel()
    }
}
