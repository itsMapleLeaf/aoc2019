fun main() {
    val rangeString = "240920-789857"
    val (start, end) = rangeString.split("-").map { it.toInt() }

    fun hasRepeatingGroupOfTwo(pass: String) =
        pass.repeatingGroups.any { it.length == 2 }

    fun hasAscendingDigits(pass: String) =
        pass.toList().sorted() == pass.toList()

    val validPasswords = (start..end)
        .map { it.toString() }
        .filter { hasRepeatingGroupOfTwo(it) && hasAscendingDigits(it) }

    println(validPasswords.joinToString("\n"))
    println("valid passwords: ${validPasswords.size}")
}
