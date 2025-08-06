package com.hexagraph.cropchain.domain.services

import android.content.Context
import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.hexagraph.cropchain.R

class GeminiService(
    context: Context
) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = context.getString(R.string.GEMINI_API_KEY),
    )

    suspend fun generateContent(
        bitmap: Bitmap? = null,
        prompt: String
    ): String? {
        return try {
            val response = generativeModel.generateContent(
                content {
                    if(bitmap != null)
                        image(bitmap)
                    text(prompt)
                }
            )
            response.text
        } catch (e: Exception) {
            throw e
        }
    }
}