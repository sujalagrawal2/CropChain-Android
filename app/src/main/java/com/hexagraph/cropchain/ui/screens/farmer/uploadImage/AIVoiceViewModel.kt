package com.hexagraph.cropchain.ui.screens.farmer.uploadImage

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.domain.services.GeminiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

enum class VoiceState {
    IDLE,
    LISTENING,
    PROCESSING
}

@HiltViewModel
class AIVoiceViewModel @Inject constructor() : ViewModel() {

    private val _voiceState = mutableStateOf(VoiceState.IDLE)
    val voiceState: State<VoiceState> = _voiceState

    private val _transcript = mutableStateOf("")
    val transcript: State<String> = _transcript

    private val _isListening = mutableStateOf(false)
    val isListening: State<Boolean> = _isListening

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    private var speechRecognizer: SpeechRecognizer? = null
    private var geminiService: GeminiService? = null
    private var hasStoppedListening = false

    companion object {
        private const val GEMINI_PROMPT = """
            Based on the provided transcript and image, generate a concise title and detailed description for this crop image.
            
            Transcript: %s
            
            Please provide the response in the following format:
            TITLE: [A concise title for the crop]
            DESCRIPTION: [A detailed description including crop type, growth stage, health condition, and any other relevant observations]
            
            Make sure to separate the title and description clearly using the above format.
        """
    }

    fun initializeSpeechRecognizer(context: Context) {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    _isListening.value = true
                    _errorMessage.value = ""
                    hasStoppedListening = false
                }

                override fun onBeginningOfSpeech() {
                    _voiceState.value = VoiceState.LISTENING
                    _isListening.value = true
                }

                override fun onRmsChanged(rmsdB: Float) {
                    // Can be used for voice level indication
                }

                override fun onBufferReceived(buffer: ByteArray?) {}

                override fun onEndOfSpeech() {
                    _isListening.value = false
                    // Don't change state here, wait for results or error
                }

                override fun onError(error: Int) {
                    _isListening.value = false
                    _voiceState.value = VoiceState.IDLE
                    _errorMessage.value = when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                        SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                        SpeechRecognizer.ERROR_NETWORK -> "Network error"
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                        SpeechRecognizer.ERROR_NO_MATCH -> "No speech input detected"
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
                        SpeechRecognizer.ERROR_SERVER -> "Server error"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                        else -> "Recognition error occurred"
                    }
                    hasStoppedListening = false
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        _transcript.value = matches[0]
                    }
                    _isListening.value = false

                    // Only go to IDLE if we haven't manually stopped listening
                    // If we manually stopped, stay in current state to preserve transcript
                    if (!hasStoppedListening) {
                        _voiceState.value = VoiceState.IDLE
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        _transcript.value = matches[0]
                    }
                }

                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }
    }

    fun startListening() {
        if (speechRecognizer != null) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            }

            _transcript.value = ""
            _voiceState.value = VoiceState.LISTENING
            _isListening.value = true
            hasStoppedListening = false
            speechRecognizer?.startListening(intent)
        }
    }

    fun stopListening() {
        hasStoppedListening = true
        speechRecognizer?.stopListening()
        _isListening.value = false

        // Keep the transcript and don't change state immediately
        // Let the speech recognizer finish processing
        if (_transcript.value.isNotEmpty()) {
            _voiceState.value = VoiceState.IDLE
        }
    }

    fun processWithGemini(
        context: Context,
        imageUri: Uri?,
        onSuccess: (title: String, description: String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (_transcript.value.isEmpty()) {
            onError("No transcript available")
            return
        }

        _voiceState.value = VoiceState.PROCESSING

        viewModelScope.launch {
            try {
                if (geminiService == null) {
                    geminiService = GeminiService(context)
                }

                val prompt = GEMINI_PROMPT.format(_transcript.value)

                // Convert URI to Bitmap if image is provided
                val bitmap: Bitmap? = imageUri?.let { uri ->
                    try {
                        val inputStream = context.contentResolver.openInputStream(uri)
                        android.graphics.BitmapFactory.decodeStream(inputStream)
                    } catch (e: Exception) {
                        null
                    }
                }

                val response = geminiService?.generateContent(bitmap, prompt)

                if (response != null) {
                    parseGeminiResponse(response, onSuccess, onError)
                } else {
                    onError("Failed to get response from Gemini")
                }
            } catch (e: Exception) {
                onError("Error processing with Gemini: ${e.message}")
            } finally {
                _voiceState.value = VoiceState.IDLE
            }
        }
    }

    private fun parseGeminiResponse(
        response: String,
        onSuccess: (title: String, description: String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val lines = response.split("\n")
            var title = ""
            var description = ""

            for (line in lines) {
                when {
                    line.startsWith("TITLE:", ignoreCase = true) -> {
                        title = line.substringAfter(":").trim()
                    }
                    line.startsWith("DESCRIPTION:", ignoreCase = true) -> {
                        description = line.substringAfter(":").trim()
                    }
                }
            }

            if (title.isNotEmpty() && description.isNotEmpty()) {
                onSuccess(title, description)
            } else {
                // Fallback parsing if format is not followed exactly
                val parts = response.split("TITLE:", "DESCRIPTION:", ignoreCase = true)
                if (parts.size >= 3) {
                    title = parts[1].trim()
                    description = parts[2].trim()
                    onSuccess(title, description)
                } else {
                    onError("Could not parse Gemini response")
                }
            }
        } catch (e: Exception) {
            onError("Error parsing response: ${e.message}")
        }
    }

    fun resetState() {
        hasStoppedListening = false
        _voiceState.value = VoiceState.IDLE
        _transcript.value = ""
        _isListening.value = false
        _errorMessage.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer?.destroy()
    }
}
