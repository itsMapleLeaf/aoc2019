package math

import kotlin.math.pow
import kotlin.math.sqrt

data class Point(val x: Int, val y: Int) {
    constructor(pair: Pair<Int, Int>) : this(pair.first, pair.second)

    operator fun plus(point: Point) = Point(x + point.x, y + point.y)
    operator fun minus(point: Point) = Point(x - point.x, y - point.y)
    operator fun unaryMinus() = Point(-x, -y)

    fun distanceTo(other: Point): Double =
        sqrt((x - other.x).toDouble().pow(2) + (y - other.y).toDouble().pow(2))

    fun toPair() = Pair(x, y)
}