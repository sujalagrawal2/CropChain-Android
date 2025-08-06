package com.hexagraph.cropchain.ui.screens.onboarding

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
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
    iteration: Long,
    onCompletion: () -> Unit
) {
    val uiState by onboardingViewModel.uiState.collectAsState()
    val error by onboardingViewModel.errorFlow.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(allowedScreens, iteration) {
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

    LaunchedEffect(iteration) {
        onboardingViewModel.areAllPermissionsAllowedFlow.collectLatest {
            onboardingViewModel.checkMissingPermissions(context)
        }
    }

    // Create the navigation back stack for onboarding flow
    val backStack = rememberNavBackStack(
        getInitialRoute(allowedScreens)
    )

    // Handle back button
    BackHandler(
        enabled = backStack.size > 1
    ) {
        onboardingViewModel.moveBackwardWithinOnboarding()
        if (backStack.size > 1) {
            backStack.removeAt(backStack.size - 1)
        }
    }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<OnboardingNavigation.LanguageSelection> {
                OnboardingScreenContainer(
                    isFullScreen = false,
                    onNext = {
                        onboardingViewModel.nextButtonAction(context)
                        navigateNext(backStack, uiState.currentScreenName, allowedScreens)
                    }
                ) {
                    OnboardingTitleSubtitle(
                        largeText = stringResource(R.string.choose_your_language_screen_title),
                        smallText = stringResource(R.string.choose_your_language_screen_subtitle)
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
            }

            entry<OnboardingNavigation.IntroScreen1> {
                OnboardingScreenContainer(
                    isFullScreen = true,
                    onNext = {
                        onboardingViewModel.nextButtonAction(context)
                        navigateNext(backStack, uiState.currentScreenName, allowedScreens)
                    }
                ) {
                    IntroScreen(Modifier, OnboardingScreens.INTRODUCTION_SCREEN_1)
                }
            }

            entry<OnboardingNavigation.IntroScreen2> {
                OnboardingScreenContainer(
                    isFullScreen = true,
                    onNext = {
                        onboardingViewModel.nextButtonAction(context)
                        navigateNext(backStack, uiState.currentScreenName, allowedScreens)
                    }
                ) {
                    IntroScreen(Modifier, OnboardingScreens.INTRODUCTION_SCREEN_2)
                }
            }

            entry<OnboardingNavigation.IntroScreen3> {
                OnboardingScreenContainer(
                    isFullScreen = true,
                    onNext = {
                        onboardingViewModel.nextButtonAction(context)
                        navigateNext(backStack, uiState.currentScreenName, allowedScreens)
                    }
                ) {
                    IntroScreen(Modifier, OnboardingScreens.INTRODUCTION_SCREEN_3)
                }
            }

            entry<OnboardingNavigation.RoleSelection> {
                OnboardingScreenContainer(
                    isFullScreen = false,
                    onNext = {
                        onboardingViewModel.nextButtonAction(context)
                        navigateNext(backStack, uiState.currentScreenName, allowedScreens)
                    }
                ) {
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
            }

            entry<OnboardingNavigation.PermissionsScreen> {
                OnboardingScreenContainer(
                    isFullScreen = false,
                    showNextButton = false,
                    onNext = {
                        onboardingViewModel.nextButtonAction(context)
                        navigateNext(backStack, uiState.currentScreenName, allowedScreens)
                    }
                ) {
                    PermissionScreen(
                        modifier = Modifier,
                        viewModel = onboardingViewModel,
                    )
                }
            }

            entry<OnboardingNavigation.AadharInput> {
                OnboardingScreenContainer(
                    isFullScreen = false,
                    onNext = {
                        onboardingViewModel.nextButtonAction(context)
                        navigateNext(backStack, uiState.currentScreenName, allowedScreens)
                    }
                ) {
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
                            navigateNext(backStack, uiState.currentScreenName, allowedScreens)
                        }
                    )
                }
            }

            entry<OnboardingNavigation.VerificationRequestSent> {
                OnboardingScreenContainer(
                    isFullScreen = false,
                    onNext = {
                        onboardingViewModel.nextButtonAction(context)
                        if (uiState.currentScreenIndex >= uiState.allowedScreens.lastIndex) {
                            onCompletion()
                        } else {
                            navigateNext(backStack, uiState.currentScreenName, allowedScreens)
                        }
                    }
                ) {
                    VerificationRequestSentScreen()
                }
            }
        }
    )
}

@Composable
private fun OnboardingScreenContainer(
    isFullScreen: Boolean,
    showNextButton: Boolean = true,
    onNext: () -> Unit,
    content: @Composable () -> Unit
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (!isFullScreen) Modifier
                    .background(brush = cropChainGradient)
                else Modifier.background(MaterialTheme.colorScheme.surface)
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
                            Modifier
                        else
                            Modifier
                                .fillMaxHeight(0.77f)
                                .clip(shape = RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp))
                    )
                    .background(MaterialTheme.colorScheme.surface)
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
                        content()
                    }

                    if (showNextButton)
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            AppButton(
                                text = stringResource(R.string.button_text_onboarding),
                                isEnabled = true,
                                modifier = Modifier.fillMaxWidth(0.85f)
                            ) {
                                onNext()
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

private fun getInitialRoute(allowedScreens: List<OnboardingScreens>): OnboardingNavigation {
    return when (allowedScreens.firstOrNull()) {
        OnboardingScreens.LANGUAGE_SELECTION -> OnboardingNavigation.LanguageSelection
        OnboardingScreens.INTRODUCTION_SCREEN_1 -> OnboardingNavigation.IntroScreen1
        OnboardingScreens.INTRODUCTION_SCREEN_2 -> OnboardingNavigation.IntroScreen2
        OnboardingScreens.INTRODUCTION_SCREEN_3 -> OnboardingNavigation.IntroScreen3
        OnboardingScreens.ROLE_SELECTION -> OnboardingNavigation.RoleSelection
        OnboardingScreens.PERMISSIONS_SCREEN -> OnboardingNavigation.PermissionsScreen
        OnboardingScreens.AADHAR_INPUT -> OnboardingNavigation.AadharInput
        OnboardingScreens.VERIFICATION_REQUEST_SENT -> OnboardingNavigation.VerificationRequestSent
        else -> OnboardingNavigation.LanguageSelection
    }
}

private fun navigateNext(
    backStack: androidx.navigation3.runtime.NavBackStack,
    currentScreen: OnboardingScreens,
    allowedScreens: List<OnboardingScreens>
) {
    val currentIndex = allowedScreens.indexOf(currentScreen)
    if (currentIndex < allowedScreens.size - 1) {
        val nextScreen = allowedScreens[currentIndex + 1]
        val nextRoute = when (nextScreen) {
            OnboardingScreens.LANGUAGE_SELECTION -> OnboardingNavigation.LanguageSelection
            OnboardingScreens.INTRODUCTION_SCREEN_1 -> OnboardingNavigation.IntroScreen1
            OnboardingScreens.INTRODUCTION_SCREEN_2 -> OnboardingNavigation.IntroScreen2
            OnboardingScreens.INTRODUCTION_SCREEN_3 -> OnboardingNavigation.IntroScreen3
            OnboardingScreens.ROLE_SELECTION -> OnboardingNavigation.RoleSelection
            OnboardingScreens.PERMISSIONS_SCREEN -> OnboardingNavigation.PermissionsScreen
            OnboardingScreens.AADHAR_INPUT -> OnboardingNavigation.AadharInput
            OnboardingScreens.VERIFICATION_REQUEST_SENT -> OnboardingNavigation.VerificationRequestSent
        }
        backStack.add(nextRoute)
    }
}
