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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.ui.component.DisplayImageFromIPFS

@Composable
fun VerifierScreen(modifier: Modifier, viewModel: VerifierScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState

    LazyColumn(modifier = modifier) {
        item {
            uiState.images.forEachIndexed { index, image ->
                if (image.images[0] == 'h')
                    LazyRow {
                        item {
                            DisplayImageFromIPFS(cid = image.images)
                            Column {
                                Text(stringResource(R.string.ai_solution, image.aiSolution))
                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    stringResource(
                                        R.string.scientist_solution,
                                        image.scientistSolution
                                    ))
                                Spacer(modifier = Modifier.height(4.dp))
                                Button(onClick = {
                                    viewModel.verifyImage(image.images, true)
                                }) {
                                    Text(stringResource(R.string.like))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Button(onClick = {
                                    viewModel.verifyImage(image.images, false)
                                }) {
                                    Text(stringResource(R.string.dislike))
                                }
                            }
                            Text(stringResource(R.string.verification_count) +" : ${image.verificationCount}")
                        }
                    }
            }
        }
    }
}