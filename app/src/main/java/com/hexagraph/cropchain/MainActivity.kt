package com.hexagraph.cropchain

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferencesImpl
import com.hexagraph.cropchain.ui.navigation.farmer.MainScreen
import com.hexagraph.cropchain.ui.screens.onboarding.AuthenticationNavigation
import com.hexagraph.cropchain.ui.screens.onboarding.OnBoardingScreen
import com.hexagraph.cropchain.ui.screens.onboarding.OnboardingScreens
import com.hexagraph.cropchain.ui.screens.onboarding.OnboardingViewModel
import com.hexagraph.cropchain.ui.screens.onboarding.PermissionsRequired
import com.hexagraph.cropchain.ui.theme.CropChainTheme
import com.hexagraph.cropchain.util.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var fcmImageId: String? = null
    private var fcmImageType: String? = null

    override fun attachBaseContext(newBase: Context) {


        // Initialize locale before setting the base context
        val langCode = runBlocking { AppPreferencesImpl(newBase).appLanguage.getFlow().first() }
        Log.d("MainActivityLang", "Language code: ${langCode.languageCode}")
        val context = LocaleHelper.setLocale(newBase, langCode.languageCode)
        super.attachBaseContext(context)
    }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appPreferences: AppPreferences = AppPreferencesImpl(this)

        val areAllPermissionsGrantedInitial =
            runBlocking { appPreferences.areAllPermissionsGranted.getFlow().first() }
        val aadharIdInitial = runBlocking { appPreferences.aadharID.getFlow().first() }

        val isUserLoggedInInitial = runBlocking { appPreferences.isUserLoggedIn.getFlow().first() }

        handleIntent(intent)
        checkPermissions()
        enableEdgeToEdge()
        setContent {
            CropChainTheme {
                val areAllPermissionsGranted by appPreferences.areAllPermissionsGranted.getFlow()
                    .collectAsState(areAllPermissionsGrantedInitial)
                val backstack = rememberNavBackStack(
                    if (isUserLoggedInInitial && aadharIdInitial.isNotEmpty()) AuthenticationNavigation.MainApp
                    else AuthenticationNavigation.OnBoarding()
                )

                Surface(modifier = Modifier.fillMaxSize()) {
                    val onboardingViewModel = hiltViewModel<OnboardingViewModel>()

                    NavDisplay(
                        backStack = backstack,
                        entryProvider = entryProvider {
                            entry<AuthenticationNavigation.OnBoarding> {
                                var allowedScreens = it.allowedScreens
                                if (areAllPermissionsGranted) allowedScreens =
                                    allowedScreens.filter { it != OnboardingScreens.PERMISSIONS_SCREEN }
                                if (aadharIdInitial.isNotEmpty()) allowedScreens =
                                    allowedScreens.filter { it != OnboardingScreens.AADHAR_INPUT }
                                OnBoardingScreen(
                                    onboardingViewModel = onboardingViewModel,
                                    iteration = it.timestamp,
                                    allowedScreens = allowedScreens,
                                    onCompletion = {
                                        runBlocking {
                                            appPreferences.isUserLoggedIn.set(true)
                                        }
                                        backstack.add(AuthenticationNavigation.MainApp)
                                        backstack.removeIf { screen -> screen is AuthenticationNavigation.OnBoarding }
                                    }
                                )
                            }

                            entry<AuthenticationNavigation.MainApp> {
                                var isCurrentUserFarmer = false

                                runBlocking {
                                    isCurrentUserFarmer = appPreferences.isCurrentUserFarmer.get()
                                }

                                if (isCurrentUserFarmer) MainScreen(fcmImageId, fcmImageType) {
                                    Log.d("MainActivity", "Navigating to Onboarding screen")
                                    backstack.add(AuthenticationNavigation.OnBoarding())
                                    backstack.removeIf { it is AuthenticationNavigation.MainApp }
                                }
                                else com.hexagraph.cropchain.ui.navigation.scientist.MainScreen(fcmImageId, fcmImageType)
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {

        val type = intent.getStringExtra("type")
        val imageId = intent.getStringExtra("imageId")
        val imageType = intent.getStringExtra("imageType")

        if (type != null && type == "CropChain-FCM" && imageId != null && imageType != null) {
            fcmImageId = imageId
            fcmImageType = imageType
            Log.d("MainActivity", "Notification Clicked with ID: $imageId and Type: $imageType")

        }
    }

    fun checkPermissions() {
        val appPreferences: AppPreferences = AppPreferencesImpl(this)
        var granted = true
        PermissionsRequired.entries.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission.permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                granted = false
            }
        }
        CoroutineScope(Dispatchers.Default).launch {
            appPreferences.areAllPermissionsGranted.set(granted)
        }
    }
}




