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
            pass.toList().sorted() == pass.toList()
        }
    )

    val validPasswords = range
        .map { it.toString() }
        .filter { validators.all { isValid -> isValid(it) } }

    println(validPasswords.joinToString("\n"))
    println("valid passwords: ${validPasswords.size}")
}