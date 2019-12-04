typealias Validator = (pass: String) -> Boolean

fun <T> Iterable<T>.anyIndexed(predicate: (Int, T) -> Boolean): Boolean {
    for ((index, value) in withIndex()) {
        if (predicate(index, value)) {
            return true
        }
    }
    return false
}

fun <T> Iterable<T>.allIndexed(predicate: (Int, T) -> Boolean): Boolean {
    for ((index, value) in withIndex()) {
        if (!predicate(index, value)) {
            return false
        }
    }
    return true
}

val Char.asDigit get() = toString().toInt()

fun main() {
    val rangeString = "240920-789857"

    val range = run {
        val parts = rangeString.split("-")
        parts[0].toInt()..parts[1].toInt()
    }

    val validators = listOf<Validator>(
        { pass ->
            pass
                .drop(1)
                .fold(listOf(pass.take(1))) { groups, nextChar ->
                    when (nextChar) {
                        groups.last().last() ->
                            groups.dropLast(1) + (groups.last() + nextChar)
                        else ->
                            groups + nextChar.toString()
                    }
                }
                .any { it.length == 2 }
        },
        {
            it.dropLast(1)
                .toList()
                .allIndexed { index, char -> char.asDigit <= it[index + 1].asDigit }
        }
    )

    val validPasswords = range
        .map { it.toString() }
        .filter { validators.all { isValid -> isValid(it) } }

    println(validPasswords.joinToString("\n"))
    println("valid passwords: ${validPasswords.size}")
}