package com.hexagraph.cropchain.farmer.ui.screen

import android.app.Activity
import android.view.View
import android.view.WindowManager
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.farmer.ui.navigation.NavRoutes
import com.hexagraph.cropchain.ui.theme.cropChainGradient


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
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.HomeScreen.route,
            modifier = Modifier.then(
                if(navController.currentDestination?.route == NavRoutes.ProfileScreen.route) Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                else Modifier.padding(innerPadding)
            )
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
    val color = NavigationBarItemDefaults.colors().copy(
        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer, // Green for selected icon
        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer, // Green for selected text
        selectedIndicatorColor = MaterialTheme.colorScheme.primaryContainer, // Same green for selected indicator
    )

    NavigationBar() {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text(stringResource(R.string.dashboard), fontWeight = FontWeight.Bold) },
            selected = currentRoute == NavRoutes.HomeScreen.route,
            onClick = {
                if (currentRoute != NavRoutes.HomeScreen.route) {
                    navController.navigate(NavRoutes.HomeScreen.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            colors = color
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.AddCircle, contentDescription = "Upload") },
            label = { Text(stringResource(R.string.upload), fontWeight = FontWeight.Bold) },
            selected = currentRoute == NavRoutes.SelectImageScreen.route,
            onClick = {
                if (currentRoute != NavRoutes.SelectImageScreen.route) {
                    navController.navigate(NavRoutes.SelectImageScreen.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            colors = color
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
            label = { Text(stringResource(R.string.profile), fontWeight = FontWeight.Bold) },
            selected = currentRoute == NavRoutes.ProfileScreen.route,
            onClick = {
                if (currentRoute != NavRoutes.ProfileScreen.route) {
                    navController.navigate(NavRoutes.ProfileScreen.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            colors = color
        )
    }
}






