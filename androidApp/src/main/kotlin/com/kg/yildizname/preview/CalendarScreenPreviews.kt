package com.kg.yildizname.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.feature.calendar.ui.CalendarScreen

@Preview(name = "Calendar", showBackground = true, heightDp = 800)
@Composable
private fun CalendarScreenPreview() {
    YzTheme {
        CalendarScreen()
    }
}
