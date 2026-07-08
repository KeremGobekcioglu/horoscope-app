package com.kg.yildizname.core.domain.model


enum class SignToElement(val element: Element) {
    ARIES(Element.FIRE),
    TAURUS(Element.EARTH),
    GEMINI(Element.AIR),
    CANCER(Element.WATER),
    LEO(Element.FIRE),
    VIRGO(Element.EARTH),
    LIBRA(Element.AIR),
    SCORPIO(Element.WATER),
    SAGITTARIUS(Element.FIRE),
    CAPRICORN(Element.EARTH),
    AQUARIUS(Element.AIR),
    PISCES(Element.WATER)
}

enum class Element {
    FIRE,
    EARTH,
    AIR,
    WATER
}