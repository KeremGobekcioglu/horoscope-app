package com.kg.yildizname.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.Reading
import com.kg.yildizname.core.data.model.ScoreSet
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.components.YzBottomNav
import com.kg.yildizname.core.ui.components.YzBottomNavItemData
import com.kg.yildizname.core.util.DateUtils
import com.kg.yildizname.feature.calendar.ui.CalendarDay
import com.kg.yildizname.feature.calendar.ui.CalendarScreen
import com.kg.yildizname.feature.calendar.ui.CalendarUiState
import com.kg.yildizname.feature.calendar.ui.CalendarViewModel
import com.kg.yildizname.feature.calendar.ui.MonthRelation
import com.kg.yildizname.feature.calendar.ui.PageTab
import com.kg.yildizname.feature.compatability.ui.CompatibilityScreen
import com.kg.yildizname.feature.home.ui.HomeScreen
import com.kg.yildizname.feature.home.ui.HomeUiState
import com.kg.yildizname.feature.home.ui.HomeViewModel
import com.kg.yildizname.feature.onboarding.ui.OnboardingEvent
import com.kg.yildizname.feature.onboarding.ui.OnboardingStep1Screen
import com.kg.yildizname.feature.onboarding.ui.OnboardingStep2Screen
import com.kg.yildizname.feature.onboarding.ui.OnboardingStep3Screen
import com.kg.yildizname.feature.onboarding.ui.OnboardingViewModel
import com.kg.yildizname.feature.readingDetail.ui.ReadingDetailScreen
import com.kg.yildizname.feature.readingDetail.ui.ReadingDetailUiState
import com.kg.yildizname.feature.readingDetail.ui.ReadingDetailViewModel
import com.kg.yildizname.feature.settings.ui.SettingsScreen
import com.kg.yildizname.feature.share.ui.ShareCardRequest
import com.kg.yildizname.feature.share.ui.ShareFlowHost
import com.kg.yildizname.feature.share.ui.rememberShareFlowState
import com.kg.yildizname.feature.share.ui.shareCardSignName
import com.kg.yildizname.feature.share.ui.shareQuoteFrom
import com.kg.yildizname.feature.splash.ui.SplashEvent
import com.kg.yildizname.feature.splash.ui.SplashScreen
import com.kg.yildizname.feature.splash.ui.SplashViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.Calendar
import compose.icons.feathericons.Heart
import compose.icons.feathericons.Home
import compose.icons.feathericons.Settings
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.nav_calendar
import horoscope.shared.generated.resources.nav_compatibility
import horoscope.shared.generated.resources.nav_home
import horoscope.shared.generated.resources.nav_settings
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.plus
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

// Routes that show the bottom nav — everything else hides it
private val bottomNavRoutes = setOf("Home", "Calendar", "Compatibility", "Settings")

// Helper that gives every onboarding screen the SAME ViewModel instance.
@Composable
private fun NavBackStackEntry.rememberOnboardingViewModel(
    navController: NavController,
): OnboardingViewModel {
    val graphEntry = remember(this) { navController.getBackStackEntry<OnboardingGraph>() }
    return koinViewModel(viewModelStoreOwner = graphEntry)
}

@Composable
fun YildiznameNavGraph(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    val showBottomNav = bottomNavRoutes.any { currentRoute.contains(it, ignoreCase = true) }

    val shareFlowState = rememberShareFlowState()

    val bottomNavItems = listOf(
        YzBottomNavItemData(
            route = "Home",
            label = stringResource(Res.string.nav_home),
            icon  = FeatherIcons.Home
        ),
        YzBottomNavItemData(
            route = "Compatibility",
            label = stringResource(Res.string.nav_compatibility),
            icon  = FeatherIcons.Heart
        ),
        YzBottomNavItemData(
            route = "Calendar",
            label = stringResource(Res.string.nav_calendar),
            icon  = FeatherIcons.Calendar
        ),
        YzBottomNavItemData(
            route = "Settings",
            label = stringResource(Res.string.nav_settings),
            icon  = FeatherIcons.Settings
        ),
    )

    Box(Modifier.fillMaxSize()) {
    StarFieldBackground(Modifier.fillMaxSize())
    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (showBottomNav) {
                YzBottomNav(
                    currentRoute  = currentRoute,
                    onTabSelected = { route ->
                        val destination: Any = when (route) {
                            "Home"          -> Home
                            "Compatibility" -> Compatibility
                            "Calendar"      -> Calendar
                            "Settings"      -> Settings
                            else            -> return@YzBottomNav
                        }
                        navController.navigate(destination) {
                            popUpTo<Home> { saveState = true }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    },
                    items = bottomNavItems
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Splash,
            modifier         = Modifier.padding(innerPadding)
        ) {

            composable<Splash> {
                val vm: SplashViewModel = koinViewModel()

                LaunchedEffect(vm) {
                    vm.events.collect { event ->
                        when (event) {
                            SplashEvent.NavigateToHome -> navController.navigate(Home) {
                                popUpTo<Splash> { inclusive = true }
                            }
                            SplashEvent.NavigateToOnboarding -> navController.navigate(OnboardingGraph) {
                                popUpTo<Splash> { inclusive = true }
                            }
                        }
                    }
                }

                SplashScreen(onAnimationDone = vm::onAnimationDone)
            }

            navigation<OnboardingGraph>(startDestination = OnboardingStep1) {

                composable<OnboardingStep1> { backStackEntry ->
                    val vm = backStackEntry.rememberOnboardingViewModel(navController)
                    val uiState by vm.uiState.collectAsStateWithLifecycle()

                    LaunchedEffect(vm) {
                        vm.events.collect { event ->
                            when (event) {
                                OnboardingEvent.NavigateToStep2 -> Unit
                                OnboardingEvent.NavigateToStep3 -> Unit
                                // v1: steps 2/3 bypassed, sign is the only required field — see July 9, Thursday
                                OnboardingEvent.NavigateToHome  -> navController.navigate(Home) {
                                    popUpTo<OnboardingGraph> { inclusive = true }
                                }
                            }
                        }
                    }

                    OnboardingStep1Screen(
                        selectedSign   = uiState.selectedSign,
                        onSignSelected = vm::selectSign,
                        onContinue     = vm::confirmSign,
                        error          = uiState.error,
                        onErrorShown   = vm::clearError,
                    )
                }

                composable<OnboardingStep2> { backStackEntry ->
                    val vm = backStackEntry.rememberOnboardingViewModel(navController)
                    val uiState by vm.uiState.collectAsStateWithLifecycle()

                    LaunchedEffect(vm) {
                        vm.events.collect { event ->
                            when (event) {
                                OnboardingEvent.NavigateToStep3 -> navController.navigate(OnboardingStep3)
                                OnboardingEvent.NavigateToStep2 -> Unit
                                OnboardingEvent.NavigateToHome  -> Unit
                            }
                        }
                    }

                    OnboardingStep2Screen(
                        selectedDate  = uiState.birthDate,
                        selectedSign  = uiState.selectedSign,
                        onDateChanged = vm::setBirthDate,
                        onContinue    = vm::confirmBirthDate,
                        onSkip        = vm::skipBirthDate,
                        error         = uiState.error,
                        onErrorShown  = vm::clearError,
                    )
                }

                composable<OnboardingStep3> { backStackEntry ->
                    val vm = backStackEntry.rememberOnboardingViewModel(navController)
                    val uiState by vm.uiState.collectAsStateWithLifecycle()

                    LaunchedEffect(vm) {
                        vm.events.collect { event ->
                            when (event) {
                                OnboardingEvent.NavigateToHome ->
                                    navController.navigate(Home) {
                                        popUpTo<OnboardingGraph> { inclusive = true }
                                    }
                                OnboardingEvent.NavigateToStep2 -> Unit
                                OnboardingEvent.NavigateToStep3 -> Unit
                            }
                        }
                    }

                    OnboardingStep3Screen(
                        optionalData  = uiState.optionalData,
                        onDataChanged = vm::setOptionalData,
                        onStart       = vm::completeOnboarding,
                        onSkip        = vm::skipOptionalData,
                        error         = uiState.error,
                        onErrorShown  = vm::clearError,
                    )
                }
            }

            composable<Home> {
                val vm: HomeViewModel = koinViewModel()
                val uiState by vm.uiState.collectAsStateWithLifecycle()
                val homeSuccess = uiState as? HomeUiState.Success
                val homeShareSignName = homeSuccess?.let { shareCardSignName(it.reading.sign) }

                HomeScreen(
                    uiState             = uiState,
                    onReadMoreClick     = { sign, period ->
                        navController.navigate(ReadingDetail(sign = sign, period = period))
                    },
                    onShareClick        = { /* wire shareText() when Share.kt actuals are ready */ },
                    onShareCardClick    = {
                        if (homeSuccess != null && homeShareSignName != null) {
                            shareFlowState.open(
                                ShareCardRequest(
                                    signDisplayName = homeShareSignName,
                                    sign = homeSuccess.reading.sign,
                                    quoteText = shareQuoteFrom(homeSuccess.reading.text),
                                    date = DateUtils.todayLocalDate(),
                                )
                            )
                        }
                    },
                    onNotificationClick = { /* future: notification settings screen */ },
                    onRetryClick        = { vm.retry() },
                )
            }

            composable<ReadingDetail> { backStackEntry ->
                val route: ReadingDetail = backStackEntry.toRoute()
                val vm: ReadingDetailViewModel = koinViewModel {
                    parametersOf(route.sign, route.period)
                }
                val uiState by vm.uiState.collectAsStateWithLifecycle()

                ReadingDetailScreen(
                    uiState     = uiState,
                    onBackClick = { navController.popBackStack() },
                    onShareClick = {
                        (uiState as? ReadingDetailUiState.Success)?.let { success ->
                            shareFlowState.open(
                                ShareCardRequest(
                                    signDisplayName = success.signDisplayName,
                                    sign = success.sign,
                                    quoteText = shareQuoteFrom(success.generalText),
                                    date = DateUtils.todayLocalDate(),
                                )
                            )
                        }
                    },
                )
            }

            composable<Calendar> {
                val viewModel: CalendarViewModel = koinViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                CalendarScreen(
                    uiState = uiState,
                    onNextMonth = viewModel::onNextMonth,
                    onRetryClick = {},
                    onReadMoreClick = {sign,period ->
                        navController.navigate(ReadingDetail(sign,period))
                    },
                    onPreviousMonth = viewModel::onPreviousMonth,
                    onDaySelectedDay = viewModel::onDaySelected,
                    onTabChange = viewModel::onTabChange
                )
            }

            composable<Compatibility> {
                CompatibilityScreen()
            }

            composable<Settings> {
                SettingsScreen()
            }
        }
    }

    ShareFlowHost(state = shareFlowState)
    }
}