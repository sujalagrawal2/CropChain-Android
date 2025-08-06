//package com.hexagraph.cropchain.ui.screens.farmer.uploadImageStatus
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBackIosNew
//import androidx.compose.material.icons.filled.Check
//import androidx.compose.material.icons.filled.CheckCircle
//import androidx.compose.material.icons.filled.Refresh
//import androidx.compose.material.icons.filled.Warning
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.LinearProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import coil.compose.rememberAsyncImagePainter
//import com.hexagraph.cropchain.R
//import com.hexagraph.cropchain.domain.model.CropImages
//
//@Composable
//fun UploadImageToBlockchainScreen(
//    onBackButtonPressed: () -> Unit,
//    viewModel: UploadStatusViewModel = hiltViewModel(),
//    goToProfileScreen: () -> Unit
//) {
//    val uiState = viewModel.uiState
//    val status = viewModel.status
//    val isConnected = viewModel.isConnected()
//
//
//    LaunchedEffect(status) {
//        if (status.value == ScreenStatus.LOADING) {
//            viewModel.getAllCrops()
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//            .padding(16.dp)
//    ) {
//        // Top AppBar
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            IconButton(onClick = onBackButtonPressed) {
//                Icon(
//                    imageVector = Icons.Default.ArrowBackIosNew,
//                    contentDescription = null,
//                    tint = Color.White
//                )
//            }
//            Text(
//                text = stringResource(R.string.upload_status),
//                style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
//                modifier = Modifier.padding(start = 8.dp)
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            IconButton(onClick = { viewModel.uploadAllImages() }) {
//                Icon(
//                    imageVector = Icons.Default.Refresh,
//                    contentDescription = null,
//                    tint = Color.White
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//
//        // Upload Status List
//        when (status.value) {
//            ScreenStatus.LOADING -> {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator(color = Color.Green, strokeWidth = 4.dp)
//                }
//            }
//
//            ScreenStatus.COMPLETED -> {
//                LazyColumn(
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    item {
//                        // MetaMask Connection Status
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clip(RoundedCornerShape(12.dp)),
//                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
//                            elevation = CardDefaults.cardElevation(4.dp)
//                        ) {
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(16.dp)
//                            ) {
//                                Row(
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Icon(
//                                        imageVector = if (isConnected) Icons.Default.CheckCircle else Icons.Default.Warning,
//                                        contentDescription = null,
//                                        tint = if (isConnected) Color(0xFF2ECC71) else Color(
//                                            0xFFFF6347
//                                        )
//                                    )
//                                    Spacer(modifier = Modifier.width(8.dp))
//                                    Text(
//                                        text = if (isConnected) stringResource(R.string.metamask_connected) else "Account not selected",
//                                        color = Color.White,
//                                        style = MaterialTheme.typography.bodyMedium
//                                    )
//                                }
//
//                                if (!isConnected) {
//                                    Spacer(modifier = Modifier.height(12.dp))
//                                    Button(
//                                        onClick = { goToProfileScreen() },
//                                        colors = ButtonDefaults.buttonColors(
//                                            containerColor = Color(
//                                                0xFF0077CC
//                                            )
//                                        ),
//                                        modifier = Modifier.align(Alignment.End)
//                                    ) {
//                                        Text("Select the account", color = Color.White)
//                                    }
//                                }
//                            }
//                        }
//
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        // Upload to Blockchain Button
//                        val anyUploaded =
//                            uiState.value.cropList.any { it.uploadedToPinata == 1 }
//                        Button(
//                            onClick = { viewModel.uploadToBlockChain() },
//                            enabled = isConnected && anyUploaded,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(52.dp),
//                            shape = RoundedCornerShape(12.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = if (isConnected && anyUploaded) Color(0xFF4CAF50) else Color.Gray,
//                                disabledContainerColor = Color.DarkGray
//                            )
//                        ) {
//                            Text("Send Uploaded Images to Blockchain", color = Color.White)
//                        }
//
//                        Spacer(modifier = Modifier.height(24.dp))
//                        uiState.value.cropList.forEach { crop ->
//                            UploadStatusCard(
//                                cropImages = crop,
//                                onProgressUpdate = { viewModel.setProgress(it) }
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                        }
//                    }
//                }
//            }
//
//            else -> {
//                Text("Error occurred", color = Color.Red)
//            }
//        }
//
//    }
//}
//
//@Composable
//fun UploadStatusCard(
//    cropImages: CropImages,
//    onProgressUpdate: (Int) -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(16.dp)),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
//        elevation = CardDefaults.cardElevation(6.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Crop Image using UID
//            Image(
//                painter = rememberAsyncImagePainter(model = cropImages.uid),
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(72.dp)
//                    .clip(RoundedCornerShape(12.dp))
//                    .background(Color.DarkGray)
//            )
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    text = cropImages.fileName,
//                    color = Color.White,
//                    style = MaterialTheme.typography.bodyMedium,
//                    maxLines = 1
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Text(
//                    text = when {
//                        cropImages.uploadedToPinata == 1 -> "✅ Uploaded to Pinata"
//                        cropImages.uploadedToPinata == 0 -> "Error in uploading. "
//                        cropImages.uploadProgress == 0 -> "Pending"
//                        else -> "⏳ Uploading to Pinata..."
//                    },
//                    color = Color.LightGray,
//                    fontSize = 12.sp
//                )
//
//                if (cropImages.uploadedToPinata == -1) {
//                    Spacer(modifier = Modifier.height(8.dp))
//                    LinearProgressIndicator(
//                        progress = (cropImages.uploadProgress / 100f), // Replace with actual progress if available
//                        modifier = Modifier.fillMaxWidth(),
//                        color = Color(0xFF2ECC71),
//                        trackColor = Color.DarkGray
//                    )
//                }
//            }
//
//            if (cropImages.uploadedToPinata == 1) {
//                Icon(
//                    imageVector = Icons.Default.Check,
//                    contentDescription = null,
//                    tint = Color(0xFF2ECC71),
//                    modifier = Modifier
//                        .padding(start = 12.dp)
//                        .size(24.dp)
//                )
//            }
//        }
//    }
//}
