package com.kg.yildizname.core.util

// util/CompatibilityIds.kt

import com.kg.yildizname.core.data.model.ZodiacSign

fun pairId(signA: ZodiacSign, signB: ZodiacSign): String =
    listOf(signA.firestoreKey, signB.firestoreKey).sorted().joinToString("_")