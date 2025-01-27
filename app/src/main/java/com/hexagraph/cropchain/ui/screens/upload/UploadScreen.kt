package com.hexagraph.cropchain.ui.screens.upload

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun UploadScreen(
    modifier: Modifier = Modifier,
    viewModel: UploadScreenViewModel = hiltViewModel(),
//    @ApplicationContext context: Context
) {

    val uploadImageStatus by viewModel.uploadImageStatus


    Column(modifier = modifier) {
        Text("Upload", fontSize = 32.sp, modifier = Modifier.padding(8.dp))

        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            selectedImageUri = uri
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = "Select Image")
            }

            Spacer(modifier = Modifier.height(16.dp))

            selectedImageUri?.let { uri ->
                val context = LocalContext.current
                val file = uriToFile(context, uri)
                Row {
                    Button(onClick = {
                        viewModel.uploadImageToPinata(file)
                    }) {
                        Text(text = "Upload Image")
                    }
                    if (uploadImageStatus == UploadImageStatus.COMPLETED) {
                        var iconVisible by remember { mutableStateOf(true) }
                        if (iconVisible)
                            Icon(
                                imageVector = Icons.Filled.Check, // Built-in Material icon
                                contentDescription = "Favorite Icon", // Accessibility description
                                tint = Color.Green, // Icon color
                                modifier = Modifier.size(40.dp)

                            )
                        Toast.makeText(context, "Uploaded!", Toast.LENGTH_SHORT)
                            .show()
                        CoroutineScope(Dispatchers.IO).launch {
                            delay(3000)
                            iconVisible = false
                        }
                    }
                    if (uploadImageStatus == UploadImageStatus.LOADING) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(50.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 4.dp
                        )
                    }
                    if (uploadImageStatus == UploadImageStatus.ERROR) {
                        Toast.makeText(context, "Error in Uploading Image!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    if (uploadImageStatus == UploadImageStatus.FAILED) {
                        Toast.makeText(
                            context,
                            "Failed Please check your Internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }
                val uploadImageToBlockChainStatue by viewModel.uploadImageToBlockChainStatue
                if (uploadImageStatus == UploadImageStatus.COMPLETED) {
                    Row {
                        Button(onClick = { viewModel.uploadImageToBlockChain() }) {
                            Text("Upload to BlockChain")
                        }
                        if (uploadImageToBlockChainStatue == UploadImageStatus.COMPLETED) {
                            var iconVisible by remember { mutableStateOf(true) }
                            if (iconVisible)
                                Icon(
                                    imageVector = Icons.Filled.Check, // Built-in Material icon
                                    contentDescription = "Favorite Icon", // Accessibility description
                                    tint = Color.Green, // Icon color
                                    modifier = Modifier.size(40.dp)

                                )
                            Toast.makeText(context, "Uploaded!", Toast.LENGTH_SHORT)
                                .show()
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(3000)
                                iconVisible = false
                            }
                        }
                        if (uploadImageToBlockChainStatue == UploadImageStatus.LOADING) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(50.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 4.dp
                            )
                        }
                        if (uploadImageToBlockChainStatue == UploadImageStatus.ERROR) {
                            Toast.makeText(context, "Error in Uploading Image!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
//            DisplayImageFromIPFS("QmSR2CUHtG5wffxHaxinjs1gE3V4Yf6fgKJ9guqbCzRNuJ")
        }
    }
}

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, "temp_image")
    file.outputStream().use { output ->
        inputStream?.copyTo(output)
    }
    return file
}
