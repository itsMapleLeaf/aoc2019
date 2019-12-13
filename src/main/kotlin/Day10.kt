
import math.Vector
import math.isInRange
import java.io.File
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sign

fun main() {
    val location = findBestMonitoringLocation(puzzleInput)
    println(location.visibleDetectionCount())

    val vaporized = location.vaporizeAsteroids()
    val (x, y) = vaporized.drop(199).first().vector
    println(x * 100 + y)
}

data class MonitoringLocation(val vector: Vector, val detections: List<AsteroidDetection>) {
    fun visibleDetectionCount() =
        detections.map { it.baseAngle }.toSet().size

    fun vaporizeAsteroids() = sequence {
        val targets = detections
            .sortedWith(Comparator { a, b ->
                when {
                    a.clockwiseAngle != b.clockwiseAngle -> (a.clockwiseAngle - b.clockwiseAngle).sign.toInt()
                    else -> (a.distance - b.distance).sign.toInt()
                }
            })
            .toMutableList()

        var currentAngle = Double.NEGATIVE_INFINITY
        while (targets.isNotEmpty()) {
            val next = targets
                .find { it.clockwiseAngle > currentAngle }
                ?: targets[0]

            targets.remove(next)
            currentAngle = next.clockwiseAngle
            yield(next)
        }
    }
}

data class AsteroidDetection(val vector: Vector, val baseAngle: Double, val distance: Double) {
    // returns the angle in range of [0, PI * 2) going clockwise
    val clockwiseAngle: Double
        get() {
            return when {
                baseAngle.isInRange(-PI, 0) -> -baseAngle
                baseAngle.isInRange(0, PI) -> (PI - baseAngle) + PI
                else -> error("invalid baseAngle")
            }
        }
}

fun findBestMonitoringLocation(rows: List<String>): MonitoringLocation {
    val asteroids = sequence {
        for ((y, rowString) in rows.withIndex()) {
            for ((x, char) in rowString.withIndex()) {
                if (char == '#') yield(Vector(x, y))
            }
        }
    }

    fun getDetections(center: Vector) = sequence {
        for (other in asteroids) {
            if (other == center) continue

            val (relativeX, relativeY) = center - other
            val distance = center.distanceTo(other)
            val angle = atan2(relativeX.toDouble(), relativeY.toDouble())
            yield(AsteroidDetection(other, angle, distance))
        }
    }

    return asteroids
        .map { point -> MonitoringLocation(point, getDetections(point).toList()) }
        .maxBy { it.visibleDetectionCount() }
        ?: error("there are no asteroids?")
}

private val puzzleInput = File("Day10Input.txt").readLines()

private val testInputs = listOf(
    // i just copy/pasted all of these and I'm too lazy to format them "nicely"
    ".#..#\n" +
            ".....\n" +
            "#####\n" +
            "....#\n" +
            "...##",

    "......#.#.\n" +
            "#..#.#....\n" +
            "..#######.\n" +
            ".#.#.###..\n" +
            ".#..#.....\n" +
            "..#....#.#\n" +
            "#..#....#.\n" +
            ".##.#..###\n" +
            "##...#..#.\n" +
            ".#....####",

    "#.#...#.#.\n" +
            ".###....#.\n" +
            ".#....#...\n" +
            "##.#.#.#.#\n" +
            "....#.#.#.\n" +
            ".##..###.#\n" +
            "..#...##..\n" +
            "..##....##\n" +
            "......#...\n" +
            ".####.###.",

    ".#..#..###\n" +
            "####.###.#\n" +
            "....###.#.\n" +
            "..###.##.#\n" +
            "##.##.#.#.\n" +
            "....###..#\n" +
            "..#.#..#.#\n" +
            "#..#.#.###\n" +
            ".##...##.#\n" +
            ".....#.#..",

    ".#..##.###...#######\n" +
            "##.############..##.\n" +
            ".#.######.########.#\n" +
            ".###.#######.####.#.\n" +
            "#####.##.#.##.###.##\n" +
            "..#####..#.#########\n" +
            "####################\n" +
            "#.####....###.#.#.##\n" +
            "##.#################\n" +
            "#####.##.###..####..\n" +
            "..######..##.#######\n" +
            "####.##.####...##..#\n" +
            ".#####..#.######.###\n" +
            "##...#.##########...\n" +
            "#.##########.#######\n" +
            ".####.#.###.###.#.##\n" +
            "....##.##.###..#####\n" +
            ".#.#.###########.###\n" +
            "#.#.#.#####.####.###\n" +
            "###.##.####.##.#..##"
).map { it.split("\n") }