private const val puzzleInput =
    "3,8,1001,8,10,8,105,1,0,0,21,34,59,68,89,102,183,264,345,426,99999,3,9,102,5,9,9,1001,9,5,9,4,9,99,3,9,101,3,9,9,1002,9,5,9,101,5,9,9,1002,9,3,9,1001,9,5,9,4,9,99,3,9,101,5,9,9,4,9,99,3,9,102,4,9,9,101,3,9,9,102,5,9,9,101,4,9,9,4,9,99,3,9,1002,9,5,9,1001,9,2,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,99,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,101,2,9,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,99"

// for part 1
private const val testInput1 = "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0"
private const val testInput2 = "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0"

// for part 2
private const val testInput3 = "3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5"
private const val testInput4 =
    "3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10"

private fun showLargestThrusterOutput() {
    val currentProgram = puzzleInput

    fun getThrusterOutput(phaseSettings: List<Int>): Int {
        var lastOutput = 0
        for (setting in phaseSettings) {
            val program = IntcodeProgram
                .fromString(currentProgram)
                .addInput(setting, lastOutput)
                .run()

            lastOutput = program.outputs.last().toInt()
        }
        return lastOutput
    }

    val highestThrusterOutput = (0..4).toList()
        .permutations()
        .map(::getThrusterOutput)
        .max()

    println("largest thruster output: $highestThrusterOutput")
}

private fun showLargestThrusterOutputWithFeedbackLoop() {
    val currentProgram = puzzleInput
    val phaseSettingPermutations = getUniqueFiveLengthPermutations(5, 9)

    fun runThrusters(phaseSettings: List<Int>): Int {
        val programs = phaseSettings
            .map { setting -> IntcodeProgram.fromString(currentProgram).addInput(setting).run() }
            .toMutableList()

        var lastOutput = 0

        while (true) {
            programs.replaceAll { program ->
                val newProgram = program.addInput(lastOutput).run()
                lastOutput = newProgram.outputs.last().toInt()
                newProgram
            }

            if (programs.any { it.isStopped }) break
        }

        return lastOutput
    }

    val maxFeedbackThrusterOutput = phaseSettingPermutations.map(::runThrusters).max()
    println("max thruster output with feedback loop: $maxFeedbackThrusterOutput")
}

fun main() {
    showLargestThrusterOutput()
    showLargestThrusterOutputWithFeedbackLoop()
}