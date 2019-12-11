package math

fun lerp(a: Number, b: Number, delta: Number): Double =
    a.toDouble() + (b.toDouble() - a.toDouble()) * delta.toDouble()

fun delta(value: Number, a: Number, b: Number): Double =
    (value.toDouble() - a.toDouble()) / (b.toDouble() - a.toDouble())

fun Number.isInRange(start: Number, end: Number) =
    start.toDouble() <= this.toDouble() && this.toDouble() <= end.toDouble()

// x = a + (b - a) * d
// x - a = d * (b - a)
// (x - a) / (b - a) = d