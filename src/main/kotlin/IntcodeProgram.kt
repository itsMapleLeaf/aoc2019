private sealed class Instruction(val steps: Int) {
    data class Add(val left: Long, val right: Long, val resultIndex: Int) : Instruction(4)
    data class Multiply(val left: Long, val right: Long, val resultIndex: Int) : Instruction(4)
    data class Input(val inputIndex: Int) : Instruction(2)
    data class Output(val output: Long) : Instruction(2)
    data class JumpIfTrue(val value: Long, val destination: Int) : Instruction(3)
    data class JumpIfFalse(val value: Long, val destination: Int) : Instruction(3)
    data class LessThan(val left: Long, val right: Long, val resultIndex: Int) : Instruction(4)
    data class Equals(val left: Long, val right: Long, val resultIndex: Int) : Instruction(4)
    data class SetRelativeModeOffset(val offset: Int) : Instruction(2)
    object Stop : Instruction(0)
}

private enum class ParamMode {
    Position,
    Immediate,
    Relative,
}

internal enum class RunState {
    Running,
    InputNeeded,
    Stopped,
}

internal data class IntcodeProgram(
    val values: Map<Int, Long>, // IDEA: wrap this in some IntcodeMemory object
    val position: Int = 0,
    val inputs: List<Int> = emptyList(),
    val outputs: List<Long> = emptyList(),
    private val runState: RunState = RunState.Running,
    private val relativeModeOffset: Int = 0,
    private val debugMode: Boolean = false
) {
    companion object {
        fun fromString(programString: String): IntcodeProgram =
            IntcodeProgram(programString.split(",").map(String::toLong).toIndexValueMap())

        fun fromNumbers(vararg numbers: Int): IntcodeProgram =
            IntcodeProgram(numbers.map { it.toLong() }.toIndexValueMap())
    }

    private fun getValue(index: Int): Long = values.getOrDefault(index, 0)
    internal fun setValue(index: Int, value: Long): IntcodeProgram = copy(values = values + Pair(index, value))

    internal fun addInput(vararg inputs: Int) = copy(inputs = this.inputs + inputs.toList())

    internal fun run(): IntcodeProgram {
        if (debugMode) println(this)

        val next = nextState()
        return if (next.runState == RunState.Running) next.run() else next
    }

    internal val isStopped get() = runState == RunState.Stopped
    internal val needsInput get() = runState == RunState.InputNeeded

    internal fun debug() = copy(debugMode = true)

    private fun addOutput(newOutput: Long) = copy(outputs = outputs + newOutput)
    private fun consumeInput() = copy(inputs = inputs.drop(1))

    private fun setPosition(position: Int) = copy(position = position)
    private fun advance(count: Int) = setPosition(position + count)

    private fun setRunState(runState: RunState) = copy(runState = runState)

    private fun adjustRelativeModeOffset(delta: Int) = copy(relativeModeOffset = relativeModeOffset + delta)

    private fun currentInstruction(): Instruction {
        val currentValue = getValue(position)
        val instructionCode = currentValue % 100

        // IDEA: param parser data class to contain all of this logic?
        val paramModes = currentValue.digits().dropLast(2).reversed().map {
            when (it) {
                0 -> ParamMode.Position
                1 -> ParamMode.Immediate
                2 -> ParamMode.Relative
                else -> error("unknown param mode code $it")
            }
        }

        fun directParam(offset: Int) = getValue(position + offset + 1)

        // represents params which we read from and work with
        fun param(offset: Int): Long = when (paramModes.getOrNull(offset)) {
            ParamMode.Immediate -> directParam(offset)
            ParamMode.Relative -> getValue(relativeModeOffset + directParam(offset).toInt())
            else /* positional or null */ -> getValue(directParam(offset).toInt())
        }

        // to get params that are addresses we write to (e.g. for input, and the result of logical instructions)
        // these params are not affected by immediate mode
        fun writeAddressParam(offset: Int): Int {
            val writeAddress = directParam(offset).toInt()
            return when (paramModes.getOrNull(offset)) {
                // if relative mode, add the offset to the address we're writing to
                ParamMode.Relative -> writeAddress + relativeModeOffset

                // normal param reading mode, this is the address we write to
                else -> writeAddress
            }
        }

        return when (instructionCode.toInt()) {
            1 -> Instruction.Add(param(0), param(1), writeAddressParam(2))
            2 -> Instruction.Multiply(param(0), param(1), writeAddressParam(2))
            3 -> Instruction.Input(writeAddressParam(0))
            4 -> Instruction.Output(param(0))
            5 -> Instruction.JumpIfTrue(param(0), param(1).toInt())
            6 -> Instruction.JumpIfFalse(param(0), param(1).toInt())
            7 -> Instruction.LessThan(param(0), param(1), writeAddressParam(2))
            8 -> Instruction.Equals(param(0), param(1), writeAddressParam(2))
            9 -> Instruction.SetRelativeModeOffset(param(0).toInt())
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
                    setValue(inputIndex, input.toLong()).consumeInput().advance(steps).setRunState(RunState.Running)
                }
            }

            is Instruction.Output -> with(instruction) {
                addOutput(output).advance(steps)
            }

            is Instruction.JumpIfTrue -> with(instruction) {
                if (value != 0L) setPosition(destination) else advance(steps)
            }

            is Instruction.JumpIfFalse -> with(instruction) {
                if (value == 0L) setPosition(destination) else advance(steps)
            }

            is Instruction.LessThan -> with(instruction) {
                setValue(resultIndex, if (left < right) 1 else 0).advance(steps)
            }

            is Instruction.Equals -> with(instruction) {
                setValue(resultIndex, if (left == right) 1 else 0).advance(steps)
            }

            is Instruction.SetRelativeModeOffset -> with(instruction) {
                adjustRelativeModeOffset(offset).advance(steps)
            }
        }
    }
}