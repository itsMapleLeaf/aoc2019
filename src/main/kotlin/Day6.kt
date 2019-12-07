import java.io.File

private class Planet(private val name: String) {
    internal var parent: Planet? = null

    override fun toString() = name

    internal fun getOrbitCount(): Int =
        if (parent == null) 0 else 1 + parent!!.getOrbitCount()

    internal fun getPathToCenter(): List<Planet> =
        listOf(this) + if (parent == null)
            listOf()
        else
            parent!!.getPathToCenter()
}

private fun getInput() =
    File("PlanetChart.txt").readLines()

private fun getTestInput() =
    "COM)B B)C C)D D)E E)F B)G G)H D)I E)J J)K K)L K)YOU I)SAN".split(" ")

private fun constructPlanetMap(): Map<String, Planet> {
    val namePairs = getInput().map { it.split(")").toPair() }
    val planetMap = mutableMapOf<String, Planet>()

    for ((parentName, childName) in namePairs) {
        val parent = planetMap[parentName] ?: Planet(parentName)
        val child = planetMap[childName] ?: Planet(childName)
        child.parent = parent
        planetMap[parentName] = parent
        planetMap[childName] = child
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
    val sanToCenter = planetMap.getValue("SAN").getPathToCenter().reversed()

    val sharedCount = youToCenter.indices
        .takeWhile { index -> youToCenter[index] == sanToCenter[index] }
        .size

    val uniquePaths = youToCenter.drop(sharedCount) + sanToCenter.drop(sharedCount)
    val totalDistance = uniquePaths.size - 2

    println("minimum orbital transfers to santa: $totalDistance")
}

fun main() {
    getTotalOrbits()
    getTotalOrbitalTransfersToSanta()
}