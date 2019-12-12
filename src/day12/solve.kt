package day12

import utils.readInput
import java.lang.IllegalStateException

const val regexStr = "x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)"
const val STEPS = 1000

sealed class Point3D(var x: Int, var y:Int, var z:Int)
class Moon(ix: Int, iy:Int, iz:Int) : Point3D(ix, iy, iz) {
    val v = Velocity(0, 0, 0)
    fun updatePosition() {
        x += v.x
        y += v.y
        z += v.z
    }
}
class Velocity(ix: Int, iy:Int, iz:Int) : Point3D(ix, iy, iz)

fun main() {
    val data = readInput("src/day12/input.data")
    val moons: List<Moon> = parseMoons(data)
    simulate(moons)
}

fun simulate(moons: List<Moon>) {
    (1..STEPS).forEach{ _ ->
        updateVelocity(moons)
        updatePosition(moons)
    }
    calcEnergy(moons)
}

fun calcEnergy(moons: List<Moon>) {
    var energy = 0L
    moons.forEach { m ->
        energy += (kotlin.math.abs(m.x) + kotlin.math.abs(m.y) + kotlin.math.abs(m.z)) * (kotlin.math.abs(m.v.x) + kotlin.math.abs(m.v.y) + kotlin.math.abs(m.v.z))
    }
    println("energy: $energy")
}

fun updatePosition(moons: List<Moon>) {
    moons.forEach { m ->
        m.updatePosition()
    }
}

fun updateVelocity(moons: List<Moon>) {
    (0 until moons.size-1).forEach { m1 ->
        (m1+1 until moons.size).forEach { m2 ->
            updateMoonsVelocity(moons[m1], moons[m2])
        }
    }
}

fun updateMoonsVelocity(m1: Moon, m2: Moon) {
    if (m1.x > m2.x) {
        m1.v.x -= 1
        m2.v.x += 1
    } else if (m1.x < m2.x) {
        m1.v.x += 1
        m2.v.x -= 1
    }
    if (m1.y > m2.y) {
        m1.v.y -= 1
        m2.v.y += 1
    } else if (m1.y < m2.y) {
        m1.v.y += 1
        m2.v.y -= 1
    }
    if (m1.z > m2.z) {
        m1.v.z -= 1
        m2.v.z += 1
    } else if (m1.z < m2.z) {
        m1.v.z += 1
        m2.v.z -= 1
    }
}

private fun parseMoons(data: List<String>): List<Moon> {
    val regex = regexStr.toRegex()
    return data.map {
        val matchResult = regex.find(it)
        var x: Int = Int.MIN_VALUE
        var y: Int = Int.MIN_VALUE
        var z: Int = Int.MIN_VALUE
        var found = false
        matchResult?.let {
            x = matchResult.groupValues[1].toInt()
            y = matchResult.groupValues[2].toInt()
            z = matchResult.groupValues[3].toInt()
            found = true
        }
        if (!found) {
            throw IllegalStateException("Can not parse $it")
        }
        Moon(x, y, z)
    }
}

fun Moon.print() {
    println("$x $y $z : ${v.x} ${v.y} ${v.z}")
}