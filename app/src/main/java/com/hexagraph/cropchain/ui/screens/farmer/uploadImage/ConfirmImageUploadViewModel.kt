package com.hexagraph.cropchain.ui.screens.farmer.uploadImage

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.repository.CropRepository
import com.hexagraph.cropchain.util.getCurrentTimestamp
import com.hexagraph.cropchain.util.saveImageToInternalStorage
import com.hexagraph.cropchain.workManager.WorkManagerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ConfirmImageUploadViewModel @Inject constructor(
    private val cropRepository: CropRepository,
    private val workManager: WorkManagerRepository,
) : ViewModel() {

    private val _title = mutableStateOf("")
    val title: State<String> = _title

    private val _description = mutableStateOf("")
    val description: State<String> = _description

    private val _imageUris = mutableStateOf<List<Uri>>(emptyList())
    val imageUris: State<List<Uri>> = _imageUris

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateDescription(newDescription: String) {
        _description.value = newDescription
    }

    fun updateImageUris(uris: List<Uri>) {
        _imageUris.value = uris
    }

    fun addImageUri(uri: Uri) {
        _imageUris.value = _imageUris.value + uri
    }

    fun removeImageUri(uri: Uri) {
        _imageUris.value = _imageUris.value.filter { it != uri }
    }

    fun insertCrops(context: Context, onCompleted: () -> Unit) {
        viewModelScope.launch {
            _imageUris.value.forEach { uri ->
                val newUri = saveImageToInternalStorage(context, uri)
                val fileName =
                    if (newUri == null || newUri.path == null) "Unknown Image" else File(newUri.path!!).name
                cropRepository.insertCrop(
                    Crop(
                        date = getCurrentTimestamp(),
                        uid = newUri.toString(),
                        fileName = fileName,
                        title = _title.value.ifBlank { "Unknown Crop" },
                        description = _description.value.ifBlank { "No Description" }
                    )
                )
            }
            workManager.count()
            onCompleted()
        }
    }

    fun resetForm() {
        _title.value = ""
        _description.value = ""
        _imageUris.value = emptyList()
    }
}