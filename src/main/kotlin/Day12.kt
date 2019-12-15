import math.Vector
import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlin.math.sqrt

private val testInput = listOf(
    "<x=-1, y=0, z=2>",
    "<x=2, y=-10, z=-7>",
    "<x=4, y=-8, z=8>",
    "<x=3, y=5, z=-1>"
)

private val testInput2 = listOf(
    "<x=-8, y=-10, z=0>",
    "<x=5, y=5, z=10>",
    "<x=2, y=-7, z=3>",
    "<x=9, y=-8, z=-3>"
)

private val puzzleInput = listOf(
    "<x=13, y=-13, z=-2>",
    "<x=16, y=2, z=-15>",
    "<x=7, y=-18, z=-12>",
    "<x=-3, y=-8, z=-8>"
)

private fun Vector.absoluteSum() =
    x.absoluteValue + y.absoluteValue + z.absoluteValue

private fun Vector.signs() =
    Vector(x.sign, y.sign, z.sign)

private fun Long.sqrt() =
    sqrt(toDouble()).toLong()

private fun naturalNumbers() =
    generateSequence(1L) { it + 1 }

private fun primes(): Sequence<Long> = sequence {
    primesLoop@ for (possiblePrime in naturalNumbers()) {
        for (possibleDivisor in possiblePrime.sqrt() downTo 2) {
            if (possiblePrime % possibleDivisor == 0L) continue@primesLoop
        }
        yield(possiblePrime)
    }
}

// written using the Cake/Ladder method https://www.calculatorsoup.com/calculators/math/lcm.php
private fun leastCommonMultiple(vararg numbers: Number): Long {
    val cake = mutableListOf(numbers.map { it.toLong() })
    val ladder = mutableListOf<Long>()

    while (true) {
        val currentRow = cake.last()

        val dividingPrime = primes().drop(1)
            .takeWhile { it <= currentRow.max() ?: error("row is empty?") }
            .filter { prime -> currentRow.count { it % prime == 0L } >= 2 }
            .maxBy { prime -> currentRow.count { it % prime == 0L } }
            ?: break

        val nextRow = currentRow.map { if (it % dividingPrime == 0L) it / dividingPrime else it }

        cake.add(nextRow)
        ladder += dividingPrime
    }

    val factors = ladder + cake.last()
    return factors.reduce(Long::times)
}

private data class Moon(var position: Vector, var velocity: Vector = Vector(0, 0, 0))

private fun createMoons(positionStrings: List<String>): List<Moon> =
    positionStrings.map { vectorString ->
        val intPattern = "-?\\d+"
        val vectorRegex = Regex("<x=($intPattern), y=($intPattern), z=($intPattern)>")

        val match = vectorRegex.find(vectorString) ?: error("invalid planet string (or regex broke)")
        val (x, y, z) = match.groupValues.drop(1).map { it.toInt() }

        Moon(Vector(x, y, z))
    }

private fun totalEnergyInSystem(): Int {
    val moons = createMoons(puzzleInput)

    fun step() {
        for ((firstIndex, first) in moons.withIndex()) {
            for (second in moons.slice((firstIndex + 1)..moons.lastIndex)) {
                val difference = (second.position - first.position).signs()
                first.velocity += difference
                second.velocity -= difference
            }
        }

        for (moon in moons) {
            moon.position += moon.velocity
        }
    }

    for (i in 0 until 1000) {
        step()
    }

    return moons.map { it.position.absoluteSum() * it.velocity.absoluteSum() }.sum()
}

private fun originOfTheUniverse(): Long {
    val positionStrings = puzzleInput

    fun stepsUntilRepeatedState(runVelocityStep: (Moon, Moon) -> Unit): Int {
        val moons = createMoons(positionStrings)
        val states = mutableSetOf<String>()

        while (true) {
            for ((firstIndex, first) in moons.withIndex()) {
                for (second in moons.slice((firstIndex + 1)..moons.lastIndex)) {
                    runVelocityStep(first, second)
                }
            }

            for (moon in moons) {
                moon.position += moon.velocity
            }

            val currentState = moons.toString()
            if (states.contains(currentState)) {
                return states.size
            }

            states.add(currentState)
        }
    }

    fun simulateX(first: Moon, second: Moon) {
        val difference = (second.position.x - first.position.x).sign
        first.velocity += Vector(difference, 0, 0)
        second.velocity -= Vector(difference, 0, 0)
    }

    fun simulateY(first: Moon, second: Moon) {
        val difference = (second.position.y - first.position.y).sign
        first.velocity += Vector(0, difference, 0)
        second.velocity -= Vector(0, difference, 0)
    }

    fun simulateZ(first: Moon, second: Moon) {
        val difference = (second.position.z - first.position.z).sign
        first.velocity += Vector(0, 0, difference)
        second.velocity -= Vector(0, 0, difference)
    }

    val x = stepsUntilRepeatedState(::simulateX)
    val y = stepsUntilRepeatedState(::simulateY)
    val z = stepsUntilRepeatedState(::simulateZ)
    return leastCommonMultiple(x, y, z)
}

fun main() {
    println(totalEnergyInSystem())
    println(originOfTheUniverse())
}

