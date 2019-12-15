package day15

import day02.part2value
import day10.Point
import utils.IntCode
import utils.copy
import utils.readInput
import java.lang.IllegalStateException

const val NORTH = 1L
const val SOUTH = 2L
const val WEST = 3L
const val EAST = 4L

const val WALL = 0L
const val MOVED = 1L
const val OXYGEN = 2L

fun main() {
    val code = readInput("src/day15/input.data")[0].split(",").map { it.toLong() }

    val map: MutableMap<Point, Long> = mutableMapOf()

    class Droid {
        val position = Point(0, 0)
        var lastMove = NORTH
        var lastStatus = MOVED
        fun start() {
            map[Point(0, 0)] = MOVED

            IntCode(code.copy()).run({ getInput() }) { out ->
                lastStatus = out
                map[position.newPosition(lastMove)] = out
                println("Map: ${position.newPosition(lastMove)} = $lastStatus  -> $lastMove")
                if (out != WALL) {
                    position.move(lastMove)
                }
                println("Position: $position")
                map.print(position)
            }
        }
        fun getInput(): Long {
            if (lastStatus == OXYGEN) {
                println("OXYGEN")
                throw IllegalStateException("OXYGEN")
                return 0
            }
            lastMove = getNextMove(position)
            if (lastMove == 0L) {
                map.print(position)
                throw IllegalStateException("can not move")
                return 0
            }
            return lastMove
        }

        private fun getNextMove(pos: Point): Long {
            val west = map[pos.newPosition(WEST)] ?: return WEST
            val north = map[pos.newPosition(NORTH)] ?: return NORTH
            val east = map[pos.newPosition(EAST)] ?: return EAST
            val south = map[pos.newPosition(SOUTH)] ?: return SOUTH

            if (west != WALL) {
                val res = getNextMove(pos.newPosition(WEST))
                if (res > 0) {
                    return WEST
                }
            }
            if (north != WALL) {
                val res = getNextMove(pos.newPosition(NORTH))
                if (res > 0) {
                    return NORTH
                }
            }
            if (east != WALL) {
                val res = getNextMove(pos.newPosition(EAST))
                if (res > 0) {
                    return EAST
                }
            }
            if (south != WALL) {
                val res = getNextMove(pos.newPosition(SOUTH))
                if (res > 0) {
                    return SOUTH
                }
            }

            println("Not found $pos")
            return 0
        }
    }

    Droid().apply {
        start()
    }
}

fun Point.newPosition(dir: Long): Point {
    return Point(x, y).apply { move(dir) }
}

fun Point.move(dir: Long) {
    when(dir) {
        NORTH -> y -= 1
        SOUTH -> y += 1
        WEST -> x -=1
        EAST -> x += 1
        else -> throw IllegalStateException("Not known move $dir")
    }
}

fun Map<Point, Long>.print(droid: Point) {
    val size = this.size
    var min = Point(Int.MAX_VALUE, Int.MAX_VALUE)
    var max = Point(Int.MIN_VALUE, Int.MIN_VALUE)

    (-size until size).forEach { y ->
        (-size until size).forEach { x ->
            this[Point(x,y)]?.let {
                if (x > max.x) {
                    max.x = x
                }
                if (x < min.x) {
                    min.x = x
                }
                if (y > max.y) {
                    max.y = y
                }
                if (y < min.y) {
                    min.y = y
                }
            }
        }
    }

    println("$min $max")

    (min.y..max.y).forEach { y ->
        (min.x..max.x).forEach { x ->
            var empty = true
            this[Point(x,y)]?.let {
                if (droid.x == x && droid.y == y){
                    print('D')
                } else {
                    print(
                        when (it) {
                            WALL -> '#'
                            MOVED -> '.'
                            OXYGEN -> 'O'
                            else -> ' '
                        }
                    )
                }
                empty = false
            }
            if (empty) {
                print(' ')
            }
        }
        println()
    }

}