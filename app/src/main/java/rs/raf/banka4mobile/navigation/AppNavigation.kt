package rs.raf.banka4mobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import rs.raf.banka4mobile.presentation.exchange.ExchangeScreen
import rs.raf.banka4mobile.presentation.home.HomeScreen
import rs.raf.banka4mobile.presentation.login.LoginScreen
import rs.raf.banka4mobile.presentation.profile.ProfileScreen
import rs.raf.banka4mobile.presentation.verification.VerificationScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigateToHome() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onOpenVerification = { navController.navigate(Screen.Verification.route) },
                onOpenExchange = { navController.navigate(Screen.Exchange.route) },
                onOpenProfile = { navController.navigate(Screen.Profile.route) }
            )
        }

        composable(Screen.Verification.route) {
            VerificationScreen(
                onBack = { navController.navigateToHome() }
            )
        }

        composable(Screen.Exchange.route) {
            ExchangeScreen(
                onBack = { navController.navigateToHome() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack = { navController.navigateToHome() },
                onLogoutSuccess = { navController.navigateToLogin() }
            )
        }
    }
}

fun NavController.navigateToHome() {
    navigate(Screen.Home.route) {
        popUpTo(Screen.Login.route) {
            inclusive = true
        }
    }
}

fun NavController.navigateToLogin() {
    navigate(Screen.Login.route) {
        popUpTo(graph.id) {
            inclusive = true
        }
        launchSingleTop = true
    }
}