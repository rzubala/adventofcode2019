package day17

import day10.Point
import utils.IntCode
import utils.copy
import utils.readInput

fun main() {
    val code = readInput("src/day17/input.data")[0].split(",").map { it.toLong() }

    val map = mutableMapOf<Point, Char>()

    var x = 0
    var y = 0
    IntCode(code.copy()).run({ 0L }) { out ->
        when(out.toInt()) {
            10 -> {
                y++
                x = -1
            }
            '#'.toInt() -> map[Point(x,y)] = '#'
        }
        x++
        print(out.toChar())
    }
    println(map.toString())
    findMapCrossings(map)
}

fun findMapCrossings(map: MutableMap<Point, Char>) {
    var sum = 0
    map.keys.forEach{p ->
        map[p.up()]?.let {u ->
            map[p.down()]?.let {d ->
                map[p.left()]?.let {l ->
                    map[p.right()]?.let {r ->
                        println("found $p")
                        sum += p.x.times(p.y)
                    }
                }
            }
        }
    }
    println("Sum: $sum")
}

fun Point.up(): Point = Point(x, y-1)
fun Point.down(): Point = Point(x, y+1)
fun Point.left(): Point = Point(x-1, y)
fun Point.right(): Point = Point(x+1, y)
