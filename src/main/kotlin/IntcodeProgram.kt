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

internal enum class StepResult {
    Continue,
    Stop,
}

internal class IntcodeProgram(programString: String) {
    internal val values = programString.split(",").toMutableList()
    internal val outputs = mutableListOf<Int>()
    private val inputs = mutableListOf<String>()
    private var position = 0

    internal fun setValue(index: Int, value: String) {
        values[index] = value
    }

    internal fun setInputs(vararg inputs: String) {
        this.inputs.clear()
        this.inputs.addAll(inputs)
    }

    internal fun setInputs(vararg inputs: Int) {
        setInputs(*inputs.map { it.toString() }.toTypedArray())
    }

    private fun step(): StepResult {
        fun getParam(offset: Int) =
            values[position + offset + 1].toInt()

        fun getPositionParam(offset: Int) =
            values[getParam(offset)].toInt()

        val currentValue = values[position]

        val instruction = when (val num = currentValue.takeLast(2).toInt()) {
            1 -> Instruction.Add
            2 -> Instruction.Multiply
            3 -> Instruction.Input
            4 -> Instruction.Output
            5 -> Instruction.JumpIfTrue
            6 -> Instruction.JumpIfFalse
            7 -> Instruction.LessThan
            8 -> Instruction.Equals
            99 -> Instruction.Stop
            else -> throw Error("unknown instruction number $num")
        }

        val paramModes = currentValue.dropLast(2).reversed().map {
            when (it) {
                '0' -> ParamMode.Position
                '1' -> ParamMode.Immediate
                else -> throw Error("unknown param mode number $it")
            }
        }

        fun getParamMode(offset: Int) =
            paramModes.getOrNull(offset) ?: ParamMode.Position

        fun getParamWithMode(offset: Int) = when (getParamMode(offset)) {
            ParamMode.Position -> getPositionParam(offset)
            ParamMode.Immediate -> getParam(offset)
        }

        when (instruction) {
            Instruction.Stop -> {
                return StepResult.Stop
            }

            Instruction.Add, Instruction.Multiply -> {
                val first = getParamWithMode(0)
                val second = getParamWithMode(1)
                val resultIndex = getParam(2)

                val result = when (instruction) {
                    Instruction.Add -> first + second
                    Instruction.Multiply -> first * second
                    else -> throw Error("kotlin please get better type narrowing")
                }

                values[resultIndex] = result.toString()
                position += 4
            }

            Instruction.Input -> {
                val input = inputs.removeAt(0)
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

        return StepResult.Continue
    }

    internal fun run() {
        do {
            val result = step()
        } while (result != StepResult.Stop)
    }
}
