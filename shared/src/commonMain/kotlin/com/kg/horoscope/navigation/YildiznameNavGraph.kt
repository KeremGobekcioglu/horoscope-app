package com.kg.horoscope.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kg.horoscope.screens.CalendarScreen
import com.kg.horoscope.screens.CompatibilityScreen
import com.kg.horoscope.screens.HomeScreen
import com.kg.horoscope.screens.OnboardingScreen
import com.kg.horoscope.screens.ReadingDetailScreen
import com.kg.horoscope.screens.SettingsScreen
import com.kg.horoscope.screens.SplashScreen

@Composable
fun YildiznameNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Splash
    ) {
        composable<Splash> {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Home) {
                        popUpTo<Splash> { inclusive = true }
                    }
                },
                onNavigateToOnboarding = {
                    navController.navigate(Onboarding) {
                        popUpTo<Splash> { inclusive = true }
                    }
                }
            )
        }

        composable<Onboarding> {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Home) {
                        popUpTo<Onboarding> { inclusive = true }
                    }
                }
            )
        }

        composable<Home> {
            HomeScreen(
                onReadingDetail = { sign, period ->
                    navController.navigate(ReadingDetail(sign = sign, period = period))
                }
            )
        }

        composable<ReadingDetail> { backStackEntry ->
            val route: ReadingDetail = backStackEntry.toRoute()
            ReadingDetailScreen(
                sign = route.sign,
                period = route.period,
                onBack = { navController.popBackStack() }
            )
        }

        composable<Calendar> {
            CalendarScreen()
        }

        composable<Compatibility> {
            CompatibilityScreen()
        }

        composable<Settings> {
            SettingsScreen()
        }
    }
}
