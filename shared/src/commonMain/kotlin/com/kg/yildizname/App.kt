package com.kg.yildizname

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.kg.yildizname.core.domain.usecase.RegisterDeviceForNotificationsUseCase
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.navigation.YildiznameNavGraph
import org.koin.compose.koinInject
import org.koin.mp.KoinPlatform
import org.koin.mp.KoinPlatformTools

@Composable
fun App() {
    val navController = rememberNavController()
    val registerDevice: RegisterDeviceForNotificationsUseCase = koinInject()

    LaunchedEffect(Unit)
    {
        registerDevice()
    }
    YzTheme {
        YildiznameNavGraph(navController = navController)
    }
}
