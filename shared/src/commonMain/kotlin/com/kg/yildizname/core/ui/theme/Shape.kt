package com.kg.yildizname.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val CardShape   = RoundedCornerShape(20.dp)
val ChipShape   = RoundedCornerShape(12.dp)
val ButtonShape = RoundedCornerShape(16.dp)
val PillShape   = RoundedCornerShape(999.dp)
val SheetShape  = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
val SquareShape = RoundedCornerShape(4.dp)

val YzShapes = Shapes(
    extraSmall = ChipShape,
    small      = ChipShape,
    medium     = ButtonShape,
    large      = CardShape,
    extraLarge = PillShape
)
