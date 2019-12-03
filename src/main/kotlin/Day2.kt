private class Operator(private val getResult: (Int, Int) -> Int) {
    fun run(values: MutableList<Int>, headPosition: Int) {
        val firstSourcePosition = values[headPosition + 1]
        val secondSourcePosition = values[headPosition + 2]
        val destinationPosition = values[headPosition + 3]
        values[destinationPosition] = getResult(values[firstSourcePosition], values[secondSourcePosition])
    }
}

private val operators = mapOf(
    1 to Operator { a, b -> a + b },
    2 to Operator { a, b -> a * b }
)

private fun runProgram(programString: String, noun: Int, verb: Int): Int {
    val values = programString
        .split(',')
        .map { it.toInt() }
        .toMutableList()

    values[1] = noun
    values[2] = verb

    var position = 0

    while (true) {
        when (val currentValue = values[position]) {
            in operators.keys -> {
                operators[currentValue]?.run(values, position)
                position += 4
            }

            99 ->
                return values[0]

            else ->
                throw Error("what the fuck")
        }
    }
}

private const val program =
    "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,10,1,19,1,19,9,23,1,23,6,27,1,9,27,31,1,31,10,35,2,13,35,39,1,39,10,43,1,43,9,47,1,47,13,51,1,51,13,55,2,55,6,59,1,59,5,63,2,10,63,67,1,67,9,71,1,71,13,75,1,6,75,79,1,10,79,83,2,9,83,87,1,87,5,91,2,91,9,95,1,6,95,99,1,99,5,103,2,103,10,107,1,107,6,111,2,9,111,115,2,9,115,119,2,13,119,123,1,123,9,127,1,5,127,131,1,131,2,135,1,135,6,0,99,2,0,14,0"

private fun restoreGravityAssistProgram() {
    val result = runProgram(program, 12, 2)
    println("restore gravity assist: $result")
}

private fun reverseOutput() {
    for (noun in 0..99) {
        for (verb in 0..99) {
            val result = runProgram(program, noun, verb)
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


