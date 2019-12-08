internal fun <T> trace(value: T): T {
    println(value)
    return value
}

internal infix fun Int.approaching(other: Int) = when {
    this < other -> this..other
    else -> this downTo other
}

internal fun <A, B> getPossiblePermutationPairs(first: Iterable<A>, second: Iterable<B>): List<Pair<A, B>> {
    return first.flatMap { a -> second.map { b -> Pair(a, b) } }
}

internal fun <T> List<T>.permutations(): Set<List<T>> = when (size) {
    0 -> emptySet()
    1 -> setOf(listOf(first()))
    else -> {
        val first = first()
        drop(1).permutations()
            .flatMap { sublist -> (0..sublist.size).map { index -> sublist.plusAt(index, first) } }
            .toSet()
    }
}

internal fun <T> List<T>.plusAt(index: Int, element: T) = when (index) {
    !in 0..size -> throw Error("cannot plusAt: $index is not in range")
    0 -> listOf(element) + this
    size - 1 -> this + element
    else -> take(index) + element + drop(index)
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

internal fun <T> List<T>.toPair() = Pair(this[0], this[1])

internal fun <T> MutableList<T>.popOrNull(): T? {
    if (isEmpty()) {
        return null
    }
    return removeAt(0)
}

// TODO: make this less bad and/or fix permutations()
internal fun getUniqueFiveLengthPermutations(start: Int, end: Int): MutableSet<List<Int>> {
    val result = mutableSetOf<List<Int>>()
    for (a in start..end) {
        for (b in start..end) {
            for (c in start..end) {
                for (d in start..end) {
                    for (e in start..end) {
                        val permutation = listOf(a, b, c, d, e)
                        if (permutation.toSet().size == 5) {
                            result.add(permutation)
                        }
                    }
                }
            }
        }
    }
    return result
}