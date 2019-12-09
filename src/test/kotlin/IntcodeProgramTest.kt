import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class IntcodeProgramTest : StringSpec() {
    init {
        "add and multiply (immediate mode)" {
            val program = IntcodeProgram
                .fromNumbers(1101, 2, 3, 3, 1102, 2, 3, 7, 99)
                .run()

            program.values shouldBe listOf(1101L, 2L, 3L, 5L, 1102L, 2L, 3L, 6L, 99L).toIndexValueMap()
        }

        "nextState - input instruction" {
            val programWithInputs = IntcodeProgram
                .fromNumbers(3, 1, 99)
                .addInputs(42)

            programWithInputs.inputs shouldBe listOf(42)

            val programAfterConsumedInputs = programWithInputs.run()

            programAfterConsumedInputs.inputs shouldBe emptyList()
        }

        "consumes input if state is at input needed" {
            val program = IntcodeProgram
                .fromNumbers(3, 1, 3, 3, 99)
                .addInputs(42)

            val programWithOneConsumedInput = program.run()

            programWithOneConsumedInput.runState shouldBe RunState.InputNeeded

            val resumedProgram = programWithOneConsumedInput.addInputs(42).run()

            resumedProgram.inputs shouldBe emptyList()
            resumedProgram.runState shouldBe RunState.Stopped
        }

        "quine" {
            val programString = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"
            val quine = IntcodeProgram.fromString(programString).run()
            quine.outputs shouldBe programString.split(",").map(String::toLong)
        }

        "sixteen digit number" {
            val sixteenDigitNumber = IntcodeProgram
                .fromString("1102,34915192,34915192,7,4,7,99,0")
                .run()

            sixteenDigitNumber.outputs.first().toString().length shouldBe 16
        }

        "hella big number" {
            val program = IntcodeProgram.fromString("104,1125899906842624,99").run()
            program.outputs.first() shouldBe 1125899906842624L
        }
    }
}