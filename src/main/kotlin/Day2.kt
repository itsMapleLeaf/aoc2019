private data class IntcodeProgram(internal val values: List<Int>, private val position: Int) {
    companion object {
        fun fromString(programString: String): IntcodeProgram {
            val values = programString.split(',').map { it.toInt() }
            return IntcodeProgram(values, 0)
        }
    }

    fun withValue(value: Int, index: Int) =
        IntcodeProgram(values.replace(value, index), position)

    val nextProgram
        get() =
            when (val currentValue = values[position]) {
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

private fun runProgram(programString: String, noun: Int, verb: Int): Int {
    val initialProgram = IntcodeProgram.fromString(programString)
        .withValue(noun, 1)
        .withValue(verb, 2)

    tailrec fun getNextProgramRecursive(program: IntcodeProgram): IntcodeProgram {
        val next = program.nextProgram
        return if (next == null) return program else getNextProgramRecursive(next)
    }

    val program = getNextProgramRecursive(initialProgram)
    return program.values.first()
}

private const val input =
    "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,10,1,19,1,19,9,23,1,23,6,27,1,9,27,31,1,31,10,35,2,13,35,39,1,39,10,43,1,43,9,47,1,47,13,51,1,51,13,55,2,55,6,59,1,59,5,63,2,10,63,67,1,67,9,71,1,71,13,75,1,6,75,79,1,10,79,83,2,9,83,87,1,87,5,91,2,91,9,95,1,6,95,99,1,99,5,103,2,103,10,107,1,107,6,111,2,9,111,115,2,9,115,119,2,13,119,123,1,123,9,127,1,5,127,131,1,131,2,135,1,135,6,0,99,2,0,14,0"

private fun restoreGravityAssistProgram() {
    val result = runProgram(input, 12, 2)
    println("restore gravity assist: $result")
}

private fun reverseOutput() {
    for (noun in 0..99) {
        for (verb in 0..99) {
            val result = runProgram(input, noun, verb)
            if (result == 19690720) {
                println("reversed output: ${100 * noun + verb}")
                return
            }
        }
    }
}

fun main() {
    restoreGravityAssistProgram()
    reverseOutput()
}


