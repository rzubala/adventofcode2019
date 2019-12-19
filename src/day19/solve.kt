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
//    val map1: MutableList<MutableList<Int>> = buildMap(code, LAST_INDEX )
//    println("sum ${ map1.sumBy { it.sum() }}")
//    println(map1.forEach { println(it.joinToString("") { it.toString() }) })

    //Part2
    val size: Int = 1100
    val map: MutableList<MutableList<Int>> = buildMap(code,  size)
    //println(map.forEach { println(it.joinToString("") { it.toString() }) })
    var dist = size + size
    var result = 0
    for (y in 0..size-SIZE) {
        val line = map[y]
        if (line.sum() >= SIZE) {
            val x1 = line.indexOfFirst { it == 1 }
            val x2 = line.indexOfLast { it == 1 }
            println("${y+1}: line sum ${line.sum()}, ${x1+1} .. ${x2+1}")
            for (x in x1..x2) {
                var sumY = getSumY(map, x, y)
                //println("sumy1 ${x+1} $sumY")
                if (sumY >= SIZE) {
                    sumY = getSumY(map, x+SIZE-1, y)
                    //println("sumy2 ${x+SIZE-1+1} $sumY")
                    if (sumY >= SIZE) {
                        if ((x + y) < dist) {
                            dist = x + y
                            result = 10000 * x + y
                        }
                        //println("found $y $x, $x1 .. $x2, ${x-SIZE + y}")
                    }
                }
            }
        }
    }
    println("Part2 $result $dist")
    //println("sum ${ map.sumBy { it.sum() }}")

}

fun getSumY(map: MutableList<MutableList<Int>>, x: Int, y: Int): Int {
    var sum = 0
    for (i in y until map.size) {
        if (map[i][x] == 0) {
            return sum
        }
        sum += map[i][x]
    }
    return sum
}

private fun buildMap(code: List<Long>, size: Int): MutableList<MutableList<Int>> {
    val map: MutableList<MutableList<Int>> = mutableListOf()
    var lastX = 0
    (0..size).forEach { y ->
        println("build $y $lastX")
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