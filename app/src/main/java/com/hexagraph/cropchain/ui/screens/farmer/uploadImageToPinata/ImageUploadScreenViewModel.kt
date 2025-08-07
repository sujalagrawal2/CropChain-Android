package com.hexagraph.cropchain.ui.screens.farmer.uploadImageToPinata

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.model.CropImages
import com.hexagraph.cropchain.domain.model.RecentActivity
import com.hexagraph.cropchain.domain.repository.CropRepository
import com.hexagraph.cropchain.domain.repository.CropTableRepository
import com.hexagraph.cropchain.domain.repository.MetaMaskSDKRepository
import com.hexagraph.cropchain.domain.repository.MetaMaskRepository
import com.hexagraph.cropchain.domain.repository.PinataRepository
import com.hexagraph.cropchain.domain.repository.RecentActivityRepository
import com.hexagraph.cropchain.workManager.WorkManagerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class ImageUploadUIState(
    val cropImages: List<CropImages> = emptyList(),
    val isUploading: Boolean = false,
    val uploadProgress: Map<Long, Int> = emptyMap(),
    val allImagesUploaded: Boolean = false,
    val isBlockchainUploadInProgress: Boolean = false,
    val isMetaMaskConnected: Boolean = false
)

@HiltViewModel
class ImageUploadScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cropRepository: CropRepository,
    private val cropTableRepository: CropTableRepository,
    private val pinataRepository: PinataRepository,
    private val metaMaskSDKRepository: MetaMaskSDKRepository,
    private val metaMaskRepository: MetaMaskRepository,
    private val workManagerRepository: WorkManagerRepository,
    private val appPreferences: AppPreferences,
    private val recentActivityRepository: RecentActivityRepository
) : ViewModel() {

    private val _title = mutableStateOf("")
    val title: State<String> = _title

    private val _description = mutableStateOf("")
    val description: State<String> = _description

    private val _uiState = mutableStateOf(ImageUploadUIState())
    val uiState: State<ImageUploadUIState> = _uiState

    private var currentCropId: Long? = null
    fun startViewModel(cropId: Long?) {
        if (cropId == null) {
            createEmptyCrop()
            checkMetaMaskConnection()
        } else {
            initializeCrop(cropId)
        }
    }


    private fun createEmptyCrop() {
        viewModelScope.launch {
            val currentDate =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val emptyCrop = Crop(createdDate = currentDate)
            currentCropId = cropTableRepository.insertCrop(emptyCrop)
            loadCropImages()
        }
    }

    private fun loadCropImages() {
        currentCropId?.let { cropId ->
            viewModelScope.launch {
                cropRepository.getCropImagesByCropId(cropId).collect { images ->
                    _uiState.value = _uiState.value.copy(
                        cropImages = images,
                        allImagesUploaded = images.isNotEmpty() && images.all { it.uploadedToPinata == 1 }
                    )
                }
            }
        }
    }

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateDescription(newDescription: String) {
        _description.value = newDescription
    }

    fun addImageUri(uri: Uri) {
        currentCropId?.let { cropId ->
            viewModelScope.launch {
                val file = copyUriToInternalStorage(context, uri)

                if (file != null) {
                    val currentDate =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                    val cropImage = CropImages(
                        uid = uri.toString(),
                        date = currentDate,
                        fileName = file.name,
                        cropId = cropId,
                        localPath = file.absolutePath,
                        uploadProgress = 0,
                        uploadedToPinata = -1
                    )

                    val imageId = cropRepository.insertCropImage(cropImage)
                    startPinataUpload(imageId, file)
                }
            }
        }
    }

    fun addMultipleImageUris(uris: List<Uri>) {
        uris.forEach { uri ->
            addImageUri(uri)
        }
    }

    fun removeImageUri(imageId: Long) {
        viewModelScope.launch {
            val cropImage = cropRepository.getCropImageById(imageId)
            cropImage?.let {
                // Delete local file
                File(it.localPath).delete()
                // Remove from database
                cropRepository.deleteCropImageById(imageId)
            }
        }
    }

    private suspend fun copyUriToInternalStorage(context: Context, uri: Uri): File? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    val fileName = "crop_${System.currentTimeMillis()}.jpg"
                    val file = File(context.filesDir, fileName)
                    val outputStream = FileOutputStream(file)

                    inputStream.copyTo(outputStream)
                    inputStream.close()
                    outputStream.close()

                    file
                } else null
            } catch (e: Exception) {
                Log.e("ImageUpload", "Error copying file: ${e.message}")
                null
            }
        }
    }

    private fun startPinataUpload(imageId: Long, file: File) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isUploading = true)

                val result = pinataRepository.uploadImageToPinata(file) { progress ->
                    viewModelScope.launch {
                        cropRepository.updateUploadProgress(imageId, progress)
                        _uiState.value = _uiState.value.copy(
                            uploadProgress = _uiState.value.uploadProgress + (imageId to progress)
                        )
                    }
                }

                result.onSuccess { ipfsHash ->
                    val ipfsUrl = "https://gateway.pinata.cloud/ipfs/$ipfsHash"
                    cropRepository.updatePinataUploadStatus(imageId, 1, ipfsUrl)
                    Log.d("PinataUpload", "Successfully uploaded: $ipfsUrl")
                }.onFailure { exception ->
                    cropRepository.updatePinataUploadStatus(imageId, 0, null)
                    Log.e("PinataUpload", "Upload failed: ${exception.message}")
                }

            } catch (e: Exception) {
                cropRepository.updatePinataUploadStatus(imageId, 0, null)
                Log.e("PinataUpload", "Upload error: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isUploading = false)
                checkAllUploadsComplete()
            }
        }
    }

    private suspend fun checkAllUploadsComplete() {
        currentCropId?.let { cropId ->
            val totalImages = cropRepository.getCropImagesCount(cropId)
            val uploadedImages = cropRepository.getUploadedCropImagesCount(cropId)

            _uiState.value = _uiState.value.copy(
                allImagesUploaded = totalImages > 0 && totalImages == uploadedImages
            )
        }
    }

    private fun checkMetaMaskConnection() {
        viewModelScope.launch {
            val isConnected = metaMaskSDKRepository.walletAddress.isNotEmpty()
            _uiState.value = _uiState.value.copy(isMetaMaskConnected = isConnected)
        }
    }

    fun connectMetaMask(onNavigateToProfile: () -> Unit) {
        metaMaskSDKRepository.connect(
            onError = { error ->
                viewModelScope.launch {
                    appPreferences.metaMaskMessage.set("MetaMask connection failed: $error")
                }
            }
        ) { connectedAccounts ->
            viewModelScope.launch {
                // Update account connection status
                val accounts = metaMaskRepository.getAllAccounts().first()
                accounts.forEach { account ->
                    var isAccountConnected = false
                    connectedAccounts.forEach { connectedAccount ->
                        if (account.account == connectedAccount) {
                            isAccountConnected = true
                        }
                    }
                    if (isAccountConnected != account.isConnected) {
                        metaMaskRepository.updateAccount(account.copy(isConnected = isAccountConnected))
                    }
                }

                // Update UI state
                _uiState.value = _uiState.value.copy(isMetaMaskConnected = true)

                // Navigate to profile if no connected accounts
                if (connectedAccounts.isEmpty()) {
                    onNavigateToProfile()
                }
            }
        }
    }

    fun uploadToBlockchain(context: Context, onComplete: () -> Unit) {
        currentCropId?.let { cropId ->
            viewModelScope.launch {
                try {
                    // Check MetaMask connection first
                    if (!_uiState.value.isMetaMaskConnected) {
                        appPreferences.metaMaskMessage.set("Please connect MetaMask first")
                        return@launch
                    }

                    _uiState.value = _uiState.value.copy(isBlockchainUploadInProgress = true)

                    // Update crop details first
                    val locationData = appPreferences.locationData.getFlow().first()
                    cropTableRepository.updateCropDetails(
                        cropId = cropId,
                        title = _title.value,
                        description = _description.value,
                        latitude = locationData.latitude,
                        longitude = locationData.longitude,
                        address = locationData.address
                    )

                    // Get all uploaded images for this crop
                    val uploadedImages = cropRepository.getCropImagesByCropIdSync(cropId)
                        .filter { it.uploadedToPinata == 1 }

                    if (uploadedImages.isEmpty()) {
                        appPreferences.metaMaskMessage.set("No images uploaded to proceed")
                        return@launch
                    }

                    // Create crops string for blockchain
                    val cropsString = uploadedImages.joinToString(separator = "$") { it.url ?: "" }

                    // Upload to blockchain
                    val result = metaMaskSDKRepository.uploadImage(
                        crops = cropsString,
                        title = _title.value,
                        description = _description.value,
                        locationData = locationData
                    )

                    result.onSuccess { txHash ->
                        // Update crop as uploaded to blockchain
                        cropTableRepository.updateBlockchainStatus(cropId, true, txHash)
                        appPreferences.metaMaskMessage.set("Successfully uploaded to blockchain!")
                        recentActivityRepository.insertActivity(
                            RecentActivity(
                                type = 0,
                                idOfRecentActivity = cropId.toInt(),
                                transactionHash = txHash,
                                isSeen = false,
                                status = 0,
                                title = _title.value
                            )
                        )
                        onComplete()
                    }.onFailure { exception ->
                        appPreferences.metaMaskMessage.set("Blockchain upload failed: ${exception.message}")
                        Log.e("BlockchainUpload", "Upload failed: ${exception.message}")
                    }

                } catch (e: Exception) {
                    appPreferences.metaMaskMessage.set("Upload error: ${e.message}")
                    Log.e("BlockchainUpload", "Upload error: ${e.message}")
                } finally {
                    _uiState.value = _uiState.value.copy(isBlockchainUploadInProgress = false)
                }
            }
        }
    }

    // Legacy method for compatibility with existing code
    fun insertCrops(context: Context, onComplete: () -> Unit) {
        uploadToBlockchain(context, onComplete)
    }

    // Get image URIs for UI compatibility
    val imageUris: State<List<Uri>> = mutableStateOf(emptyList())

    fun updateImageUris(uris: List<Uri>) {
        addMultipleImageUris(uris)
    }

    fun removeImageUri(uri: Uri) {
        val cropImage = _uiState.value.cropImages.find { it.uid == uri.toString() }
        cropImage?.let { removeImageUri(it.id) }
    }


    fun extractIpfsHash(input: String): String? {
        // If the input is already just an IPFS hash (starts with "Qm" and is 46 characters long)
        if (input.startsWith("Qm") && input.length == 46) {
            return input
        }

        // If the input is a Pinata gateway URL
        val pinataUrlRegex = "https://gateway\\.pinata\\.cloud/ipfs/(Qm[a-zA-Z0-9]{44})".toRegex()
        val matchResult = pinataUrlRegex.find(input)

        return matchResult?.groupValues?.get(1)
    }

    fun initializeCrop(cropId: Long) {
        currentCropId = cropId
        viewModelScope.launch {
            val crop = cropTableRepository.getCropById(cropId)
            crop?.let {
                _title.value = it.title
                _description.value = it.description
            }
            loadCropImages()
            checkMetaMaskConnection()
        }
    }


}
