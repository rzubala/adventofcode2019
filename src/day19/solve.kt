package day19

import day10.Point
import utils.IntCode
import utils.copy
import utils.readInput

const val LAST_INDEX = 49
const val SIZE = 100
const val LAST_INDEX_2 = SIZE.times(10) + SIZE

typealias MatrixInt = MutableList<MutableList<Int>>

fun main() {
    val code = readInput("src/day19/input.data")[0].split(",").map { it.toLong() }
    //Part1
    val map1: MatrixInt = buildMap(code, LAST_INDEX )
    println("Part1 ${ map1.sumBy { it.sum() }}")

    //Part2
    val map: MatrixInt = buildMap(code,  LAST_INDEX_2)
    var dist = LAST_INDEX_2.times(2)
    var result = 0
    val cache: MutableMap<Point, Int> = mutableMapOf()
    for (y in 0..LAST_INDEX_2-SIZE) {
        val line = map[y]
        if (line.sum() >= SIZE) {
            val x1 = line.indexOfFirst { it == 1 }
            val x2 = line.indexOfLast { it == 1 }
            for (x in x1..x2) {
                var sumY = getSumY(map, x, y, cache)
                if (sumY >= SIZE) {
                    sumY = getSumY(map, x+SIZE-1, y, cache)
                    if (sumY >= SIZE) {
                        if ((x + y) < dist) {
                            dist = x + y
                            result = 10000 * x + y
                        }
                    }
                }
            }
        }
    }
    println("Part2 $result")
}

fun getSumY(map: MatrixInt, x: Int, y: Int, cache: MutableMap<Point, Int>): Int {
    cache[Point(x,y)]?.let{
        return it
    }
    var sum = 0
    for (i in y until map.size) {
        if (map[i][x] == 0) {
            cache[Point(x,y)] = sum
            return sum
        }
        sum += map[i][x]
        if (sum >= SIZE) {
            cache[Point(x,y)] = sum
            return sum
        }
    }
    cache[Point(x,y)] = sum
    return sum
}

private fun buildMap(code: List<Long>, size: Int): MatrixInt {
    val map: MatrixInt = mutableListOf()
    var lastX = 0
    (0..size).forEach { y ->
        val row = MutableList(size) { 0 }
        map.add(row)
        var isX = true
        var beam = false
        var stop = false
        for (x in (lastX..size)) {
            val position = Point(x, y)
            IntCode(code.copy()).run({
                val value = if (isX) position.x.toLong() else position.y.toLong()
                isX = !isX
                value
            }) { out ->
                if (!beam && out == 1L) {
                    beam = true
                    lastX = position.x
                }
                if (beam && out == 0L) {
                    stop = true
                }
                if (out > 0L) {
                    row[position.x] = out.toInt()
                }
            }
            if (stop) {
                break
            }
        }
    }
    return map
}