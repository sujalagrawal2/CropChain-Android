package com.hexagraph.cropchain.ui.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.ui.component.OnboardingTitleSubtitle
import kotlinx.coroutines.delay

@Composable
fun VerificationRequestSentScreen() {
    var imageVisible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay(500)
        imageVisible = true
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        OnboardingTitleSubtitle(
            largeText = stringResource(R.string.verification_request_sent),
            smallText = stringResource(R.string.verification_subtitile),
            modifier = Modifier.width(300.dp)
        )
        Spacer(modifier = Modifier.height(36.dp))
        AnimatedVisibility(
            visible = imageVisible,
            enter = fadeIn() + scaleIn(), exit = fadeOut() + scaleOut()
        ) {
            Image(
                painter = painterResource(id = R.drawable.verificationcenter),
                modifier = Modifier.width(280.dp),
                contentScale = ContentScale.FillWidth,
                contentDescription = null
            )
        }
        if (!imageVisible)
            Spacer(modifier = Modifier.height(300.dp))
    }

}