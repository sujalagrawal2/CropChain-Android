package com.hexagraph.cropchain.ui.screens.dashboard

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.domain.model.RecentActivityType
import com.hexagraph.cropchain.ui.component.DisplayImageFromIPFS
import com.hexagraph.cropchain.ui.component.RecentActivityCard
import kotlinx.coroutines.flow.collect
import androidx.compose.runtime.getValue

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by dashboardViewModel.uiState
    Column(modifier = modifier) {

//        Text("Dashboard", fontSize = 32.sp, modifier = Modifier.padding(8.dp))
        DashboardScreenTitle(
            farmerName = uiState.userName,
            modifier = Modifier.padding(start = 8.dp)
        ) { /*TODO*/ }
        UploadedImagesContentWrapper(
            modifier = Modifier.padding(16.dp)
        ) {
            UploadedImages(Modifier.fillMaxWidth(), dashboardViewModel)
        }

        RecentActivity(Modifier.fillMaxWidth())
    }
}

@Composable
fun UploadedImages(modifier: Modifier = Modifier, dashboardViewModel: DashboardViewModel) {
    val pics by dashboardViewModel.uiState
    val verifiedImages = pics.verifiedImage
    Column {
        Row(
            modifier = modifier.padding(horizontal = 16.dp),
        ) {
            Button(
                onClick = {
                    dashboardViewModel.getUploadedImages()
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(stringResource(R.string.pending))
            }
            Button(
                onClick = {

                }, modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(stringResource(R.string.verified))
            }
        }
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(pics.verifiedImage) { pic ->
                Log.e("smth", pic)
                DisplayImageFromIPFS(
                    cid = pic,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        Button(onClick = {
            dashboardViewModel.writeReview()
        }) {
            Text(stringResource(R.string.review_image))
        }
        Button(onClick = {
            dashboardViewModel.connect()
        }) {
            Text(stringResource(R.string.connect))
        }
        Button(onClick = {
            dashboardViewModel.send()
        }) {
            Text(stringResource(R.string.send))
        }
        if (dashboardViewModel.isConnected()) {
            Text(stringResource(R.string.connected))
        } else {
            Text(stringResource(R.string.not_connected))
        }
    }
}

@Composable
fun RecentActivity(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.recent_activity_icon),
                contentDescription = stringResource(R.string.upload_image_icon),
                modifier = Modifier
                    .size(36.dp)
                    .padding(top = 8.dp)
            )
            Text(
                stringResource(R.string.recent_activity),
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(RecentActivityType.entries.size) {
                val type = RecentActivityType.entries[it]
                RecentActivityCard(
                    timeStamp = "5th January 2025 12:00 PM",
                    type = type
                )
            }
        }
    }
}


@Composable
fun UploadedImagesContentWrapper(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .widthIn(min = 200.dp)
    ) {
        Row {
            Image(
                painter = painterResource(R.drawable.imagesnew),
                contentDescription = "Upload Image Icon",
                modifier = Modifier
                    .size(36.dp)
                    .padding(top = 8.dp)
            )
            Text(
                stringResource(R.string.uploaded_images),
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )
        }
        content()
    }
}


@Composable
fun DashboardScreenTitle(
    modifier: Modifier = Modifier,
    farmerName: String = "Dilip Gogoi",
    onClickSettings: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = onClickSettings
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
        Row {
            Image(
                painter = painterResource(R.drawable.farmer_icon_with_crop),
                contentDescription = "Dashboard Icon",
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 8.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    stringResource(R.string.hello),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 16.sp
                )
                Text(farmerName, fontSize = 20.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenTitlePreview() {
    DashboardScreenTitle() {}
}