package com.hexagraph.cropchain.ui.screens.farmer.uploadImage

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.hexagraph.cropchain.R

@Composable
fun ConfirmImageUploadScreen(
    viewModel: ConfirmImageUploadViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
    onBackButtonPressed: () -> Unit,
    goToUploadStatusScreen: () -> Unit
) {
    val context = LocalContext.current
    val showBackDialog = remember { mutableStateOf(false) }
    val showVoiceBottomSheet = remember { mutableStateOf(false) }
    val showLocationPermissionDialog = remember { mutableStateOf(false) }

    val title by viewModel.title
    val description by viewModel.description
    val imageUris by viewModel.imageUris

    // Location states
    val permissionState by locationViewModel.permissionState
    val currentLocation by locationViewModel.currentLocation
    val isLoadingLocation by locationViewModel.isLoadingLocation
    val locationError by locationViewModel.locationError
    val customAddress by locationViewModel.customAddress
    val isEditingAddress by locationViewModel.isEditingAddress

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            if (imageUris.isEmpty()) {
                viewModel.updateImageUris(uris)
            } else {
                uris.forEach { uri ->
                    viewModel.addImageUri(uri)
                }
            }
        }
        Log.d("UploadImageScreen", uris.toString())
    }

    // Location permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationViewModel.handlePermissionResult(context, permissions)
    }

    LaunchedEffect(Unit) {
        if (imageUris.isEmpty()) {
            launcher.launch(arrayOf("image/*"))
        }
        locationViewModel.initializeLocationClient(context)
    }

    if (showBackDialog.value) {
        AlertDialog(
            onDismissRequest = { showBackDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    showBackDialog.value = false
                    onBackButtonPressed()
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showBackDialog.value = false }) {
                    Text(stringResource(R.string.no))
                }
            },
            title = { Text(stringResource(R.string.discard_changes)) },
            text = { Text(stringResource(R.string.are_you_sure_you_want_discard_selected_images)) }
        )
    }

    // Location permission dialog for permanently denied
    if (showLocationPermissionDialog.value) {
        AlertDialog(
            onDismissRequest = { showLocationPermissionDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    showLocationPermissionDialog.value = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLocationPermissionDialog.value = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Location Permission Required") },
            text = { Text("Location permission has been permanently denied. Please enable it in app settings to use location features.") }
        )
    }

    // Voice bottom sheet
    VoiceBottomSheet(
        isVisible = showVoiceBottomSheet.value,
        imageUri = imageUris.firstOrNull(),
        onDismiss = { showVoiceBottomSheet.value = false },
        onResult = { generatedTitle, generatedDescription ->
            viewModel.updateTitle(generatedTitle)
            viewModel.updateDescription(generatedDescription)
        }
    )

    ImageUploadLayout(
        title = title,
        description = description,
        imageUris = imageUris,
        currentLocation = currentLocation,
        isLoadingLocation = isLoadingLocation,
        locationError = locationError,
        customAddress = customAddress,
        isEditingAddress = isEditingAddress,
        permissionState = permissionState,
        onBackPressed = { showBackDialog.value = true },
        onTitleChanged = viewModel::updateTitle,
        onDescriptionChanged = viewModel::updateDescription,
        onAddImagesPressed = { launcher.launch(arrayOf("image/*")) },
        onRemoveImage = viewModel::removeImageUri,
        onLocationPressed = {
            when (permissionState) {
                LocationPermissionState.NOT_REQUESTED, LocationPermissionState.DENIED -> {
                    locationViewModel.requestLocationPermission(context, locationPermissionLauncher)
                }
                LocationPermissionState.GRANTED -> {
                    locationViewModel.getCurrentLocation(context)
                }
                LocationPermissionState.PERMANENTLY_DENIED -> {
                    showLocationPermissionDialog.value = true
                }
            }
        },
        onEditAddressPressed = { locationViewModel.startEditingAddress() },
        onAddressChanged = { locationViewModel.updateCustomAddress(it) },
        onSaveAddressPressed = { locationViewModel.stopEditingAddress() },
        onVoicePressed = { showVoiceBottomSheet.value = true },
        onUploadPressed = {
            viewModel.insertCrops(context) {
                goToUploadStatusScreen()
            }
        }
    )
}

@Composable
fun ImageUploadLayout(
    title: String,
    description: String,
    imageUris: List<Uri>,
    currentLocation: LocationData?,
    isLoadingLocation: Boolean,
    locationError: String,
    customAddress: String,
    isEditingAddress: Boolean,
    permissionState: LocationPermissionState,
    onBackPressed: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onAddImagesPressed: () -> Unit,
    onRemoveImage: (Uri) -> Unit,
    onLocationPressed: () -> Unit,
    onEditAddressPressed: () -> Unit,
    onAddressChanged: (String) -> Unit,
    onSaveAddressPressed: () -> Unit,
    onVoicePressed: () -> Unit,
    onUploadPressed: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "Upload Images",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Images Preview Carousel
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Images (${imageUris.size})",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = onAddImagesPressed,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Images",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            if (imageUris.size > 3) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "More Images",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (imageUris.isNotEmpty()) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(imageUris) { uri ->
                                ImageThumbnail(
                                    uri = uri,
                                    onRemove = { onRemoveImage(uri) }
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { onAddImagesPressed() },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Images",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tap to add images",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Location Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Location",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        OutlinedButton(
                            onClick = onLocationPressed,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (isLoadingLocation) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Get Location",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (permissionState) {
                                    LocationPermissionState.NOT_REQUESTED -> "Get Location"
                                    LocationPermissionState.DENIED -> "Allow Location"
                                    LocationPermissionState.GRANTED -> "Refresh"
                                    LocationPermissionState.PERMANENTLY_DENIED -> "Settings"
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (locationError.isNotEmpty()) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = locationError,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (isEditingAddress) {
                        OutlinedTextField(
                            value = customAddress,
                            onValueChange = onAddressChanged,
                            label = { Text("Address") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            trailingIcon = {
                                IconButton(onClick = onSaveAddressPressed) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Save"
                                    )
                                }
                            }
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = currentLocation?.address ?: customAddress.ifEmpty { "No location set" },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(onClick = onEditAddressPressed) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Address",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title Field with Voice Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChanged,
                    label = { Text("Title") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onVoicePressed,
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(12.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Input",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChanged,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Primary Action Button
            Button(
                onClick = onUploadPressed,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = imageUris.isNotEmpty(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Upload",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun ImageThumbnail(
    uri: Uri,
    onRemove: () -> Unit
) {
    Box {
        Card(
            modifier = Modifier.size(100.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(24.dp)
                .background(
                    Color.Red.copy(alpha = 0.8f),
                    RoundedCornerShape(12.dp)
                )
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "×",
                color = Color.White,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageUploadLayoutPreview() {
    MaterialTheme {
        ImageUploadLayout(
            title = "Sample Crop Title",
            description = "This is a sample description for the crop images.",
            imageUris = emptyList(),
            currentLocation = LocationData(
                latitude = 0.0,
                longitude = 0.0,
                address = "123 Sample St, Sample City"
            ),
            isLoadingLocation = false,
            locationError = "",
            customAddress = "123 Sample St, Sample City",
            isEditingAddress = false,
            permissionState = LocationPermissionState.GRANTED,
            onBackPressed = {},
            onTitleChanged = {},
            onDescriptionChanged = {},
            onAddImagesPressed = {},
            onRemoveImage = {},
            onLocationPressed = {},
            onEditAddressPressed = {},
            onAddressChanged = {},
            onSaveAddressPressed = {},
            onVoicePressed = {},
            onUploadPressed = {}
        )
    }
}
