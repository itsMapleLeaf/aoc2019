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

internal val String.repeatingGroups
    get() = this.drop(1).fold(listOf(this.take(1))) { groups, nextChar ->
        val lastGroup = groups.last()
        val lastCharacter = lastGroup.last()

        if (nextChar == lastCharacter) {
            groups.dropLast(1) + (lastGroup + nextChar)
        } else {
            groups + nextChar.toString()
        }
    }

internal val Char.asDigit get() = toString().toInt()

internal fun <T> Iterable<T>.replace(newValue: T, targetIndex: Int) =
    mapIndexed { index, currentValue -> if (index == targetIndex) newValue else currentValue }

//class WithValueAtIndexTest {
//    @Test
//    fun `sets a value at a given index`() {
//        val input = listOf(1, 2, 3)
//        val result = input.withValueAtIndex(4, 2)
//        assert(result == listOf(1, 2, 4))
//    }
//}
//
