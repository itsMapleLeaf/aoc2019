package math

data class Segment(val start: Vector, val end: Vector) {
    fun touches(vector: Vector): Boolean {
        val deltaX = delta(vector.x, start.x, end.x)
        val deltaY = delta(vector.y, start.y, end.y)

        return when {
            start.x == end.x -> vector.x == start.x && deltaY.isInRange(0, 1)
            start.y == end.y -> vector.y == start.y && deltaX.isInRange(0, 1)
            else -> deltaX.isInRange(0, 1) && deltaY.isInRange(0, 1) && deltaX == deltaY
        }
    }
}
