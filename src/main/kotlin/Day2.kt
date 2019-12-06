private fun runProgramWithNounAndVerb(programString: String, noun: Int, verb: Int): IntcodeProgram {
    val program = IntcodeProgram(programString)
    program.setValue(1, noun.toString())
    program.setValue(1, verb.toString())
    program.run()
    return program
}

private const val input =
    "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,10,1,19,1,19,9,23,1,23,6,27,1,9,27,31,1,31,10,35,2,13,35,39,1,39,10,43,1,43,9,47,1,47,13,51,1,51,13,55,2,55,6,59,1,59,5,63,2,10,63,67,1,67,9,71,1,71,13,75,1,6,75,79,1,10,79,83,2,9,83,87,1,87,5,91,2,91,9,95,1,6,95,99,1,99,5,103,2,103,10,107,1,107,6,111,2,9,111,115,2,9,115,119,2,13,119,123,1,123,9,127,1,5,127,131,1,131,2,135,1,135,6,0,99,2,0,14,0"

private fun restoreGravityAssistProgram() {
    val result = runProgramWithNounAndVerb(input, 12, 2)
    println("restore gravity assist: ${result.values}")
}

private fun reverseOutput() {
    val nounVerbPermutations = getPermutations(0..99, 0..99)

    // TODO: this is broke, fix it
    val (noun, verb) = nounVerbPermutations
        .find { (noun, verb) ->
            runProgramWithNounAndVerb(input, noun, verb).values.first() == "19690720"
        }
        ?: throw Error("could not find noun and verb for given result")

    println("reversed output: ${100 * noun + verb}")
}

fun main() {
    restoreGravityAssistProgram()
    reverseOutput()
}


