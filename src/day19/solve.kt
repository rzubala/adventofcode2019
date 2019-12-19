package day19

import day10.Point
import utils.IntCode
import utils.copy
import utils.readInput

const val LAST_INDEX = 49
const val SIZE = 100

fun main() {
    val code = readInput("src/day19/input.data")[0].split(",").map { it.toLong() }

    //Part1
    val map: MutableList<MutableList<Int>> = buildMap(code, LAST_INDEX )
    println("sum ${ map.sumBy { it.sum() }}")

}

private fun buildMap(code: List<Long>, size: Int): MutableList<MutableList<Int>> {
    val map: MutableList<MutableList<Int>> = mutableListOf()
    (0..size).forEach { y ->
        val row = mutableListOf<Int>()
        map.add(row)
        (0..size).forEach { x ->
            val position = Point(x, y)
            var isX = true
            IntCode(code.copy()).run({
                val value = if (isX) position.x.toLong() else position.y.toLong()
                isX = !isX
                value
            }) { out ->
                row.add(out.toInt())
            }
        }
    }
    return map
}