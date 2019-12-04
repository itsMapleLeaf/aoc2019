internal fun <T> trace(value: T): T {
    println(value)
    return value
}

internal infix fun Int.approaching(other: Int) = when {
    this < other -> this..other
    else -> this downTo other
}

internal fun <A, B> getPermutations(first: Iterable<A>, second: Iterable<B>): Iterable<Pair<A, B>> {
    return first.flatMap { a -> second.map { b -> Pair(a, b) } }
}

internal fun getInputLine(prefix: String): String {
    print(prefix)
    return readLine() ?: throw Error("failed to get input")
}