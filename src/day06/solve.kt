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
}

fun Orbit.countParents(): Int {
    if (parent == null) {
        return 0
    }
    return 1 + parent!!.countParents();
}
