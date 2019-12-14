package day14

import utils.readInput
import kotlin.math.ceil

data class Chemical(val name: String, val cnt: Int)
const val FUEL = "FUEL"
const val ORE = "ORE"

fun main() {
    val data = readInput("src/day14/input.data").map{
        val row = it.split(" => ")
        val dst = row[1].split(" ")
        val src = row[0].split(", ").map { srcit ->
            val srcdata = srcit.split(" ")
            Chemical(srcdata[1], srcdata[0].toInt())
        }
        Chemical(dst[1], dst[0].toInt()) to src
    }
    val rests: MutableMap<String, Int> = mutableMapOf()
    println("Part1: ${process(data, FUEL, 1, rests)}")
}

fun process(map: List<Pair<Chemical, List<Chemical>>>, chemicalName: String, scnt: Int, rests: MutableMap<String, Int>): Int {
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
    var sum = 0
    subs.forEach {
        sum += process(map, it.name, cnt.times(it.cnt), rests)
    }
    return sum
}

fun addRest(map: MutableMap<String, Int>, name: String, diff: Int) {
    map[name]?.let {rest ->
        map[name] = rest + diff
        return
    }
    map[name] = diff
}
