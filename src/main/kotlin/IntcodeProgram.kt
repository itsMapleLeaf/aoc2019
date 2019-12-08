private sealed class Instruction(val steps: Int) {
    data class Add(val left: Int, val right: Int, val resultIndex: Int) : Instruction(4)
    data class Multiply(val left: Int, val right: Int, val resultIndex: Int) : Instruction(4)
    data class Input(val inputIndex: Int) : Instruction(2)
    data class Output(val output: Int) : Instruction(2)
    data class JumpIfTrue(val value: Int, val destination: Int) : Instruction(3)
    data class JumpIfFalse(val value: Int, val destination: Int) : Instruction(3)
    data class LessThan(val left: Int, val right: Int, val resultIndex: Int) : Instruction(4)
    data class Equals(val left: Int, val right: Int, val resultIndex: Int) : Instruction(4)
    object Stop : Instruction(0)
}

internal enum class RunState {
    Running,
    InputNeeded,
    Stopped,
}

internal data class IntcodeProgram(
    val values: List<Int>,
    val position: Int = 0,
    val runState: RunState = RunState.Running,
    val inputs: List<Int> = emptyList(),
    val outputs: List<Int> = emptyList()
) {
    companion object {
        fun fromString(programString: String) =
            IntcodeProgram(programString.split(",").map(String::toInt))

        fun fromNumbers(vararg numbers: Int) =
            IntcodeProgram(numbers.toList())
    }

    internal fun setValue(index: Int, value: Int) = copy(values = values.replace(value, index))

    internal fun addInputs(vararg inputs: Int) = copy(inputs = this.inputs + inputs.toList())

    internal fun run(): IntcodeProgram {
        val next = nextState()
        return if (next.runState == RunState.Running) next.run() else next
    }

    private fun addOutput(output: Int) = copy(outputs = outputs + output)
    private fun consumeInput() = copy(inputs = inputs.drop(1))

    private fun setPosition(position: Int) = copy(position = position)
    private fun advance(count: Int) = setPosition(position + count)

    private fun setRunState(runState: RunState) = copy(runState = runState)

    private fun currentInstruction(): Instruction {
        val currentValue = values[position]
        val instructionCode = currentValue % 100
        val paramModes = currentValue.digits().dropLast(2).reversed()

        fun directParam(offset: Int) = values[position + offset + 1]

        fun param(offset: Int) = when (paramModes.getOrNull(offset)) {
            1 -> directParam(offset)
            else -> values[directParam(offset)]
        }

        return when (instructionCode) {
            1 -> Instruction.Add(param(0), param(1), directParam(2))
            2 -> Instruction.Multiply(param(0), param(1), directParam(2))
            3 -> Instruction.Input(directParam(0))
            4 -> Instruction.Output(param(0))
            5 -> Instruction.JumpIfTrue(param(0), param(1))
            6 -> Instruction.JumpIfFalse(param(0), param(1))
            7 -> Instruction.LessThan(param(0), param(1), directParam(2))
            8 -> Instruction.Equals(param(0), param(1), directParam(2))
            99 -> Instruction.Stop
            else -> error("unknown instruction code $instructionCode")
        }
    }

    private fun nextState(): IntcodeProgram {
        return when (val instruction = currentInstruction()) {
            is Instruction.Stop ->
                setRunState(RunState.Stopped)

            is Instruction.Add -> with(instruction) {
                setValue(resultIndex, left + right).advance(steps)
            }

            is Instruction.Multiply -> with(instruction) {
                setValue(resultIndex, left * right).advance(steps)
            }

            is Instruction.Input -> {
                val input = inputs.firstOrNull() ?: return setRunState(RunState.InputNeeded)
                with(instruction) {
                    setValue(inputIndex, input).consumeInput().advance(steps).setRunState(RunState.Running)
                }
            }

            is Instruction.Output -> with(instruction) {
                addOutput(output).advance(steps)
            }

            is Instruction.JumpIfTrue -> with(instruction) {
                if (value != 0) setPosition(destination) else advance(steps)
            }

            is Instruction.JumpIfFalse -> with(instruction) {
                if (value == 0) setPosition(destination) else advance(steps)
            }

            is Instruction.LessThan -> with(instruction) {
                setValue(resultIndex, if (left < right) 1 else 0).advance(steps)
            }

            is Instruction.Equals -> with(instruction) {
                setValue(resultIndex, if (left == right) 1 else 0).advance(steps)
            }
        }
    }
}