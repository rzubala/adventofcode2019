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

    //Part2
    program2(code.toMutableList())
}

fun program2(code: MutableList<Long>) {
    val input = mutableListOf<Char>()
    input.addAll(listOf('A',',','A',',','B',',','C',',','C',',','A',',','C',',','B',',','C',',','B','\n'))        //A,A,B,C,C,A,C,B,C,B,
    input.addAll(listOf('L',',','4',',','L',',','4',',','L',',','6',',','R',',','1','0',',','L',',','6','\n'))    //L,4,L,4,L,6,R,10,L,6
    input.addAll(listOf('L',',','1','2',',','L',',','6',',','R',',','1','0',',','L',',','6','\n'))                //L,12,L,6,R,10,L,6
    input.addAll(listOf('R',',','8',',','R',',','1','0',',','L',',','6','\n'))                                    //R,8,R,10,L,6
    input.addAll(listOf('y','\n'))
    var i = 0
    code[0] = 2
    println(IntCode(code.copy()).run({
        val value = input[i].toInt().toLong()
        i++
        println("input: $value")
        value
    }) { out ->
        print(out.toChar())
    })
}

enum class Directions {U, L, D, R}

fun path(start: Point, map: MutableMap<Point, Char>) {
    var dir = Directions.U
    var prevDir = dir
    var lastPosition = start
    var cnt = 0
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
                   val turn = when(prevDir) {
                       Directions.U -> when(dir) {
                           Directions.L -> 'L'
                           Directions.R -> 'R'
                           else -> throw IllegalStateException("not allowed")
                       }
                       Directions.D -> when(dir) {
                           Directions.L -> 'R'
                           Directions.R -> 'L'
                           else -> throw IllegalStateException("not allowed")
                       }
                       Directions.L -> when(dir) {
                           Directions.U -> 'R'
                           Directions.D -> 'L'
                           else -> throw IllegalStateException("not allowed")
                       }
                       Directions.R -> when(dir) {
                           Directions.U -> 'L'
                           Directions.D -> 'R'
                           else -> throw IllegalStateException("not allowed")
                       }
                   }
                   print("$turn,$cnt,")
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
                               prevDir = dir
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
        map[p.up()]?.let {
            map[p.down()]?.let {
                map[p.left()]?.let {
                    map[p.right()]?.let {
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