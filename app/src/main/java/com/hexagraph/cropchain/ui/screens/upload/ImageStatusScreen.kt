package com.hexagraph.cropchain.ui.screens.upload

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hexagraph.cropchain.util.ScreenStatus

@Composable
fun ImageStatusScreen(viewModel: ImageStatusViewModel = hiltViewModel()) {
    val status = viewModel.status
    val uiState by viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.getAllCrops()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (status.value == ScreenStatus.LOADING) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xAA000000)), // Semi-transparent background
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        uiState.cropList.forEach { it ->
                            Row {
                                Log.d("Testing", it.uid)
                                Image(
                                    painter = rememberAsyncImagePainter(Uri.parse(it.uid)),
                                    contentDescription = "Selected Image",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(8.dp)
                                )
                                if (it.uploadedToBlockChain) Text("2")
                                else if (it.uploadedToPinata == 1) Text("1")
                                else Text("0")
                            }
                        }
                    }
                }
            }

        }
    }
}