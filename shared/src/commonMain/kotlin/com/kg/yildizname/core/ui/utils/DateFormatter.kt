package com.kg.yildizname.core.ui.utils

import com.kg.yildizname.core.util.currentLanguageCode
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number

private fun deviceLanguage(): Language =
    if (currentLanguageCode() == "tr") Language.TURKISH else Language.ENGLISH

object DateFormatter {

    private val monthsTr = listOf(
        "Ocak", "Şubat", "Mart", "Nisan",
        "Mayıs", "Haziran", "Temmuz", "Ağustos",
        "Eylül", "Ekim", "Kasım", "Aralık"
    )
    private val monthsEn = listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )

    private val weekdaysTr = listOf(
        "Pazartesi", "Salı", "Çarşamba", "Perşembe",
        "Cuma", "Cumartesi", "Pazar"
    )
    private val weekdaysEn = listOf(
        "Monday", "Tuesday", "Wednesday", "Thursday",
        "Friday", "Saturday", "Sunday"
    )

    private val weekdayAbbreviationsTr = listOf(
        "Pzt", "Sa", "Ça", "Pe", "Cu", "Cmt", "Pzr"
    )
    private val weekdayAbbreviationsEn = listOf(
        "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
    )

    fun weekdayAbbreviations(language: Language = deviceLanguage()): List<String> =
        if (language == Language.TURKISH) weekdayAbbreviationsTr else weekdayAbbreviationsEn

    fun monthYear(date: LocalDate, language: Language = deviceLanguage()): String {
        val months = if (language == Language.TURKISH) monthsTr else monthsEn
        return "${months[date.month.number - 1]} ${date.year}"
    }

    fun fullDate(date: LocalDate, language: Language = deviceLanguage()): String {
        val months = if (language == Language.TURKISH) monthsTr else monthsEn
        val weekdays = if (language == Language.TURKISH) weekdaysTr else weekdaysEn
        val month = months[date.month.number - 1]
        val weekday = weekdays[date.dayOfWeek.isoDayNumber - 1]
        return when (language) {
            Language.TURKISH -> "${date.day} $month $weekday"
            Language.ENGLISH -> "$month ${date.day}, $weekday"
        }
    }

    fun formatDate(
        date: String,
        language: Language = deviceLanguage()
    ): String {

        val parts = date.split("-")
        require(parts.size == 3)

        val year = parts[0]
        val month = parts[1].toInt()
        val day = parts[2].toInt()

        val months = when (language) {
            Language.ENGLISH -> monthsEn
            Language.TURKISH -> monthsTr
        }

        return when (language) {
            Language.TURKISH -> "$day ${months[month - 1]} $year"
            Language.ENGLISH -> "${months[month - 1]} $day, $year"
        }
    }
}

enum class Language {
    ENGLISH,
    TURKISH
}