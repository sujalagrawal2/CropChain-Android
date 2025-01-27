package com.hexagraph.cropchain.ui.screens.dashboard

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexagraph.cropchain.ui.component.DisplayImageFromIPFS
import kotlinx.coroutines.flow.collect

@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        Text("Dashboard", fontSize = 32.sp, modifier = Modifier.padding(8.dp))

        UploadedImages(Modifier.fillMaxWidth(), dashboardViewModel)

        RecentActivity(Modifier.fillMaxWidth())
    }
}

@Composable
fun UploadedImages(modifier: Modifier = Modifier, dashboardViewModel: DashboardViewModel) {
    val pics = dashboardViewModel.imagesss.collectAsState().value
    Text(
        "Uploaded Images",
        modifier = Modifier.padding(horizontal = 16.dp),
        fontSize = 24.sp
    )
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        Button(
            onClick = {
                dashboardViewModel.getUploadedImages()
                      println("Hello ${pics.size}")
                      },
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text("Pending")
        }
        Button(onClick = {}, modifier = Modifier.padding(end = 8.dp)) { Text("Verified") }
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(pics) { pic ->
            Log.e("smth", pic)
            DisplayImageFromIPFS(cid = pic)
        }
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