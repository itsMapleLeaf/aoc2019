internal data class IntcodeProgram(
    private val values: List<Int>,
    private val position: Int,
    private val input: Iterator<Int>,
    internal val output: List<Int>
) {
    companion object {
        fun fromString(programString: String): IntcodeProgram {
            return IntcodeProgram(
                values = programString.split(',').map { it.toInt() },
                position = 0,
                input = IntegerInputIterator(),
                output = listOf()
            )
        }
    }

    fun withValue(value: Int, index: Int) =
        IntcodeProgram(values.replace(value, index), position, input, output)

    fun withInput(newInput: Iterator<Int>) =
        IntcodeProgram(values, position, newInput, output)

    private fun move(steps: Int) =
        IntcodeProgram(values, position + steps, input, output)

    private fun withOutput(newOutput: Int) =
        IntcodeProgram(values, position, input, output + newOutput)

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

                withValue(result, destinationIndex).move(4)
            }

            3 -> {
                val newValue = input.next()
                val destinationIndex = values[position + 1]
                withValue(newValue, destinationIndex).move(2)
            }

            4 -> {
                val outputIndex = values[position + 1]
                withOutput(values[outputIndex]).move(2)
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

private class IntegerInputIterator : Iterator<Int> {
    override fun hasNext() = true
    override fun next() = getIntegerInput()
}

private tailrec fun getIntegerInput(): Int {
    print("enter an integer: ")

    try {
        val input = readLine() ?: throw Error("no input found?")
        return input.toInt()
    } catch (error: NumberFormatException) {
        println("not a valid integer >:(")
    } catch (error: Error) {
        println("an error occurred: ${error.message}")
    }

    return getIntegerInput()
}
