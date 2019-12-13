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

private data class Planet(var position: Vector, var velocity: Vector = Vector(0, 0, 0)) {
    companion object {
        private const val intPattern = "-?\\d+"
        private val vectorRegex = Regex("<x=($intPattern), y=($intPattern), z=($intPattern)>")

        fun fromVectorString(vectorString: String): Planet {
            val match = vectorRegex.find(vectorString) ?: error("invalid planet string (or regex broke)")
            val (x, y, z) = match.groupValues.drop(1).map { it.toInt() }
            return Planet(x, y, z)
        }
    }

    constructor(x: Int, y: Int, z: Int) : this(Vector(x, y, z))

    fun energy() = position.absoluteSum() * velocity.absoluteSum()
}

fun main() {
    val vectorStrings = puzzleInput
    val planets = vectorStrings.map { Planet.fromVectorString(it) }.toMutableList()
    val stepCount = 1000

    (0 until stepCount).forEach { _ ->
        planets.indices.forEach { firstIndex ->
            ((firstIndex + 1)..planets.lastIndex).forEach { secondIndex ->
                val first = planets[firstIndex]
                val second = planets[secondIndex]

                val difference = (second.position - first.position).signs()

                first.velocity += difference
                second.velocity -= difference

//            println("$first, $second, $difference")
            }
        }

        planets.forEach { planet -> planet.position += planet.velocity }
    }

    val totalEnergy = planets.map { it.energy() }.sum()

    println(totalEnergy)
}