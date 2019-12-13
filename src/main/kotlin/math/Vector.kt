package math

import kotlin.math.pow
import kotlin.math.sqrt

data class Vector(val x: Int, val y: Int, val z: Int = 0) {
    constructor(pair: Pair<Int, Int>) : this(pair.first, pair.second)

    operator fun plus(vector: Vector) = Vector(x + vector.x, y + vector.y, z + vector.z)
    operator fun minus(vector: Vector) = Vector(x - vector.x, y - vector.y, z - vector.z)
    operator fun unaryMinus() = Vector(-x, -y, -z)

    // TODO: account for Z if we need it
    fun distanceTo(other: Vector): Double =
        sqrt((x - other.x).toDouble().pow(2) + (y - other.y).toDouble().pow(2))
}