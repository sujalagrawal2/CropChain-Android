package com.hexagraph.cropchain.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("Profile", fontSize = 32.sp, modifier = Modifier.padding(8.dp))
    }
}