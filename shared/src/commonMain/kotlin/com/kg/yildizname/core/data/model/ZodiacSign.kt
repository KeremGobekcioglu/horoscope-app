package com.kg.yildizname.core.data.model

enum class ZodiacSign(val apiKey: String, val firestoreKey: String) {
    ARIES("aries", "aries"),
    TAURUS("taurus", "taurus"),
    GEMINI("gemini", "gemini"),
    CANCER("cancer", "cancer"),
    LEO("leo", "leo"),
    VIRGO("virgo", "virgo"),
    LIBRA("libra", "libra"),
    SCORPIO("scorpio", "scorpio"),
    SAGITTARIUS("sagittarius", "sagittarius"),
    CAPRICORN("capricorn", "capricorn"),
    AQUARIUS("aquarius", "aquarius"),
    PISCES("pisces", "pisces");

    companion object {
        // Maps Turkish onboarding keys to enum entries so existing prefs continue to work.
        private val turkishKeys = mapOf(
            "koc" to ARIES, "boga" to TAURUS, "ikizler" to GEMINI,
            "yengec" to CANCER, "aslan" to LEO, "basak" to VIRGO,
            "terazi" to LIBRA, "akrep" to SCORPIO, "yay" to SAGITTARIUS,
            "oglak" to CAPRICORN, "kova" to AQUARIUS, "balik" to PISCES,
        )

        fun fromKey(key: String): ZodiacSign =
            entries.firstOrNull { it.apiKey == key.lowercase() }
                ?: turkishKeys[key.lowercase()]
                ?: SCORPIO
    }
}
