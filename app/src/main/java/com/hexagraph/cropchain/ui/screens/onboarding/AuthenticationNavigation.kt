package com.hexagraph.cropchain.ui.screens.onboarding

import kotlinx.serialization.Serializable

sealed interface AuthenticationNavigation {

    @Serializable
    data class OnBoarding(
        val allowedScreens: List<OnboardingScreens> = OnboardingScreens.entries
    ) : AuthenticationNavigation

    @Serializable
    data object MainApp: AuthenticationNavigation
}

