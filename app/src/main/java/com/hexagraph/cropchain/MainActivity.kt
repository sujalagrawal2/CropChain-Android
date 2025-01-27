package com.hexagraph.cropchain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hexagraph.cropchain.ui.navigation.AppNavHost
import com.hexagraph.cropchain.ui.screens.authentication.AuthenticationNavigation
import com.hexagraph.cropchain.ui.screens.authentication.login.LoginScreen
import com.hexagraph.cropchain.ui.screens.authentication.onboarding.OnBoardingScreen
import com.hexagraph.cropchain.ui.theme.CropChainTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CropChainTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = AuthenticationNavigation.OnBoarding
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

