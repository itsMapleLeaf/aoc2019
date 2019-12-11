package math

data class Segment(val start: Point, val end: Point) {
    fun touches(point: Point): Boolean {
        val deltaX = delta(point.x, start.x, end.x)
        val deltaY = delta(point.y, start.y, end.y)

        return when {
            start.x == end.x -> point.x == start.x && deltaY.isInRange(0, 1)
            start.y == end.y -> point.y == start.y && deltaX.isInRange(0, 1)
            else -> deltaX.isInRange(0, 1) && deltaY.isInRange(0, 1) && deltaX == deltaY
        }
    }
}
