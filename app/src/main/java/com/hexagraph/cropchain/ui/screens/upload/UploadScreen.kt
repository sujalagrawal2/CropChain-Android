package com.hexagraph.cropchain.ui.screens.upload

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UploadScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("Upload", fontSize = 32.sp, modifier = Modifier.padding(8.dp))
    }
}