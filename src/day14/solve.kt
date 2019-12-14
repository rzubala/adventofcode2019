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

    val rests: MutableMap<String, Long> = mutableMapOf()
    val oresPerFuel = process(data, FUEL, 1L, rests)
    println("Part1: $oresPerFuel")

    rests.clear()
    //part2
    var cnt = 0
    val start = ceil(LIMIT.toDouble()/oresPerFuel).times(1.1).toLong()   //add 10%
    while (true) {
        val fuels = start + cnt
        val ores = process(data, FUEL, fuels, rests)
        if (ores > LIMIT) {
            println ("Part2: ${fuels - 1}")
            break
        }
        cnt++
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
        map[name] = rest + diff
        return
    }
    map[name] = diff
}
