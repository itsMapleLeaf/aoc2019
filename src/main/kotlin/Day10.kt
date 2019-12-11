import math.Point
import java.io.File
import kotlin.math.atan2

fun main() {
    println(detectionCountOfBestMonitoringLocation(puzzleInput))
}

fun detectionCountOfBestMonitoringLocation(rows: List<String>): Int? {
    val asteroids = mutableSetOf<Point>()

    for ((y, rowString) in rows.withIndex()) {
        for ((x, char) in rowString.withIndex()) {
            if (char == '#') {
                asteroids.add(Point(x, y))
            }
        }
    }

    fun getDetectionCount(center: Point): Int {
        val angles = mutableSetOf<Double>()

        for (other in asteroids) {
            if (other == center) continue

            val (relativeX, relativeY) = center - other
            angles += atan2(relativeX.toDouble(), relativeY.toDouble())
        }

        return angles.size
    }

    return asteroids.map(::getDetectionCount).max()
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