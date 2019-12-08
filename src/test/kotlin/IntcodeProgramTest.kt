import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class IntcodeProgramTest : StringSpec({
    "nextState - input instruction" {
        val programWithInputs = IntcodeProgramFunctional
            .fromNumbers(3, 1, 99)
            .addInputs(42)

        programWithInputs.inputs shouldBe listOf("42")

        val programAfterConsumedInputs = programWithInputs.run()

        programAfterConsumedInputs.inputs shouldBe emptyList()
    }

    "consumes input if state is at input needed" {
        val program = IntcodeProgramFunctional
            .fromNumbers(3, 1, 3, 3, 99)
            .addInputs(42)

        val programWithOneConsumedInput = program.run()

        programWithOneConsumedInput.runState shouldBe RunState.InputNeeded

        val resumedProgram = programWithOneConsumedInput.addInputs(42).run()

        resumedProgram.inputs shouldBe emptyList()
        resumedProgram.runState shouldBe RunState.Stopped
    }
})