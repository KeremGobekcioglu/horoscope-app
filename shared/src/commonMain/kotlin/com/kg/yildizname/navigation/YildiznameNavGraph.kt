package com.kg.yildizname.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.kg.yildizname.feature.calendar.ui.CalendarScreen
import com.kg.yildizname.feature.compatibility.ui.CompatibilityScreen
import com.kg.yildizname.feature.home.ui.HomeScreen
import com.kg.yildizname.feature.onboarding.ui.OnboardingEvent
import com.kg.yildizname.feature.onboarding.ui.OnboardingStep1Screen
import com.kg.yildizname.feature.onboarding.ui.OnboardingStep2Screen
import com.kg.yildizname.feature.onboarding.ui.OnboardingStep3Screen
import com.kg.yildizname.feature.onboarding.ui.OnboardingViewModel
import com.kg.yildizname.feature.reading.ui.ReadingDetailScreen
import com.kg.yildizname.feature.settings.ui.SettingsScreen
import com.kg.yildizname.feature.splash.ui.SplashScreen
import org.koin.compose.viewmodel.koinViewModel

// Helper that gives every onboarding screen the SAME ViewModel instance.
// Normally koinViewModel() creates a fresh VM per screen. Here we instead
// look up the back-stack entry for the parent OnboardingGraph and ask Koin
// to store the VM there. Because all three steps share the same parent entry,
// they all get the same object — state is never lost when moving between steps.
@Composable
private fun NavBackStackEntry.rememberOnboardingViewModel(
    navController: NavController,
): OnboardingViewModel {
    // remember() caches the result so we don't re-fetch on every recomposition.
    // "this" (the current back-stack entry) is the cache key — if it ever
    // changes, the block runs again.
    val graphEntry = remember(this) { navController.getBackStackEntry<OnboardingGraph>() }
    return koinViewModel(viewModelStoreOwner = graphEntry)
}

@Composable
fun YildiznameNavGraph(navController: NavHostController) {
    NavHost(
        navController    = navController,
        startDestination = Splash
    ) {
        composable<Splash> {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(OnboardingGraph) {
                        popUpTo<Splash> { inclusive = true }
                    }
                },
                onNavigateToOnboarding = {
                    navController.navigate(OnboardingGraph) {
                        popUpTo<Splash> { inclusive = true }
                    }
                }
            )
        }

        // Groups all onboarding screens under one parent route (OnboardingGraph).
        // This lets us share a single ViewModel across Step 1, 2, and 3, and
        // pop the entire flow in one go when onboarding is complete.
        navigation<OnboardingGraph>(startDestination = OnboardingStep1) {

            composable<OnboardingStep1> { backStackEntry ->
                val vm = backStackEntry.rememberOnboardingViewModel(navController)
                val uiState by vm.uiState.collectAsStateWithLifecycle() // stops collecting when the screen is not visible

                // LaunchedEffect starts a coroutine that listens for one-time events
                // from the ViewModel (like "go to the next screen"). Using `vm` as the
                // key means the coroutine restarts only if the ViewModel instance changes,
                // not on every recomposition. This prevents duplicate event handling.
                LaunchedEffect(vm) {
                    vm.events.collect { event ->
                        // Exhaustive when — no else branch. If a new event is added to
                        // OnboardingEvent later, the compiler will force us to handle it here.
                        when (event) {
                            OnboardingEvent.NavigateToStep2 -> navController.navigate(OnboardingStep2)
                            OnboardingEvent.NavigateToStep3 -> Unit // not this screen's responsibility
                            OnboardingEvent.NavigateToHome  -> Unit // not this screen's responsibility
                        }
                    }
                }

                OnboardingStep1Screen(
                    selectedSign   = uiState.selectedSign,
                    onSignSelected = vm::selectSign,
                    onContinue     = { vm.confirmSign() },
                    error          = uiState.error,
                    onErrorShown   = vm::clearError,
                )
            }

            composable<OnboardingStep2> { backStackEntry ->
                val vm = backStackEntry.rememberOnboardingViewModel(navController)
                val uiState by vm.uiState.collectAsStateWithLifecycle() // stops collecting when the screen is not visible

                LaunchedEffect(vm) {
                    vm.events.collect { event ->
                        when (event) {
                            OnboardingEvent.NavigateToStep3 -> navController.navigate(OnboardingStep3)
                            OnboardingEvent.NavigateToStep2 -> Unit // not this screen's responsibility
                            OnboardingEvent.NavigateToHome  -> Unit // not this screen's responsibility
                        }
                    }
                }

                OnboardingStep2Screen(
                    selectedDate  = uiState.birthDate,
                    onDateChanged = vm::setBirthDate,
                    onContinue    = { vm.confirmBirthDate() },
                    onSkip        = { vm.skipBirthDate() },
                    error         = uiState.error,
                    onErrorShown  = vm::clearError,
                )
            }

            composable<OnboardingStep3> { backStackEntry ->
                val vm = backStackEntry.rememberOnboardingViewModel(navController)
                val uiState by vm.uiState.collectAsStateWithLifecycle() // stops collecting when the screen is not visible

                LaunchedEffect(vm) {
                    vm.events.collect { event ->
                        when (event) {
                            OnboardingEvent.NavigateToHome ->
                                // inclusive = true removes OnboardingGraph itself from the back
                                // stack, not just its children. This means pressing back from
                                // Home will not send the user back into onboarding.
                                navController.navigate(Home) {
                                    popUpTo<OnboardingGraph> { inclusive = true }
                                }
                            OnboardingEvent.NavigateToStep2 -> Unit // not this screen's responsibility
                            OnboardingEvent.NavigateToStep3 -> Unit // not this screen's responsibility
                        }
                    }
                }

                OnboardingStep3Screen(
                    optionalData  = uiState.optionalData,
                    onDataChanged = vm::setOptionalData,
                    onStart       = { vm.completeOnboarding() },
                    onSkip        = { vm.skipOptionalData() },
                    error         = uiState.error,
                    onErrorShown  = vm::clearError,
                )
            }
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
