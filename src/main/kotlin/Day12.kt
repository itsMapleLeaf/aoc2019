import math.Vector
import kotlin.math.absoluteValue
import kotlin.math.sign

private val testInput = listOf(
    "<x=-1, y=0, z=2>",
    "<x=2, y=-10, z=-7>",
    "<x=4, y=-8, z=8>",
    "<x=3, y=5, z=-1>"
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

fun totalEnergyInSystem(): Int {
    val vectorStrings = puzzleInput

    val planets = vectorStrings
        .map { vectorString ->
            val intPattern = "-?\\d+"
            val vectorRegex = Regex("<x=($intPattern), y=($intPattern), z=($intPattern)>")

            val match = vectorRegex.find(vectorString) ?: error("invalid planet string (or regex broke)")
            val (x, y, z) = match.groupValues.drop(1).map { it.toInt() }
            Planet(Vector(x, y, z))
        }
        .toMutableList()

    for (n in 0 until 1000) {
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

    return planets.map { it.position.absoluteSum() * it.velocity.absoluteSum() }.sum()
}

fun main() {
    println(totalEnergyInSystem())
}