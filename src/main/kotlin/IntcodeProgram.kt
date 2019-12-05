internal data class IntcodeProgram(private val values: List<Int>, private val position: Int) {
    companion object {
        fun fromString(programString: String): IntcodeProgram {
            val values = programString.split(',').map { it.toInt() }
            return IntcodeProgram(values, 0)
        }
    }

    fun withValue(value: Int, index: Int) =
        IntcodeProgram(values.replace(value, index), position)

    private fun getNextProgram(): IntcodeProgram? {
        return when (val currentValue = values[position]) {
            1, 2 -> {
                val firstSourceIndex = values[position + 1]
                val secondSourceIndex = values[position + 2]
                val destinationIndex = values[position + 3]

                val firstValue = values[firstSourceIndex]
                val secondValue = values[secondSourceIndex]

                val result = when (currentValue) {
                    1 -> firstValue + secondValue
                    2 -> firstValue * secondValue
                    else -> throw Error("we broke the laws of physics somehow")
                }

                val nextValues = values.replace(result, destinationIndex)

                IntcodeProgram(nextValues, position + 4)
            }

            99 ->
                null

            else ->
                throw Error("unknown instruction $currentValue at position $position")
        }
    }

    fun run(): IntcodeProgram {
        val next = getNextProgram() ?: return this
        return next.run()
    }

    val firstValue get() = values.first()
}