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
    val start = Point(0,0)
    IntCode(code.copy()).run({ 0L }) { out ->
        when (out.toInt()) {
            10 -> {
                y++
                x = -1
            }
            '#'.toInt() -> map[Point(x, y)] = '#'
            '^'.toInt() -> {
                map[Point(x, y)] = '#'
                start.set(x, y)
            }
        }
        x++
        print(out.toChar())
    }
    findMapCrossings(map)
    path(start, map)
}

enum class Directions {U, L, D, R}

fun path(start: Point, map: MutableMap<Point, Char>) {
    var dir = Directions.U
    var lastPosition = start
    var cnt = 0
    var it = 0
    main@ while (true) {
        val next = when(dir) {
           Directions.U -> { map[lastPosition.up()] ?: '.' }
           Directions.L -> { map[lastPosition.left()] ?: '.' }
           Directions.D -> { map[lastPosition.down()] ?: '.' }
           Directions.R -> { map[lastPosition.right()] ?: '.' }
        }
        when(next) {
           '#' -> {
               lastPosition = lastPosition.move(dir)
               cnt++
           }
           '.' -> {
               if (cnt > 0) {
                   print("$cnt$dir, ")
               }
               cnt = 0
               var found = false
               dir@ for (d in Directions.values()) {
                   if (d != dir) {
                       if (d == Directions.U && dir == Directions.D || dir == Directions.U && d == Directions.D) {
                           continue
                       }
                       if (d == Directions.L && dir == Directions.R || dir == Directions.L && d == Directions.R) {
                           continue
                       }
                       val testPosition = lastPosition.move(d)
                       if (testPosition != lastPosition) {
                           map[testPosition]?.let {
                               lastPosition = testPosition
                               dir = d
                               found = true
                               cnt++
                           }
                           if (found) {
                               break@dir
                           }
                       }
                   }
               }
               if (!found) {
                   break@main
               }
           }
       }
    }
    println("")
}

fun Point.move(dir: Directions): Point {
    return when(dir) {
        Directions.U -> up()
        Directions.D -> down()
        Directions.L -> left()
        Directions.R -> right()
    }
}

fun findMapCrossings(map: MutableMap<Point, Char>) {
    var sum = 0
    map.keys.forEach{p ->
        map[p.up()]?.let {u ->
            map[p.down()]?.let {d ->
                map[p.left()]?.let {l ->
                    map[p.right()]?.let {r ->
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
fun Point.set(x: Int, y: Int) {
    this.x = x
    this.y = y
}