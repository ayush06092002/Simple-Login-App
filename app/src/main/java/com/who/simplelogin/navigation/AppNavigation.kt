package com.who.simplelogin.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.who.simplelogin.screens.HomeScreen
import com.who.simplelogin.screens.login.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = AppScreens.Login.name) {
        composable(AppScreens.Login.name) {
            LoginScreen(navController = navController)
        }

        composable(AppScreens.Home.name) {
            HomeScreen(navController = navController)
        }
    }
}