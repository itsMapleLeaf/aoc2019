package math

data class Point(val x: Int, val y: Int) {
    operator fun plus(point: Point) = Point(x + point.x, y + point.y)
    operator fun minus(point: Point) = Point(x - point.x, y - point.y)
    operator fun unaryMinus() = Point(-x, -y)

    companion object {
        fun fromPair(pair: Pair<Int, Int>) = Point(pair.first, pair.second)
    }
}