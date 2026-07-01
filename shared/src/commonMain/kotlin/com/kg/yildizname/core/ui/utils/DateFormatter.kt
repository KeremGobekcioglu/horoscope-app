object DateFormatter {

    private val monthsEn = listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )

    private val monthsTr = listOf(
        "Ocak", "Şubat", "Mart", "Nisan",
        "Mayıs", "Haziran", "Temmuz", "Ağustos",
        "Eylül", "Ekim", "Kasım", "Aralık"
    )

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