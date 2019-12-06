package day06

import utils.readInput

data class Orbit (val name:String, var parent: Orbit? = null)

fun main() {
    val orbits: MutableList<Orbit> = mutableListOf(Orbit("COM"))
    readInput("src/day06/input.data").forEach { row ->
        val data = row.split(")")
        var parent = orbits.find { it.name == data[0] }
        if (parent == null) {
            parent = Orbit(data[0])
            orbits.add(parent)
        }
        var child = orbits.find { it.name == data[1] }
        if (child == null) {
            child = Orbit(data[1])
            orbits.add(child)
        }
        child.parent = parent
    }
    println("orbits: ${orbits.sumBy { it.countParents() }}")

    calculateTransfers(orbits, "YOU", "SAN")
}

fun calculateTransfers(orbits: MutableList<Orbit>, name1: String, name2: String) {
    val p1 = orbits.find { it.name == name1 }
    val p2 = orbits.find { it.name == name2 }
    val parents1 = p1?.getParents()
    val parents2 = p2?.getParents()
    val common = parents1?.intersect(parents2!!)
    val commonOrbit = common?.iterator()?.next()
    commonOrbit?.let {
        println("transfers: ${parents2?.indexOf(commonOrbit)?.let {result ->
            parents1.indexOf(commonOrbit).plus(result)
        }
        }")
    }
}

fun Orbit.getParents(): List<String> {
    val parents: MutableList<String> = mutableListOf()
    var parent = parent
    while (parent != null) {
        parents.add(parent.name)
        parent = parent.parent
    }
    return parents
}

fun Orbit.countParents(): Int {
    if (parent == null) {
        return 0
    }
    return 1 + parent!!.countParents()
}
