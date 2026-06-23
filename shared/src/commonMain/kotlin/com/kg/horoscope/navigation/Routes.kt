package com.kg.horoscope.navigation

import kotlinx.serialization.Serializable

@Serializable
object Splash

@Serializable
object Onboarding

@Serializable
object Home

@Serializable
data class ReadingDetail(val sign: String, val period: String)

@Serializable
object Calendar

@Serializable
object Compatibility

@Serializable
object Settings
