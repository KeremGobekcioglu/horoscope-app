package com.kg.horoscope

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.kg.horoscope.navigation.YildiznameNavGraph
import com.kg.horoscope.ui.theme.YzTheme

@Composable
fun App() {
    val navController = rememberNavController()
    YzTheme {
        YildiznameNavGraph(navController = navController)
    }
}
