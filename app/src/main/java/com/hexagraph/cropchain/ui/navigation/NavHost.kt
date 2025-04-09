package com.hexagraph.cropchain.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hexagraph.cropchain.ui.screens.dashboard.DashboardScreen
import com.hexagraph.cropchain.ui.screens.profile.ProfileScreen
import com.hexagraph.cropchain.ui.screens.scientist.FarmerScreen
import com.hexagraph.cropchain.ui.screens.scientist.HomeScreen
import com.hexagraph.cropchain.ui.screens.upload.UploadScreen
import com.hexagraph.cropchain.ui.screens.verifier.VerifierScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {

            val selectedItem = remember { mutableIntStateOf(0) }

            NavigationBar {

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Dashboard") },
                    selected = selectedItem.intValue == 0,
                    onClick = {
                        selectedItem.intValue = 0
                        navController.navigate(NavRoutes.DashboardScreen)
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = null) },
                    label = { Text("Upload") },
                    selected = selectedItem.intValue == 1,
                    onClick = {
                        selectedItem.intValue = 1
                        navController.navigate(NavRoutes.UploadScreen)
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                    label = { Text("Profile") },
                    selected = selectedItem.intValue == 2,
                    onClick = {
                        selectedItem.intValue = 2
                        navController.navigate(NavRoutes.ProfileScreen)
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Science, contentDescription = null) },
                    label = { Text("Scientist") },
                    selected = selectedItem.intValue == 3,
                    onClick = {
                        selectedItem.intValue = 3
                        navController.navigate(NavRoutes.ScientistScreen)
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.VerifiedUser, contentDescription = null) },
                    label = { Text("Verifiers") },
                    selected = selectedItem.intValue == 4,
                    onClick = {
                        selectedItem.intValue = 4
                        navController.navigate(NavRoutes.VerifierScreen)
                    }
                )

            }
        },
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = NavRoutes.DashboardScreen) {
            composable<NavRoutes.DashboardScreen> {
                DashboardScreen(modifier = Modifier.padding(innerPadding))
            }
            composable<NavRoutes.ProfileScreen> {
                ProfileScreen(Modifier.padding(innerPadding))
            }

            composable<NavRoutes.UploadScreen> {
                UploadScreen(Modifier.padding(innerPadding))
            }
            composable<NavRoutes.ScientistScreen> {
                HomeScreen(
                    Modifier.padding(innerPadding),
                    onFarmerSelect = {
                        navController.navigate(NavRoutes.FarmerScreen(it))
                    })
            }
            composable<NavRoutes.FarmerScreen> { backStackEntry ->
                val address = backStackEntry.arguments?.getString("address")
                FarmerScreen(address!!, modifier = Modifier.padding(innerPadding))
            }
            composable<NavRoutes.VerifierScreen> {
                VerifierScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}