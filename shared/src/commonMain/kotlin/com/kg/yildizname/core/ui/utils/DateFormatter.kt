import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number

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

    fun monthYear(date: LocalDate, language: Language = Language.TURKISH): String {
        val months = if (language == Language.TURKISH) monthsTr else monthsEn
        return "${months[date.month.number - 1]} ${date.year}"
    }

    fun fullDate(date: LocalDate, language: Language = Language.TURKISH): String {
        val months = if (language == Language.TURKISH) monthsTr else monthsEn
        val weekdays = if (language == Language.TURKISH) weekdaysTr else weekdaysEn
        val weekday = weekdays[date.dayOfWeek.isoDayNumber - 1]
        return "${date.dayOfYear} ${months[date.month.number - 1]}, $weekday"
    }

    fun formatDate(
        date: String,
        language: Language = Language.ENGLISH
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

        return "$day ${months[month - 1]} $year"
    }
}

enum class Language {
    ENGLISH,
    TURKISH
}