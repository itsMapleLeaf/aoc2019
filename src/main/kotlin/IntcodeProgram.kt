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

internal class IntcodeProgram(programString: String) {
    var inputList = mutableListOf<String>()
    val values = programString.split(",").toMutableList()
    var position = 0
    val outputs = mutableListOf<Int>()

    fun setValue(index: Int, value: String) {
        values[index] = value
    }

    fun setInputs(inputs: Iterable<String>) {
        inputList = inputs.toMutableList()
    }

    fun run() {
        fun getParam(offset: Int) =
            values[position + offset + 1].toInt()

        fun getPositionParam(offset: Int) =
            values[getParam(offset)].toInt()

        loop@ while (true) {
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
                Instruction.Stop ->
                    break@loop

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
                    val input = inputList.removeAt(inputList.size - 1)
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
        }
    }
}
