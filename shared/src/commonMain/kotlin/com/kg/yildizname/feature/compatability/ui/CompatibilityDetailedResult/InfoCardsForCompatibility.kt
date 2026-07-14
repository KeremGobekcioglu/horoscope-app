package com.kg.yildizname.feature.compatability.ui.CompatibilityDetailedResult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.theme.YzBgLight
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk

@Composable
fun InfoCards(
    headlineIcon: ImageVector? = null,
    contentLineIcon: ImageVector?,
    headLineIconTint: Color? = null,
    contentLineIconTint: Color?,
    textList: List<String> = mutableListOf(),
    backgroundColor: Color,
    headlineText: String,
    iconOffset: Dp?,
    iconSize: Dp? = null
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp).fillMaxWidth()
        )
        {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                if (headlineIcon != null && headLineIconTint != null) {
                    Icon(
                        imageVector = headlineIcon,
                        contentDescription = null,
                        tint = headLineIconTint,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = headlineText,
                    fontSize = 18.sp,
                    color = YzGold
                )
            }

            textList.forEach { text ->
                if (contentLineIcon == null) {
                    Text(
                        text = "$text.",
                        color = YzInk,
                        fontSize = 13.sp,
                        lineHeight = 20.sp // fix line height explicitly
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = contentLineIcon,
                            tint = contentLineIconTint ?: Color.Unspecified,
                            modifier = Modifier.size(iconSize ?: 8.dp).offset(y = iconOffset ?: (5).dp),
                            contentDescription = null
                        )
                        Text(
                            text = "$text.",
                            color = YzInk,
                            fontSize = 13.sp,
                            lineHeight = 20.sp // fix line height explicitly
                        )
                    }
                }

            }
        }
    }
}