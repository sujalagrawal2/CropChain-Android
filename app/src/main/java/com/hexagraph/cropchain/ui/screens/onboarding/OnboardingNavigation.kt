package com.hexagraph.cropchain.ui.screens.onboarding

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface OnboardingNavigation: NavKey {
    @Serializable
    data object LanguageSelection : OnboardingNavigation

    @Serializable
    data object IntroScreen1 : OnboardingNavigation

    @Serializable
    data object IntroScreen2 : OnboardingNavigation

    @Serializable
    data object IntroScreen3 : OnboardingNavigation

    @Serializable
    data object RoleSelection : OnboardingNavigation

    @Serializable
    data object PermissionsScreen : OnboardingNavigation

    @Serializable
    data object AadharInput : OnboardingNavigation

    @Serializable
    data object VerificationRequestSent : OnboardingNavigation
}
