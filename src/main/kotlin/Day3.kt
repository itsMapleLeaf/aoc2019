import kotlin.math.abs

private data class Point(val x: Int, val y: Int) {
    operator fun plus(point: Point) =
        Point(x + point.x, y + point.y)

    val manhattanDistance get() = abs(x - origin.x) + abs(y - origin.y)

    companion object {
        val origin = Point(1, 1)

        fun fromPair(pair: Pair<Int, Int>) = Point(pair.first, pair.second)
    }
}

private data class Wire(private val instructionListString: String) {
    val points = let {
        fun getMovementVector(instructionString: String) = let {
            val distance = instructionString.drop(1).toInt()
            when (val letter = instructionString[0]) {
                'U' -> Point(0, distance)
                'D' -> Point(0, -distance)
                'L' -> Point(-distance, 0)
                'R' -> Point(distance, 0)
                else -> throw Error("unexpected direction $letter")
            }
        }

        fun getNextPoints(points: List<Point>, movement: Point): List<Point> {
            val lastPoint = points.last()
            val destination = lastPoint + movement

            val tracedPath = getPossiblePermutationPairs(
                lastPoint.x approaching destination.x,
                lastPoint.y approaching destination.y
            )

            return points.dropLast(1) + tracedPath.map { Point.fromPair(it) }
        }

        instructionListString
            .split(",")
            .map(::getMovementVector)
            .fold(listOf(Point.origin), ::getNextPoints)
    }

    fun stepCountTo(point: Point) =
        points.lastIndexOf(point)
}

private data class WireBoard(private val firstInstructionString: String, private val secondInstructionString: String) {
    val firstWire = Wire(firstInstructionString)
    val secondWire = Wire(secondInstructionString)

    val intersections =
        firstWire.points.intersect(secondWire.points) - Point.origin // origin always intersects, take it out

    val lowestManhattanDistance =
        intersections.map { it.manhattanDistance }.min()

    val shortestIntersectionStepCount =
        intersections.map { firstWire.stepCountTo(it) + secondWire.stepCountTo(it) }.min()
}

fun main() {
    val firstWireInstructions = getInputLine("enter first wire instructions: ")
    val secondWireInstructions = getInputLine("enter second wire instructions: ")
    val board = WireBoard(firstWireInstructions, secondWireInstructions)
    println("lowest manhattan distance: ${board.lowestManhattanDistance}")
    println("shortest intersection step count: ${board.shortestIntersectionStepCount}")
}