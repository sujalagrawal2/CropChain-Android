package com.hexagraph.cropchain.ui.screens.scientist

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.annotations.Until
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.ui.component.DisplayImageFromIPFS

@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onFarmerSelect: (String) -> Unit
) {
    val uiState by viewModel.uiState

    LazyColumn(modifier = modifier) {
        uiState.images.forEachIndexed { farmerIndex, images ->
            item(key = "farmer-$farmerIndex") {
                Column(modifier = Modifier.clickable(onClick = {
                    onFarmerSelect(uiState.addresses[farmerIndex])
                })) {
                    Text(stringResource(R.string.farmer_1) +" ${farmerIndex + 1}")
                    LazyRow {
                        item {
                            images.forEach { image ->
                                Log.d("HomeScreen", image)
                                if (image[0] == 'h')
                                    DisplayImageFromIPFS(cid = image)
                            }
                        }
                    }
                }
            }
        }
    }

}