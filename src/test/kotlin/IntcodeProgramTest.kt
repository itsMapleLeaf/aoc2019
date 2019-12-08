import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class IntcodeProgramTest : StringSpec({
    "add and multiply (immediate mode)" {
        val program = IntcodeProgram
            .fromNumbers(1101, 2, 3, 3, 1102, 2, 3, 7, 99)
            .run()

        program.values shouldBe listOf(1101, 2, 3, 5, 1102, 2, 3, 6, 99).map { it.toString() }
    }

    "nextState - input instruction" {
        val programWithInputs = IntcodeProgram
            .fromNumbers(3, 1, 99)
            .addInputs(42)

        programWithInputs.inputs shouldBe listOf("42")

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
})