package day06

import utils.readInput

data class Orbit (val name:String, var parent: Orbit? = null)

fun main() {
    val orbits: MutableList<Orbit> = mutableListOf(Orbit("COM"))
    readInput("src/day06/input.data").forEach { row ->
        val data = row.split(")")
        val parentPlanet = orbits.addOrbit(data[0])
        val childPlanet = orbits.addOrbit(data[1])
        childPlanet.apply {
            parent = parentPlanet
        }
    }
    println("orbits: ${orbits.sumBy { it.countParents() }}")
    calculateTransfers(orbits, "YOU", "SAN")
}

fun calculateTransfers(orbits: MutableList<Orbit>, name1: String, name2: String) {
    val parents1 = orbits.find { it.name == name1 }?.getParents()
    val parents2 = orbits.find { it.name == name2 }?.getParents()
    parents1?.let {
        parents2?.let {
            val common = parents1.intersect(parents2)
            val commonOrbit = common.iterator().next()
            val transfers = parents2.indexOf(commonOrbit).let { result ->
                parents1.indexOf(commonOrbit).plus(result)
            }
            println("transfers: $transfers")
        }
    }
}

fun Orbit.getParents(): List<String> {
    val parents: MutableList<String> = mutableListOf()
    fillParents(parents)
    return parents
}

fun Orbit.fillParents(list: MutableList<String>) {
    parent?.let {
        list.add(parent!!.name)
        parent!!.fillParents(list)
    }
}

fun Orbit.countParents(): Int {
    parent?.let {
        return parent!!.countParents().plus(1)
    }
    return 0
}

fun MutableList<Orbit>.addOrbit(name: String): Orbit {
    var orbit = find { it.name == name }
    orbit?.let{
        return it
    }
    orbit = Orbit(name)
    add(orbit)
    return orbit
}
