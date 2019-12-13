package math

import kotlin.math.pow
import kotlin.math.sqrt

data class Point(val x: Int, val y: Int, val z: Int = 0) {
    constructor(pair: Pair<Int, Int>) : this(pair.first, pair.second)

    operator fun plus(point: Point) = Point(x + point.x, y + point.y, z + point.z)
    operator fun minus(point: Point) = Point(x - point.x, y - point.y, z - point.z)
    operator fun unaryMinus() = Point(-x, -y, -z)

    // TODO: account for Z if we need it
    fun distanceTo(other: Point): Double =
        sqrt((x - other.x).toDouble().pow(2) + (y - other.y).toDouble().pow(2))
}