package com.hexagraph.cropchain.ui.screens.farmer.uploadImage

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.Web3J
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
    private val web3j: Web3J,
    private val workManager: WorkManagerRepository
) : ViewModel() {
    fun insertCrops(uid: List<Uri>, context: Context, onCompleted: () -> Unit) {
        viewModelScope.launch {
            uid.forEach {
                val newUri = saveImageToInternalStorage(context, it)
                val fileName =
                    if (newUri == null || newUri.path == null) "Unknown Image" else File(newUri.path!!).name
                cropRepository.insertCrop(
                    Crop(
                        date = getCurrentTimestamp(),
                        uid = newUri.toString(),
                        fileName = fileName
                    )
                )
            }
            workManager.count()
            onCompleted()
        }
    }
}