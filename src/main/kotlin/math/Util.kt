package math

fun delta(value: Number, a: Number, b: Number): Double =
    value.toDouble() / (a.toDouble() + (b.toDouble() - a.toDouble()))

fun Number.isBetween(start: Number, end: Number) =
    start.toDouble() <= this.toDouble() && this.toDouble() <= end.toDouble()