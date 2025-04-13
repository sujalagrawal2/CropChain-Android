package com.hexagraph.cropchain.farmer.ui.screen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.hexagraph.cropchain.farmer.ui.viewModels.UploadImageViewModel
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.platform.LocalContext

@Composable
fun UploadImageScreen(
    viewModel: UploadImageViewModel = hiltViewModel(),
    onBackButtonPressed: () -> Unit,
    goToUploadStatusScreen: () -> Unit
) {
    val context = LocalContext.current
    val showBackDialog = remember { mutableStateOf(false) }
    val imageUris = remember { mutableStateOf<List<Uri>>(emptyList()) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        imageUris.value = uris
        Log.d("UploadImageScreen", uris.toString())
    }
    LaunchedEffect(Unit) {
        launcher.launch(arrayOf("image/*"))
    }
    if (showBackDialog.value) {
        AlertDialog(
            onDismissRequest = { showBackDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    showBackDialog.value = false
                    onBackButtonPressed()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBackDialog.value = false }) {
                    Text("No")
                }
            },
            title = { Text("Discard Changes?") },
            text = { Text("Are you sure you want to discard selected images?") }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Row {
            IconButton(onClick = {
                showBackDialog.value = true
            }) {
                Icon(
                    imageVector =
                        Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Text(
                text = "Upload Image",
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(imageUris.value) { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.insertCrops(imageUris.value, context)
                goToUploadStatusScreen()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Upload")
        }
    }
}