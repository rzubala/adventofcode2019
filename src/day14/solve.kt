package day14

import utils.readInput
import kotlin.math.ceil

data class Chemical(val name: String, val cnt: Long)
const val FUEL = "FUEL"
const val ORE = "ORE"
const val LIMIT = 1000000000000

fun main() {
    val data = readInput("src/day14/input.data").map{
        val row = it.split(" => ")
        val dst = row[1].split(" ")
        val src = row[0].split(", ").map { srcit ->
            val srcdata = srcit.split(" ")
            Chemical(srcdata[1], srcdata[0].toLong())
        }
        Chemical(dst[1], dst[0].toLong()) to src
    }

    val oresPerFuel = process(data, FUEL, 1L, mutableMapOf())
    println("Part1: $oresPerFuel")

    //part2
    val initialStart = ceil(LIMIT.toDouble() / oresPerFuel).toLong()
    var fuels = initialStart
    var step = initialStart.times(0.1).toLong()
    var up = false
    while (true) {
        val ores = process(data, FUEL, fuels, mutableMapOf())
        if (step == 0L) {
            if (ores > LIMIT) {
                fuels--
            }
            println("Part2: $fuels")
            break
        }
        if (when {
            ores > LIMIT -> {
                fuels -= step
                val res = up
                up = false
                res
            }
            else -> {
                fuels += step
                val res = !up
                up = true
                res
            }
        }) {
            step = step.div(2)
        }
    }
}

fun process(map: List<Pair<Chemical, List<Chemical>>>, chemicalName: String, scnt: Long, rests: MutableMap<String, Long>): Long {
    if (chemicalName == ORE) {
        return scnt
    }
    val dst = map.first { it.first.name == chemicalName }
    val dstCnt = dst.first.cnt
    var cnt = ceil(scnt.toDouble().div(dstCnt.toDouble())).toInt()
    var diff = cnt.times(dstCnt) - scnt
    rests[dst.first.name]?.let {restValue ->
        if (restValue >= scnt) {
            rests[dst.first.name] = restValue - scnt
            return 0
        }
        if (restValue > 0) {
            val tmpCnt = ceil((scnt - restValue).toDouble().div(dstCnt.toDouble())).toInt()
            if (tmpCnt < cnt) {
                cnt = tmpCnt
                rests[dst.first.name] = 0
                diff = cnt.times(dstCnt) - (scnt - restValue)
            }
        }
    }
    if (diff > 0) {
        addRest(rests, dst.first.name, diff)
    }
    val subs = dst.second
    var sum = 0L
    subs.forEach {
        sum += process(map, it.name, cnt.times(it.cnt), rests)
    }
    return sum
}

fun addRest(map: MutableMap<String, Long>, name: String, diff: Long) {
    map[name]?.let {rest ->
        map[name] = rest.plus(diff)
        return
    }
    map[name] = diff
}
