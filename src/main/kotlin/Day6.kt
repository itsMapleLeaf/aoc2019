import java.io.File

private class Planet {
    internal var parent: Planet? = null

    internal fun getOrbitCount(): Int =
        if (parent == null) 0 else 1 + parent!!.getOrbitCount()
}

private fun getInput() =
    File("PlanetChart.txt").readLines()

private fun getTestInput() =
    "COM)B B)C C)D D)E E)F B)G G)H D)I E)J J)K K)L".split(" ")

private fun constructPlanetMap(): Map<String, Planet> {
    val namePairs = getInput().map { it.split(")").toPair() }
    val planetMap = mutableMapOf<String, Planet>()

    for ((parentName, childName) in namePairs) {
        val parent = planetMap[parentName] ?: Planet()
        val child = planetMap[childName] ?: Planet()
        child.parent = parent
        planetMap[parentName] = parent
        planetMap[childName] = child
    }

    return planetMap.toMap()
}

private fun getTotalOrbits() {
    val planetMap = constructPlanetMap()
    val totalOrbits = planetMap.values.map { it.getOrbitCount() }.sum()

    println(totalOrbits)
}

//private fun getTotalOrbitalTransfersToSanta() {
//    val planetMap = constructPlanetMap()
//    println(planetMap["COM"]?.stepsToInChildren(planetMap["D"]!!))
//}

fun main() {
    getTotalOrbits()
//    getTotalOrbitalTransfersToSanta()
}
