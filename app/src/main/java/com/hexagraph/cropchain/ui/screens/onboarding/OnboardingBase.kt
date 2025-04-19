package com.hexagraph.cropchain.ui.screens.onboarding

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.ui.component.AppButton
import com.hexagraph.cropchain.ui.component.LanguageSelector
import com.hexagraph.cropchain.ui.component.OnboardingTitleSubtitle
import com.hexagraph.cropchain.ui.screens.onboarding.introscreens.IntroScreen
import com.hexagraph.cropchain.ui.screens.onboarding.login.LoginScreen
import com.hexagraph.cropchain.ui.theme.cropChainGradient
import kotlinx.coroutines.flow.collectLatest


@Composable
fun OnBoardingScreen(
    onboardingViewModel: OnboardingViewModel,
    allowedScreens: List<OnboardingScreens>,
    onCompletion: () -> Unit
) {
    val uiState by onboardingViewModel.uiState.collectAsState()
    val error by onboardingViewModel.errorFlow.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    LaunchedEffect(allowedScreens) {
        onboardingViewModel.initializeScreen(allowedScreens, onCompletion)
        onboardingViewModel.checkMissingPermissions(context)
    }
    LaunchedEffect(error) {
        if (error != null) {
            snackbarHostState.showSnackbar(context.getString(error!!))
            Log.d("OnboardingError", context.getString(error!!))
            onboardingViewModel.clearErrorFlow()
        }
    }
    LaunchedEffect(Unit) {
        onboardingViewModel.areAllPermissionsAllowedFlow.collectLatest {
            onboardingViewModel.checkMissingPermissions(context)
        }
    }
    val isFullScreen = uiState.currentScreenName == OnboardingScreens.INTRODUCTION_SCREEN_1 ||
            uiState.currentScreenName == OnboardingScreens.INTRODUCTION_SCREEN_2 ||
            uiState.currentScreenName == OnboardingScreens.INTRODUCTION_SCREEN_3

    BackHandler(
        enabled = uiState.currentScreenIndex != 0
    ) {
        onboardingViewModel.moveBackwardWithinOnboarding()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (!isFullScreen) Modifier
                    .background(brush = cropChainGradient) else Modifier.background(MaterialTheme.colorScheme.surface)
            ),
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            if (!isFullScreen)
                Logo(
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .align(Alignment.TopCenter)
                )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth()
                    .then(
                        if (isFullScreen)
                            Modifier else Modifier
                            .fillMaxHeight(0.77f)
                            .clip(shape = RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp))
                    )
                    .background(MaterialTheme.colorScheme.surface)
                    .animateContentSize(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            visibilityThreshold = IntSize.VisibilityThreshold
                        )
                    )
                    .padding(it.calculateBottomPadding())
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .weight(10f)
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .widthIn(min = 300.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(16.dp))
                        AnimatedContent(
                            targetState = uiState.currentScreenName,
                            label = "",
                            transitionSpec = {
                                slideIntoContainer(
                                    animationSpec = tween(200, easing = EaseIn),
                                    towards = if (targetState.screenNo >= initialState.screenNo) AnimatedContentTransitionScope.SlideDirection.Left else AnimatedContentTransitionScope.SlideDirection.Right
                                ).togetherWith(
                                    slideOutOfContainer(
                                        animationSpec = tween(200, easing = EaseOut),
                                        towards = if (targetState.screenNo >= initialState.screenNo) AnimatedContentTransitionScope.SlideDirection.Left else AnimatedContentTransitionScope.SlideDirection.Right
                                    )
                                )
                            }
                        ) { targetState ->
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                when (targetState) {
                                    OnboardingScreens.LANGUAGE_SELECTION -> {
                                        OnboardingTitleSubtitle(
                                            largeText = stringResource(R.string.choose_your_language_screen_title),
                                            smallText = stringResource(
                                                R.string.choose_your_language_screen_subtitle
                                            )
                                        )
                                        Spacer(Modifier.height(24.dp))
                                        LanguageSelector(
                                            modifier = Modifier.height(400.dp),
                                            selectedLanguage = uiState.selectedLanguageQuery,
                                            onChangeSelectedLanguage = {
                                                onboardingViewModel.onLanguageSelected(it)
                                            }
                                        )
                                    }

                                    OnboardingScreens.INTRODUCTION_SCREEN_1,
                                    OnboardingScreens.INTRODUCTION_SCREEN_2,
                                    OnboardingScreens.INTRODUCTION_SCREEN_3 -> {
                                        IntroScreen(Modifier, targetState)
                                    }

                                    OnboardingScreens.ROLE_SELECTION -> {
                                        OnboardingTitleSubtitle(
                                            largeText = stringResource(R.string.role_selection_title),
                                            smallText = stringResource(R.string.role_selection_subtitle),
                                        )
                                        Spacer(Modifier.height(60.dp))
                                        RoleSelector(
                                            isUserFarmer = uiState.isUserFarmerQuery,
                                            onChange = {
                                                onboardingViewModel.onIsUserFarmerSelected(it)
                                            }
                                        )
                                    }

                                    OnboardingScreens.PERMISSIONS_SCREEN -> {
                                        PermissionScreen(
                                            modifier = Modifier,
                                            onboardingViewModel
                                        )
                                    }

                                    OnboardingScreens.AADHAR_INPUT -> {
                                        LoginScreen(
                                            modifier = Modifier,
                                            uiState = uiState,
                                            onAadharChange = {
                                                onboardingViewModel.onLoginQueryChange(aadhaar = it)
                                            },
                                            onPasswordChange = {
                                                onboardingViewModel.onLoginQueryChange(password = it)
                                            },
                                            onNameChange = {
                                                onboardingViewModel.onLoginQueryChange(name = it)
                                            },
                                            onDone = {
                                                onboardingViewModel.nextButtonAction(context = context)
                                            }
                                        )
                                    }

                                    OnboardingScreens.VERIFICATION_REQUEST_SENT -> {
                                        VerificationRequestSentScreen()
                                    }
                                }
                            }
                        }
                    }
                    if (uiState.currentScreenName != OnboardingScreens.PERMISSIONS_SCREEN)
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            AppButton(
                                text = stringResource(R.string.button_text_onboarding),
                                isEnabled = true,
                                modifier = Modifier.fillMaxWidth(0.85f)
                            ) {
                                onboardingViewModel.nextButtonAction(context = context)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                }
            }

        }

    }
}


@Composable
private fun Logo(modifier: Modifier, size: Int = 100) {
    Image(
        painter = painterResource(R.drawable.leaf),
        contentDescription = null,
        modifier = modifier
            .padding(1.dp)
            .size(size.dp)
    )
}