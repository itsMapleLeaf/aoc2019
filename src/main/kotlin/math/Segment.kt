package math

data class Segment(val start: Point, val end: Point) {
    fun touches(point: Point): Boolean {
        val deltaX = delta(point.x, start.x, end.x)
        val deltaY = delta(point.y, start.y, end.y)
        return deltaX.isBetween(0, 1) && deltaY.isBetween(0, 1) && deltaX == deltaY
    }
}
