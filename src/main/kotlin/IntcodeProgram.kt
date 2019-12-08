private enum class Instruction {
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

private enum class ParamMode {
    Position,
    Immediate,
}

internal enum class RunState {
    Init,
    Continue,
    InputNeeded,
    Stop,
}

private fun String.toInstruction() = when (takeLast(2).toInt()) {
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

private fun String.paramModes() =
    dropLast(2).reversed().map(Char::toParamMode)

internal class IntcodeProgram(programString: String) {
    internal val values = programString.split(",").toMutableList()
    internal val outputs = mutableListOf<Int>()
    private var runState = RunState.Init
    private val inputs = mutableListOf<String>()
    private var position = 0

    internal val isStopped get() = runState == RunState.Stop

    internal fun setValue(index: Int, value: Any) {
        values[index] = value.toString()
    }

    internal fun addInputs(vararg inputs: String) {
        this.inputs.addAll(inputs)
    }

    internal fun addInputs(vararg inputs: Int) {
        addInputs(*inputs.map(Int::toString).toTypedArray())
    }

    private fun getParam(offset: Int) =
        values[position + offset + 1].toInt()

    private fun getPositionParam(offset: Int) =
        values[getParam(offset)].toInt()

    private fun step(): RunState {
        val currentValue = values[position]

        val instruction = currentValue.toInstruction()
        val paramModes = currentValue.paramModes()

        fun getParamMode(offset: Int) =
            paramModes.getOrNull(offset) ?: ParamMode.Position

        fun getParamWithMode(offset: Int) = when (getParamMode(offset)) {
            ParamMode.Position -> getPositionParam(offset)
            ParamMode.Immediate -> getParam(offset)
        }

        when (instruction) {
            Instruction.Stop ->
                return RunState.Stop

            Instruction.Add, Instruction.Multiply -> {
                val first = getParamWithMode(0)
                val second = getParamWithMode(1)
                val resultIndex = getParam(2)

                val result = when (instruction) {
                    Instruction.Add -> first + second
                    Instruction.Multiply -> first * second
                    else -> throw Error("unhandled math instruction $instruction")
                }

                values[resultIndex] = result.toString()
                position += 4
            }

            Instruction.Input -> {
                val input = inputs.popOrNull() ?: return RunState.InputNeeded

                val storedIndex = getParam(0)
                values[storedIndex] = input
                position += 2
            }

            Instruction.Output -> {
                val output = getParamWithMode(0)
                outputs += output
                position += 2
            }

            Instruction.JumpIfTrue -> {
                val value = getParamWithMode(0)
                val destination = getParamWithMode(1)
                if (value != 0) {
                    position = destination
                } else {
                    position += 3
                }
            }

            Instruction.JumpIfFalse -> {
                val value = getParamWithMode(0)
                val destination = getParamWithMode(1)
                if (value == 0) {
                    position = destination
                } else {
                    position += 3
                }
            }

            Instruction.LessThan -> {
                val first = getParamWithMode(0)
                val second = getParamWithMode(1)
                val destinationIndex = getParam(2)

                values[destinationIndex] = if (first < second) "1" else "0"
                position += 4
            }

            Instruction.Equals -> {
                val first = getParamWithMode(0)
                val second = getParamWithMode(1)
                val destinationIndex = getParam(2)

                values[destinationIndex] = if (first == second) "1" else "0"
                position += 4
            }
        }

        return RunState.Continue
    }

    internal fun run() {
        do {
            runState = step()
        } while (runState == RunState.Continue)
    }
}
