import math.Point
import math.Segment
import java.io.File

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
        var count = 0

        for (other in asteroids) {
            if (other == center) continue

            val lineOfSight = Segment(center, other)
            val hasAsteroidBetween = asteroids.any { it != center && it != other && lineOfSight.touches(it) }

            if (!hasAsteroidBetween) count += 1
        }

        return count
    }

    val detectionCounts = asteroids.map(::getDetectionCount)
    return detectionCounts.max()
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