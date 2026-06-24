package com.kg.yildizname

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.navigation.YildiznameNavGraph

@Composable
fun App() {
    val navController = rememberNavController()
    YzTheme {
        YildiznameNavGraph(navController = navController)
    }
}
