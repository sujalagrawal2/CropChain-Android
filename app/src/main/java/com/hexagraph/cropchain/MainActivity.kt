package com.hexagraph.cropchain

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hexagraph.cropchain.domain.repository.apppreferences.AppPreferences
import com.hexagraph.cropchain.domain.repository.apppreferences.AppPreferencesImpl
import com.hexagraph.cropchain.ui.navigation.AppNavHost
import com.hexagraph.cropchain.ui.screens.onboarding.AuthenticationNavigation
import com.hexagraph.cropchain.ui.screens.onboarding.login.LoginScreen
import com.hexagraph.cropchain.ui.screens.onboarding.OnBoardingScreen
import com.hexagraph.cropchain.ui.screens.onboarding.OnboardingViewModel
import com.hexagraph.cropchain.ui.theme.CropChainTheme
import com.hexagraph.cropchain.util.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.navigation.toRoute
import com.hexagraph.cropchain.farmer.ui.screen.MainScreen
import com.hexagraph.cropchain.ui.screens.onboarding.OnboardingScreens
import com.hexagraph.cropchain.ui.screens.onboarding.PermissionsRequired
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private lateinit var ethereum: Ethereum
//    private val dappMetadata = DappMetadata(
//        name = "Crop Chain",
//        url = "https://cropchain.com"
//    )
//    private val sdkOptions = SDKOptions(
//        infuraAPIKey = "0xdo7Ieek_okE7Do3XTfAHaZyh-9D81Z",
//        readonlyRPCMap = mapOf("0xaa36a7" to "https://eth-sepolia.g.alchemy.com/v2/0xdo7Ieek_okE7Do3XTfAHaZyh-9D81Z")
//    )
//    private val logger = object : Logger {
//
//
//        override fun error(message: String) {
//
//        }
//
//        override fun log(message: String) {
//            println("Log in logger Metamask , Message : $message")
//        }
//    }
//    private val communicationClientModule = CommunicationClientModule(
//        context = this
//    )
//    private val readOnlyRPCProvider = ReadOnlyRPCProvider(
//        infuraAPIKey = "0xdo7Ieek_okE7Do3XTfAHaZyh-9D81Z",
//        readonlyRPCMap = mapOf("0xaa36a7" to "https://eth-sepolia.g.alchemy.com/v2/0xdo7Ieek_okE7Do3XTfAHaZyh-9D81Z"),
//        logger = logger
//    )

    override fun attachBaseContext(newBase: Context) {
        // Initialize locale before setting the base context
        val langCode = runBlocking { AppPreferencesImpl(newBase).appLanguage.getFlow().first() }
        Log.d("MainActivityLang", "Language code: ${langCode.languageCode}")
        val context = LocaleHelper.setLocale(newBase, langCode.languageCode)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
//                0
//            )
//        ethereum = Ethereum(
//            this,
//            dappMetadata = dappMetadata,
//            sdkOptions = sdkOptions,
//            logger = logger,
//            communicationClientModule = communicationClientModule,
//            readOnlyRPCProvider = readOnlyRPCProvider,
//        )
//        ethereum.connect {
//            println("Selected Address: " + ethereum.selectedAddress )
//
//        }
        super.onCreate(savedInstanceState)
        var appPreferences: AppPreferences = AppPreferencesImpl(this)
        // Preload values synchronously
        val areAllPermissionsGrantedInitial = runBlocking { appPreferences.areAllPermissionsGranted.getFlow().first() }
        val aadharIdInitial = runBlocking { appPreferences.aadharID.getFlow().first() }

        checkPermissions()
        enableEdgeToEdge()
        setContent {
            CropChainTheme {
                val navController = rememberNavController()
                val areAllPermissionsGranted by appPreferences.areAllPermissionsGranted.getFlow().collectAsState(areAllPermissionsGrantedInitial)
                val aadharId by appPreferences.aadharID.getFlow().collectAsState(aadharIdInitial)
                Surface(modifier = Modifier.fillMaxSize()) {
                    val onboardingViewModel = hiltViewModel<OnboardingViewModel>()

                        NavHost(
                            navController = navController,
                            startDestination =
                                if(aadharId.isEmpty() && !areAllPermissionsGranted){
                                AuthenticationNavigation.OnBoarding(OnboardingScreens.entries)
                            }else if(aadharId.isEmpty()){
                                AuthenticationNavigation.OnBoarding(OnboardingScreens.entries - OnboardingScreens.PERMISSIONS_SCREEN)
                            }else if(!areAllPermissionsGranted) {
                                AuthenticationNavigation.OnBoarding(listOf(OnboardingScreens.PERMISSIONS_SCREEN))
                            }else{
                                AuthenticationNavigation.MainApp
                            }
                        ) {

                            composable<AuthenticationNavigation.OnBoarding> {
                                val allowedScreens = it.toRoute<AuthenticationNavigation.OnBoarding>().allowedScreens
                                OnBoardingScreen(
                                    onboardingViewModel = onboardingViewModel,
                                    allowedScreens = allowedScreens,
                                    onCompletion = {
                                        navController.navigate(AuthenticationNavigation.MainApp){
                                            popUpTo(AuthenticationNavigation.OnBoarding()){inclusive = true}
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                            composable<AuthenticationNavigation.MainApp> {
                                MainScreen()
                            }

                        }
                    }
                }
            }
        }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    fun checkPermissions(){
        var appPreferences: AppPreferences = AppPreferencesImpl(this)
        var granted = true
        PermissionsRequired.entries.forEach { permission->
            if(ContextCompat.checkSelfPermission(this, permission.permission) != PackageManager.PERMISSION_GRANTED){
                granted = false
            }
        }
        CoroutineScope(Dispatchers.Default).launch {
            appPreferences.areAllPermissionsGranted.set(granted)
        }
    }
}

