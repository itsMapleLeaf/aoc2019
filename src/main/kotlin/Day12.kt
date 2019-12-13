import math.Vector
import kotlin.math.absoluteValue
import kotlin.math.sign

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

private data class Planet(var position: Vector, var velocity: Vector = Vector(0, 0, 0))

private class Simulation(planetPositionStrings: List<String>) {
    val planets = planetPositionStrings
        .map { vectorString ->
            val intPattern = "-?\\d+"
            val vectorRegex = Regex("<x=($intPattern), y=($intPattern), z=($intPattern)>")

            val match = vectorRegex.find(vectorString) ?: error("invalid planet string (or regex broke)")
            val (x, y, z) = match.groupValues.drop(1).map { it.toInt() }
            Planet(Vector(x, y, z))
        }
        .toMutableList()

    @ExperimentalUnsignedTypes
    fun stateHash(): Long {
        return this.planets.hashCode().toUInt().toLong()
    }

    fun step() {
        for ((firstIndex, first) in planets.withIndex()) {
            for (second in planets.slice((firstIndex + 1)..planets.lastIndex)) {
                val difference = (second.position - first.position).signs()
                first.velocity += difference
                second.velocity -= difference
            }
        }

        for (planet in planets) {
            planet.position += planet.velocity
        }
    }
}

private fun totalEnergyInSystem(): Int {
    val simulation = Simulation(puzzleInput)

    for (i in 0 until 1000) {
        simulation.step()
    }

    return simulation.planets.map { it.position.absoluteSum() * it.velocity.absoluteSum() }.sum()
}

@ExperimentalUnsignedTypes
private fun originOfTheUniverse(): Long {
    val simulation = Simulation(testInput2)
    var simulationHashBits = 0L
    var i = 0L

    while (true) {
        simulation.step()
        i += 1

        val hash = simulation.stateHash()

        val current = simulationHashBits
        val next = simulationHashBits or hash
        println("$current or $hash = $next")
        if (current == next) {
            return i
        }

        simulationHashBits = next

        if (i % 1_000_000 == 0L) {
            println(i)
        }
    }
}

fun main() {
//    println(totalEnergyInSystem())
    println(originOfTheUniverse())
}
