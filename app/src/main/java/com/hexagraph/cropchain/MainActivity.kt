package com.hexagraph.cropchain

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hexagraph.cropchain.services.SharedPreferencesHelper
import com.hexagraph.cropchain.ui.navigation.AppNavHost
import com.hexagraph.cropchain.ui.screens.authentication.AuthenticationNavigation
import com.hexagraph.cropchain.ui.screens.authentication.login.LoginScreen
import com.hexagraph.cropchain.ui.screens.authentication.onboarding.OnBoardingScreen
import com.hexagraph.cropchain.ui.theme.CropChainTheme
import dagger.hilt.android.AndroidEntryPoint
import io.metamask.androidsdk.CommunicationClientModule
import io.metamask.androidsdk.DappMetadata
import io.metamask.androidsdk.Ethereum
import io.metamask.androidsdk.Logger
import io.metamask.androidsdk.ReadOnlyRPCProvider
import io.metamask.androidsdk.SDKOptions


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var isUserLoggedIn = false
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

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
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
        isUserLoggedIn = SharedPreferencesHelper.isLoggedIn(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CropChainTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination =
                        if (!isUserLoggedIn) AuthenticationNavigation.OnBoarding
                        else AuthenticationNavigation.MainApp
                    ) {

                        composable<AuthenticationNavigation.OnBoarding> {
                            OnBoardingScreen(
                                navController,
                                Modifier.padding(innerPadding)
                            )
                        }

                        composable<AuthenticationNavigation.LoginScreen> {
                            LoginScreen(
                                navController,
                                Modifier.padding(innerPadding)
                            )
                        }

                        composable<AuthenticationNavigation.MainApp> {
                            AppNavHost()
                        }

                    }
                }
            }
        }
    }
}

