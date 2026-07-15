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
import com.kg.yildizname.App
import com.kg.yildizname.core.ui.theme.AppIcons
import com.kg.yildizname.core.ui.theme.YzBgLight
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.cons
import horoscope.shared.generated.resources.pros
import org.jetbrains.compose.resources.stringResource

@Composable
fun InfoCards(
    headlineIcon: ImageVector? = null,
    contentLineIcon: ImageVector?,
    headLineIconTint: Color? = null,
    contentLineIconTint: Color?,
    textList: List<String> = emptyList(),
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

@Composable
fun ProsConsCard(
    pros: List<String> = emptyList(),
    cons: List<String> = emptyList()
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(YzBgLight.copy(alpha = 0.65f))
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProsConsColumn(
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.pros),
            items = pros,
            itemIcon = AppIcons.Positive,
            color = YzGold
        )
        ProsConsColumn(
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.cons),
            items = cons,
            itemIcon = AppIcons.Negative,
            color = YzInk
        )
    }
}

@Composable
private fun ProsConsColumn(
    modifier: Modifier,
    title: String,
    items: List<String>,
    itemIcon: ImageVector,
    color: Color
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            modifier = Modifier.padding(horizontal = 5.dp)
        ) {
            Icon(
                imageVector = AppIcons.Bullet,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(6.dp)
            )
            Text(
                text = title,
                color = color,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
        items.forEach { text ->
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 1.dp)
            ) {
                Icon(
                    imageVector = itemIcon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .padding(top = 1.dp)
                        .size(12.dp)
                )
                Text(
                    text = text,
                    color = color,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}