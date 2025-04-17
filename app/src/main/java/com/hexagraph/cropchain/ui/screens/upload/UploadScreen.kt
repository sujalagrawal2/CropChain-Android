package com.hexagraph.cropchain.ui.screens.upload

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hexagraph.cropchain.R


@Composable
fun UploadScreen(
    modifier: Modifier = Modifier,
) {
    val uploadImageScreen = remember { mutableStateOf(true) }
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Button(onClick = {
                uploadImageScreen.value = true
            }) {
                Row(modifier = Modifier.height(24.dp)) {
                    Image(
                        painter = painterResource(R.drawable.upload_image2),
                        contentDescription = "Upload Image Icon",
                    )
                    Text(stringResource(R.string.upload_image))
                }
            }
            Button(onClick = {
                uploadImageScreen.value = false
            }) {
                Text(stringResource(R.string.status))
            }
        }
        if (uploadImageScreen.value) UploadImageScreen(goToStatusScreen = {
            uploadImageScreen.value = false
        })
        else ImageStatusScreen()
    }
}



