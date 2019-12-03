import java.io.File
import kotlin.math.max

private fun getModuleMasses() =
    File("ModuleMasses.txt").readLines().map { it.toInt() }

private fun getRequiredBaseFuelForModule(mass: Int) =
    (mass / 3) - 2

private fun getDescendingFuelForModule(mass: Int): Int {
    if (mass <= 0) { return 0 }

    val base = max(getRequiredBaseFuelForModule(mass), 0)
    return base + getDescendingFuelForModule(base)
}

fun main() {
    val baseFuel = getModuleMasses().map(::getRequiredBaseFuelForModule).sum()
    val descendedFuel = getModuleMasses().map(::getDescendingFuelForModule).sum()

    println("base fuel: $baseFuel")
    println("descended fuel: $descendedFuel")
}
