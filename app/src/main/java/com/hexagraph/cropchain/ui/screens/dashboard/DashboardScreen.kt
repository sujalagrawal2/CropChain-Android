package com.hexagraph.cropchain.ui.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("Dashboard", fontSize = 32.sp, modifier = Modifier.padding(8.dp))

        UploadedImages(Modifier.fillMaxWidth())

        RecentActivity(Modifier.fillMaxWidth())
    }
}

@Composable
fun UploadedImages(modifier: Modifier = Modifier) {
    Text(
        "Uploaded Images",
        modifier = Modifier.padding(horizontal = 16.dp),
        fontSize = 24.sp
    )
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        Button(onClick = {}, modifier = Modifier.padding(end = 8.dp)) {
            Text("Pending")
        }
        Button(onClick = {}, modifier = Modifier.padding(end = 8.dp)) { Text("Verified") }
    }
}

@Composable
fun RecentActivity(modifier: Modifier = Modifier) {
    Text(
        "Recent Activity",
        modifier = modifier.padding(horizontal = 16.dp),
        fontSize = 24.sp
    )
}