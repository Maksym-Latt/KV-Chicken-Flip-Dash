package com.chicken.flipdash

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chicken.flipdash.ui.screens.splash.SplashScreen
import com.chicken.flipdash.ui.screens.game.GameScreen
import com.chicken.flipdash.ui.screens.menu.MenuScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Menu : Screen("menu")
    object Game : Screen("game")
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onContinue = {
                navController.navigate(Screen.Menu.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Menu.route) {
            MenuScreen(onStartGame = { navController.navigate(Screen.Game.route) })
        }
        composable(Screen.Game.route) {
            GameScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
