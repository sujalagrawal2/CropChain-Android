package com.hexagraph.cropchain.ui.screens.verifier

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexagraph.cropchain.ui.component.DisplayImageFromIPFS

@Composable
fun VerifierScreen(modifier: Modifier, viewModel: VerifierScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState

    LazyColumn(modifier = modifier) {
        item {
            uiState.images.forEachIndexed { index, image ->
                Log.d("Verifier Screen", image)
                val result = uiState.imageResult
                if (image[0] == 'h')
                    LazyRow {
                        item {
                            DisplayImageFromIPFS(cid = image)
                            Column {
                                if (index < result.size)
                                    Text("AI Solution : ${result[index].aiSolution}")
                                Spacer(modifier = Modifier.height(4.dp))
                                if (index < result.size)
                                    Text("Scientist Solution : ${result[index].scientistSolution}")
                                Spacer(modifier = Modifier.height(4.dp))
                                Button(onClick = {
                                    viewModel.verifyImage(image, true)
                                }) {
                                    Text("Like")
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Button(onClick = {
                                    viewModel.verifyImage(image, false)
                                }) {
                                    Text("DisLike")
                                }
                            }
                            if (index < result.size)
                                Text("Scientist Solution : ${result[index].verificationCount}")
                        }
                    }
            }
        }
    }

}