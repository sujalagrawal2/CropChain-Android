package com.hexagraph.cropchain.farmer.ui.screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hexagraph.cropchain.farmer.ui.navigation.NavRoutes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val showBottomBar =
        (currentRoute == NavRoutes.HomeScreen.route || currentRoute == NavRoutes.ProfileScreen.route || currentRoute == NavRoutes.SelectImageScreen.route)


    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.HomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                NavRoutes.HomeScreen.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(400))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeOut(animationSpec = tween(300))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    )
                }
            ) {
                HomeScreen(
                    onUploadedImagesClick = {
                        navController.navigate(NavRoutes.UploadedImageScreen.route)
                    },
                    onUploadClick = {
                        navController.navigate(NavRoutes.UploadIImageScreen.route)
                    })
            }
            composable(
                NavRoutes.ProfileScreen.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(400))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeOut(animationSpec = tween(300))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    )
                }
            ) {
                ProfileScreen()
            }
            composable(
                NavRoutes.SelectImageScreen.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(400))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeOut(animationSpec = tween(300))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    )
                }
            ) {
                SelectImageScreen(
                    goToUploadStatusScreen = {
                        navController.navigate(NavRoutes.UploadStatusScreen.route) {
                            popUpTo(NavRoutes.SelectImageScreen.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    goToUploadImageScreen = {
                        navController.navigate(NavRoutes.UploadIImageScreen.route)
                    })
            }
            composable(NavRoutes.UploadStatusScreen.route) {
                UploadStatusScreen(onBackButtonPressed = {
                    navController.navigateUp()
                })
            }
            composable(NavRoutes.UploadIImageScreen.route) {
                UploadImageScreen(
                    onBackButtonPressed = {
                        navController.navigateUp()
                    },
                    goToUploadStatusScreen = {
                        navController.navigate(NavRoutes.UploadStatusScreen.route) {
                            popUpTo(NavRoutes.SelectImageScreen.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    })
            }

            composable(NavRoutes.UploadedImageScreen.route) {
                UploadedImageScreen(
                    onBackButtonPressed = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = currentRoute == NavRoutes.HomeScreen.route,
            onClick = {
                if (currentRoute != NavRoutes.HomeScreen.route) {
                    navController.navigate(NavRoutes.HomeScreen.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.AddCircle, contentDescription = "Upload") },
            label = { Text("Upload") },
            selected = currentRoute == NavRoutes.SelectImageScreen.route,
            onClick = {
                if (currentRoute != NavRoutes.SelectImageScreen.route) {
                    navController.navigate(NavRoutes.SelectImageScreen.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == NavRoutes.ProfileScreen.route,
            onClick = {
                if (currentRoute != NavRoutes.ProfileScreen.route) {
                    navController.navigate(NavRoutes.ProfileScreen.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}



