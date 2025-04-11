package com.hexagraph.cropchain.ui.screens.onboarding

import com.hexagraph.cropchain.domain.model.SupportedLanguages

data class OnboardingUIState(
    val allowedScreens: List<OnboardingScreens> = OnboardingScreens.entries,
    val currentScreenIndex: Int = 0,
    val selectedLanguageQuery: SupportedLanguages = SupportedLanguages.ENGLISH,
    val aadhaarQuery: String = "",
    val nameQuery: String = "",
    val isUserFarmerQuery: Boolean = true,
    val password: String = "",

    val currentAppLanguage: SupportedLanguages = SupportedLanguages.ENGLISH,
    val storedAadharId: String = "",
    val isUserFarmer: Boolean = true,

    //Lambdas
    val onCompletion: ()->Unit = {}
){
    val isPasswordValid: Boolean
        get() {
            if(password.isEmpty()) return true
            if(password.length < 6){
                return false
            }
            return false
        }

    val isAadhaarValid: Boolean
        get() {
            if(aadhaarQuery.isEmpty()) return true
            return aadhaarQuery.length == 12 && aadhaarQuery.all { it.isDigit() }
        }

    val currentScreenName: OnboardingScreens
        get() = allowedScreens.getOrNull(currentScreenIndex) ?: OnboardingScreens.LANGUAGE_SELECTION
}
