typealias Validator = (pass: String) -> Boolean


fun main() {
    val rangeString = "240920-789857"

    val range = run {
        val parts = rangeString.split("-")
        parts[0].toInt()..parts[1].toInt()
    }

    val validators = listOf<Validator>(
        { pass ->
            pass.repeatingGroups.any { it.length == 2 }
        },
        { pass ->
            pass.dropLast(1)
                .indices
                .all { index -> pass[index].asDigit <= pass[index + 1].asDigit }
        }
    )

    val validPasswords = range
        .map { it.toString() }
        .filter { validators.all { isValid -> isValid(it) } }

    println(validPasswords.joinToString("\n"))
    println("valid passwords: ${validPasswords.size}")
}