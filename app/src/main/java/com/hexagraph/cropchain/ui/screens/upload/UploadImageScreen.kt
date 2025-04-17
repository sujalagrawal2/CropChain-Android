package com.hexagraph.cropchain.ui.screens.upload

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.hexagraph.cropchain.R

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun UploadImageScreen(
    viewModel: UploadImageViewModel = hiltViewModel(),
    goToStatusScreen: () -> Unit
) {
    val context = LocalContext.current
    val imageUris = remember { mutableStateOf<List<Uri>>(emptyList()) }
    val imagesSelected = remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        imageUris.value = uris
    }
    Column {
        if (!imagesSelected.value)
            CenterCardWithIllustration(
                modifier = Modifier.padding(16.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        launcher.launch(arrayOf("image/*"))
                        imagesSelected.value = true
                    }) {
                        Text(text = stringResource(R.string.select_image))
                    }
                    Spacer(modifier = Modifier.height(16.dp))

//                Spacer(modifier = Modifier.height(16.dp))
//
//                imageUris?.let { uri ->
//                    val context = LocalContext.current
//                    val file = uriToFile(context, uri)
//                    Row {
//                        Button(onClick = {
//                            viewModel.uploadImage(file)
//                        }) {
//                            Text(text = "Upload Image")
//                        }
//                        if (uiState.uploadImageStatus == UploadImageStatus.COMPLETED) {
//                            var iconVisible by remember { mutableStateOf(true) }
//                            if (iconVisible)
//                                Icon(
//                                    imageVector = Icons.Filled.Check, // Built-in Material icon
//                                    contentDescription = "Favorite Icon", // Accessibility description
//                                    tint = Color.Green, // Icon color
//                                    modifier = Modifier.size(40.dp)
//
//                                )
////                            Toast.makeText(context, "Uploaded!", Toast.LENGTH_SHORT)
////                                .show()
//                            CoroutineScope(Dispatchers.IO).launch {
//                                delay(3000)
//                                iconVisible = false
//                            }
//                        }
//                        if (uiState.uploadImageStatus == UploadImageStatus.LOADING) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.size(50.dp),
//                                color = MaterialTheme.colorScheme.primary,
//                                strokeWidth = 4.dp
//                            )
//                        }
//                        if (uiState.uploadImageStatus == UploadImageStatus.ERROR) {
//                            Toast.makeText(context, "Error in Uploading Image!", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                        if (uiState.uploadImageStatus == UploadImageStatus.FAILED) {
//                            Toast.makeText(
//                                context,
//                                "Failed Please check your Internet connection",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//
//
//                    }
//                    val uploadImageToBlockChainStatue by viewModel.uploadImageToBlockChainStatue
//                    if (uiState.uploadImageStatus == UploadImageStatus.COMPLETED) {
//                        Row {
//                            Button(onClick = { viewModel.uploadImageToBlockChain() }) {
//                                Text("Upload to BlockChain")
//                            }
//                            if (uploadImageToBlockChainStatue == UploadImageStatus.COMPLETED) {
//                                var iconVisible by remember { mutableStateOf(true) }
//                                if (iconVisible)
//                                    Icon(
//                                        imageVector = Icons.Filled.Check, // Built-in Material icon
//                                        contentDescription = "Favorite Icon", // Accessibility description
//                                        tint = Color.Green, // Icon color
//                                        modifier = Modifier.size(40.dp)
//
//                                    )
////                                Toast.makeText(context, "Uploaded!", Toast.LENGTH_SHORT)
////                                    .show()
//                                CoroutineScope(Dispatchers.IO).launch {
//                                    delay(3000)
//                                    iconVisible = false
//                                }
//                            }
//                            if (uploadImageToBlockChainStatue == UploadImageStatus.LOADING) {
//                                CircularProgressIndicator(
//                                    modifier = Modifier.size(50.dp),
//                                    color = MaterialTheme.colorScheme.primary,
//                                    strokeWidth = 4.dp
//                                )
//                            }
//                            if (uploadImageToBlockChainStatue == UploadImageStatus.ERROR) {
//                                Toast.makeText(
//                                    context,
//                                    "Error in Uploading Image!",
//                                    Toast.LENGTH_SHORT
//                                )
//                                    .show()
//                            }
//                        }
//                    }
//                }
                }
            }
        else {

            LazyColumn {
                item {
                    imageUris.value.forEach { uri ->
                        Log.d("Correct", uri.toString())
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = stringResource(R.string.selected_image),
                            modifier = Modifier
                                .size(100.dp)
                                .padding(8.dp)
                        )
                    }
                    Button(onClick = {
                        viewModel.insertCrops(imageUris.value, context)
                        goToStatusScreen()
                    }) {
                        Text(stringResource(R.string.upload))
                    }
                }
            }
        }

    }
}


@Composable
fun CenterCardWithIllustration(
    modifier: Modifier = Modifier,
    buttonContent: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.capture_illustration),
                contentDescription = stringResource(R.string.capture_illustration),
                modifier = Modifier
            )
            Text(text = stringResource(R.string.take_a_clear_picture_of_the_crop), fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            buttonContent()
        }
    }

}