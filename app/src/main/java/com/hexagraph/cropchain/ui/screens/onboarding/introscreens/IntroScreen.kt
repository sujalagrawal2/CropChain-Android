package com.hexagraph.cropchain.ui.screens.onboarding.introscreens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.ui.screens.onboarding.OnboardingScreens
import com.hexagraph.cropchain.ui.theme.bodyFontFamily
import com.hexagraph.cropchain.ui.theme.displayFontFamily

@Composable
fun IntroScreen(modifier: Modifier, screen: OnboardingScreens) {
    val headingText = when(screen) {
        OnboardingScreens.INTRODUCTION_SCREEN_1  ->  stringResource(R.string.intro_screen_1_heading)
        OnboardingScreens.INTRODUCTION_SCREEN_2  -> stringResource(R.string.intro_screen_2_title)
        OnboardingScreens.INTRODUCTION_SCREEN_3  ->  stringResource(R.string.intro_screen_3_title)
        else -> throw IllegalArgumentException("Invalid screen number")
    }
    val bodyText = when(screen) {
        OnboardingScreens.INTRODUCTION_SCREEN_1  ->  stringResource(R.string.intro_screen_1_body)
        OnboardingScreens.INTRODUCTION_SCREEN_2  ->  stringResource(R.string.intro_screen_2_body)
        OnboardingScreens.INTRODUCTION_SCREEN_3  ->  stringResource(R.string.intro_screen_3_body)
        else -> throw IllegalArgumentException("Invalid screen number")
    }
    val dots = when(screen) {
        OnboardingScreens.INTRODUCTION_SCREEN_1  ->  R.drawable.dots1
        OnboardingScreens.INTRODUCTION_SCREEN_2  ->  R.drawable.dots2
        OnboardingScreens.INTRODUCTION_SCREEN_3  ->  R.drawable.dots3
        else -> throw IllegalArgumentException("Invalid screen number")
    }
    val screenSize = getScreenHeightInDp()
    Box(modifier = modifier.fillMaxWidth()
        .height(screenSize.dp-200.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when(screen) {
                OnboardingScreens.INTRODUCTION_SCREEN_1 ->  Screen1Illustration(Modifier.height(200.dp).fillMaxWidth())
                OnboardingScreens.INTRODUCTION_SCREEN_2  ->   Screen2Illustration(Modifier.height(200.dp).width(300.dp))
                OnboardingScreens.INTRODUCTION_SCREEN_3  ->   Screen3Illustration(Modifier.height(200.dp).width(300.dp))
                else -> throw IllegalArgumentException("Invalid screen number")
            }
            Image(
                painter = painterResource(id = dots),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = headingText,
                style = TextStyle(
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    fontFamily = displayFontFamily,
                    fontWeight = FontWeight(600)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = bodyText,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 21.sp,
                    fontFamily = bodyFontFamily,
                    fontWeight = FontWeight(400),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.width(330.dp)
            )
        }
    }
}


@Composable
private fun Screen1Illustration(modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(top = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.onboarding_1),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun Screen2Illustration(modifier: Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.onboarding_2),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun Screen3Illustration(modifier: Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.onboarding_2),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
fun getScreenHeightInDp(): Int {
    val configuration = LocalConfiguration.current
    return configuration.screenHeightDp
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun IntroScreenPreview() {
    Surface {
        IntroScreen(
            modifier = Modifier
                .wrapContentHeight(),
            screen = OnboardingScreens.INTRODUCTION_SCREEN_1
        )
    }
}