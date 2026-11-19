package com.example.autograph_fe.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autograph_fe.ui.screens.auth.LoginRoute
import com.example.autograph_fe.ui.screens.auth.RegisterRoute
import com.example.autograph_fe.ui.screens.onboarding.OnboardingRoute
import com.example.autograph_fe.ui.screens.welcome.WelcomeScreen

@Composable
fun AutographNavHost(
    hasSeenOnboarding: Boolean,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    // After login, register or guest: onboarding the first time, Home after that
    val enterApp: () -> Unit = {
        val next = if (hasSeenOnboarding) Destination.Home else Destination.Onboarding
        navController.navigate(next) {
            popUpTo(Destination.Welcome) { inclusive = true }   // remove Welcome/Login/Register from history
        }
    }

    NavHost(navController = navController, startDestination = Destination.Welcome) {

        composable<Destination.Welcome> {
            WelcomeScreen(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onCreateAccount = { navController.navigate(Destination.Register) },
                onLogIn = { navController.navigate(Destination.Login) },
                onContinueAsGuest = enterApp,
            )
        }

        composable<Destination.Login> {
            LoginRoute(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onLoggedIn = enterApp,
                onBack = { navController.popBackStack() },
                onCreateAccount = {
                    navController.navigate(Destination.Register) {
                        popUpTo(Destination.Login) { inclusive = true }   // swap, don't stack
                    }
                },
            )
        }

        composable<Destination.Register> {
            RegisterRoute(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onRegistered = enterApp,
                onBack = { navController.popBackStack() },
                onLogIn = {
                    navController.navigate(Destination.Login) {
                        popUpTo(Destination.Register) { inclusive = true }
                    }
                },
            )
        }

        composable<Destination.Onboarding> {
            OnboardingRoute(
                onFinished = {
                    navController.navigate(Destination.Home) {
                        popUpTo(Destination.Onboarding) { inclusive = true }
                    }
                },
            )
        }

        composable<Destination.Home> {
            // Placeholder until the real Home screen exists
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "Home",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}
