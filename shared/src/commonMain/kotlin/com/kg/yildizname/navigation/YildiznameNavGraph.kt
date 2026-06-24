package com.kg.yildizname.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kg.yildizname.feature.calendar.ui.CalendarScreen
import com.kg.yildizname.feature.compatibility.ui.CompatibilityScreen
import com.kg.yildizname.feature.home.ui.HomeScreen
import com.kg.yildizname.feature.onboarding.ui.OnboardingEvent
import com.kg.yildizname.feature.onboarding.ui.OnboardingStep1Screen
import com.kg.yildizname.feature.onboarding.ui.OnboardingViewModel
import com.kg.yildizname.feature.reading.ui.ReadingDetailScreen
import com.kg.yildizname.feature.settings.ui.SettingsScreen
import com.kg.yildizname.feature.splash.ui.SplashScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun YildiznameNavGraph(navController: NavHostController) {
    NavHost(
        navController    = navController,
        startDestination = Splash
    ) {
        composable<Splash> {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(OnboardingStep1) { // TODO: restore to Home after testing
                        popUpTo<Splash> { inclusive = true }
                    }
                },
                onNavigateToOnboarding = {
                    navController.navigate(OnboardingStep1) {
                        popUpTo<Splash> { inclusive = true }
                    }
                }
            )
        }

        composable<OnboardingStep1> {
            val vm: OnboardingViewModel = koinViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                vm.events.collect { event ->
                    when (event) {
                        OnboardingEvent.NavigateToStep2 ->
                            navController.navigate(OnboardingStep2)
                    }
                }
            }

            OnboardingStep1Screen(
                selectedSign   = uiState.selectedSign,
                onSignSelected = vm::selectSign,
                onContinue     = { vm.confirmSign() },
            )
        }

        composable<OnboardingStep2> {
            Text("Onboarding Step 2 — coming soon")
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
                sign   = route.sign,
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
