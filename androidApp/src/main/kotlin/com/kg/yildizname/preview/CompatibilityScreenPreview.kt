package com.kg.yildizname.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.core.ui.utils.yzStatusBarsPadding
import com.kg.yildizname.feature.compatability.ui.CompatibilityScreen
import com.kg.yildizname.feature.compatability.ui.components.AnalyzeButton

@Preview
@Composable
fun CompPreview()
{
    YzTheme {
        CompatibilityScreen()
    }

}

@Preview
@Composable
fun AnalyzeButtonPreviews() {
    YzTheme {
    Box(
        modifier = Modifier.fillMaxSize().yzStatusBarsPadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(

            modifier = Modifier.padding(vertical = 64.dp, horizontal = 32.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            AnalyzeButton(true, onClick = {})
            AnalyzeButton(false, onClick = {})
        }
    }
}
}