package com.hexagraph.cropchain.ui.screens.scientist

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.ui.component.DisplayImageFromIPFS

@Composable
fun FarmerScreen(
    address: String,
    viewModel: FarmerScreenViewModel = hiltViewModel(),
    modifier: Modifier
) {
    val uiState by viewModel.uiState
    LaunchedEffect(Unit) {
        viewModel.getAllImages(address)
    }
    Column(modifier = modifier) {
        LazyRow {
            item {
                uiState.images.forEach { image ->
                    Log.d("FarmerScreen", image)
                    if (image[0] == 'h')
                        DisplayImageFromIPFS(cid = image)
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = {
            viewModel.writeReview(uiState.images)
        }) {
            Text(stringResource(R.string.review))
        }
    }
}