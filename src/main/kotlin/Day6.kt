import java.io.File

private class Planet(private val name: String) {
    internal var parent: Planet? = null

    override fun toString() = "Planet($name)"

    internal fun getOrbitCount(): Int {
        val parent = this.parent ?: return 0
        return 1 + parent.getOrbitCount()
    }

    internal fun getPathToCenter(): List<Planet> {
        val parent = this.parent ?: return emptyList()
        return listOf(this) + parent.getPathToCenter()
    }
}

private fun getInput() =
    File("PlanetChart.txt").readLines()

private fun getTestInput() =
    "COM)B B)C C)D D)E E)F B)G G)H D)I E)J J)K K)L K)YOU I)SAN".split(" ")

private fun constructPlanetMap(): Map<String, Planet> {
    val namePairs = getInput().map { it.split(")").toPair() }
    val planetMap = mutableMapOf<String, Planet>()

    for ((parentName, childName) in namePairs) {
        val parent = planetMap.getOrPut(parentName) { Planet(parentName) }
        val child = planetMap.getOrPut(childName) { Planet(childName) }
        child.parent = parent
    }

    return planetMap.toMap()
}

private fun getTotalOrbits() {
    val planetMap = constructPlanetMap()
    val totalOrbits = planetMap.values.map { it.getOrbitCount() }.sum()

    println("total orbits: $totalOrbits")
}

private fun getTotalOrbitalTransfersToSanta() {
    val planetMap = constructPlanetMap()

    val youToCenter = planetMap.getValue("YOU").getPathToCenter().reversed()
    val santaToCenter = planetMap.getValue("SAN").getPathToCenter().reversed()

    println("your path to center: $youToCenter")
    println("santa's path to center: $santaToCenter")

    val sharedCount = youToCenter.indices
        .takeWhile { index -> youToCenter[index] == santaToCenter[index] }
        .size

    val uniquePaths = youToCenter.drop(sharedCount) + santaToCenter.drop(sharedCount)
    val totalDistance = uniquePaths.size - 2

    println("minimum orbital transfers to santa: $totalDistance")
}

fun main() {
    getTotalOrbits()
    getTotalOrbitalTransfersToSanta()
}
