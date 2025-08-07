@file:JvmName("UploadImageScreenKt")

package com.hexagraph.cropchain.ui.screens.farmer.uploadImageToPinata

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hexagraph.cropchain.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageUploadPromptScreen(
    goToUploadStatusScreen: () -> Unit = {},
    goToUploadImageScreen: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.upload_images),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(Modifier.height(52.dp))
        CenterCardWithIllustration(
            modifier = Modifier.padding(16.dp),

            ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    goToUploadImageScreen()
                }) {
                    Text(text = stringResource(R.string.select_image))
                }
                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }

}

@Composable
fun CenterCardWithIllustration(
    modifier: Modifier = Modifier,
    buttonContent: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,

        ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.capture_illustration),
                contentDescription = "Capture Illustration",
                modifier = Modifier
            )
            Text(text = stringResource(R.string.take_a_clear_picture_of_the_crop), fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            buttonContent()
        }
    }

}