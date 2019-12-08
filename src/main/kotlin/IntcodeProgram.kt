internal enum class Instruction {
    Add,
    Multiply,
    Input,
    Output,
    JumpIfTrue,
    JumpIfFalse,
    LessThan,
    Equals,
    Stop,
}

internal enum class ParamMode {
    Position,
    Immediate,
}

internal enum class RunState {
    Running,
    InputNeeded,
    Stopped,
}

internal fun String.toInstruction() = when (takeLast(2).toInt()) {
    1 -> Instruction.Add
    2 -> Instruction.Multiply
    3 -> Instruction.Input
    4 -> Instruction.Output
    5 -> Instruction.JumpIfTrue
    6 -> Instruction.JumpIfFalse
    7 -> Instruction.LessThan
    8 -> Instruction.Equals
    99 -> Instruction.Stop
    else -> throw Error("unknown instruction number \"$this\"")
}

private fun Char.toParamMode() = when (this) {
    '0' -> ParamMode.Position
    '1' -> ParamMode.Immediate
    else -> throw Error("unknown param mode number \"$this\"")
}

internal fun String.paramModes() =
    dropLast(2).reversed().map(Char::toParamMode)

internal data class IntcodeProgramFunctional(
    val values: List<String>,
    val position: Int = 0,
    val runState: RunState = RunState.Running,
    val inputs: List<String> = emptyList(),
    val outputs: List<Int> = emptyList()
) {
    companion object {
        fun fromString(programString: String) =
            IntcodeProgramFunctional(programString.split(","))

        fun fromNumbers(vararg numbers: Int) =
            IntcodeProgramFunctional(numbers.map(Int::toString).toList())
    }

    internal fun setValue(index: Int, value: String) = copy(values = values.replace(value, index))
    internal fun setValue(index: Int, value: Int) = setValue(index, value.toString())

    private fun addInputs(vararg inputs: String) = copy(inputs = inputs.toList())
    internal fun addInputs(vararg inputs: Int) = addInputs(*inputs.map { it.toString() }.toTypedArray())

    internal fun run(): IntcodeProgramFunctional {
        val next = nextState()
        return when (next.runState) {
            RunState.Running -> next.run()
            else -> next
        }
    }

    private fun addOutput(output: Int) = copy(outputs = outputs + output)
    private fun consumeInput() = copy(inputs = inputs.drop(1))

    private fun setPosition(position: Int) = copy(position = position)
    private fun advance(count: Int) = setPosition(position + count)

    private fun setRunState(runState: RunState) = copy(runState = runState)

    private fun currentValue() = values[position]

    private fun currentInstruction() = currentValue().toInstruction()
    private fun currentParamModes() = currentValue().paramModes()

    private fun getParam(offset: Int) = values[position + offset + 1].toInt()
    private fun getPositionParam(offset: Int) = values[getParam(offset)].toInt()

    private fun getParamMode(offset: Int) = currentParamModes().getOrNull(offset) ?: ParamMode.Position

    private fun getParamWithMode(offset: Int) = when (getParamMode(offset)) {
        ParamMode.Position -> getPositionParam(offset)
        ParamMode.Immediate -> getParam(offset)
    }

    private fun nextState(): IntcodeProgramFunctional =
        when (val instruction = currentInstruction()) {
            Instruction.Stop ->
                setRunState(RunState.Stopped)

            Instruction.Add, Instruction.Multiply -> {
                val first = getParamWithMode(0)
                val second = getParamWithMode(1)
                val resultIndex = getParam(2)

                val result = when (instruction) {
                    Instruction.Add -> first + second
                    Instruction.Multiply -> first * second
                    else -> throw Error("unhandled math instruction $instruction")
                }

                setValue(resultIndex, result).advance(4)
            }

            Instruction.Input -> {
                when (val input = inputs.firstOrNull()) {
                    null ->
                        setRunState(RunState.InputNeeded)
                    else -> {
                        val storedIndex = getParam(0)
                        setValue(storedIndex, input).consumeInput().advance(2).setRunState(RunState.Running)
                    }
                }
            }

            Instruction.Output -> {
                val output = getParamWithMode(0)
                addOutput(output).advance(2)
            }

            Instruction.JumpIfTrue -> {
                val value = getParamWithMode(0)
                val destination = getParamWithMode(1)
                if (value != 0) {
                    setPosition(destination)
                } else {
                    advance(3)
                }
            }

            Instruction.JumpIfFalse -> {
                val value = getParamWithMode(0)
                val destination = getParamWithMode(1)
                if (value == 0) {
                    setPosition(destination)
                } else {
                    advance(3)
                }
            }

            Instruction.LessThan -> {
                val first = getParamWithMode(0)
                val second = getParamWithMode(1)
                val destinationIndex = getParam(2)

                setValue(destinationIndex, if (first < second) 1 else 0).advance(4)
            }

            Instruction.Equals -> {
                val first = getParamWithMode(0)
                val second = getParamWithMode(1)
                val destinationIndex = getParam(2)

                setValue(destinationIndex, if (first == second) 1 else 0).advance(4)
            }
        }
}