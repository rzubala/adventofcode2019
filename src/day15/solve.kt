package day15

import day10.Point
import utils.IntCode
import utils.clearConsole
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
    val path: MutableList<Long> = mutableListOf()

    class Droid {
        val position = Point(0, 0)
        val oxygen = Point(0, 0)
        var lastMove = NORTH
        var lastStatus = MOVED
        var back = false
        fun start() {
            map[Point(0, 0)] = MOVED

            IntCode(code.copy()).run({ getInput() }) { out ->
                lastStatus = out
                map[position.newPosition(lastMove)] = out
                if (out != WALL) {
                    position.move(lastMove)
                    if (!back) {
                        path.add(lastMove)
                    }
                }
            }
        }
        fun getInput(): Long {
            if (lastStatus == OXYGEN) {
                oxygen.x = position.x
                oxygen.y = position.y
                println ("Part1: ${path.size}")
            }
            lastMove = getNextMove(position)
            if (lastMove == 0L) {
                println("Part2: ${spreadOxygen(map, oxygen, 0)}")
                return 0
            }
            return lastMove
        }

        private fun getNextMove(pos: Point): Long {
            back = false
            map[pos.newPosition(WEST)] ?: return WEST
            map[pos.newPosition(NORTH)] ?: return NORTH
            map[pos.newPosition(EAST)] ?: return EAST
            map[pos.newPosition(SOUTH)] ?: return SOUTH
            if (path.isEmpty()) {
                return 0
            }
            back = true
            return when(path.removeAt(path.size-1)) {
               NORTH -> SOUTH
               SOUTH -> NORTH
               WEST -> EAST
               EAST -> WEST
               else -> 0
            }
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

private fun spreadOxygen(map: MutableMap<Point, Long>, pos: Point, level: Int): Int {
    var max = level
    if (map[pos.newPosition(NORTH)] == MOVED) {
        map[pos.newPosition(NORTH)] = OXYGEN
        val tLevel = spreadOxygen(map, pos.newPosition(NORTH), level+1)
        if (tLevel > max) {
            max = tLevel
        }
    }
    if (map[pos.newPosition(SOUTH)] == MOVED) {
        map[pos.newPosition(SOUTH)] = OXYGEN
        val tLevel = spreadOxygen(map, pos.newPosition(SOUTH), level+1)
        if (tLevel > max) {
            max = tLevel
        }
    }
    if (map[pos.newPosition(WEST)] == MOVED) {
        map[pos.newPosition(WEST)] = OXYGEN
        val tLevel = spreadOxygen(map, pos.newPosition(WEST), level+1)
        if (tLevel > max) {
            max = tLevel
        }
    }
    if (map[pos.newPosition(EAST)] == MOVED) {
        map[pos.newPosition(EAST)] = OXYGEN
        val tLevel = spreadOxygen(map, pos.newPosition(EAST), level+1)
        if (tLevel > max) {
            max = tLevel
        }
    }
    return max
}

fun Map<Point, Long>.print() {
    val size = this.size
    val min = Point(Int.MAX_VALUE, Int.MAX_VALUE)
    val max = Point(Int.MIN_VALUE, Int.MIN_VALUE)

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

    (min.y..max.y).forEach { y ->
        (min.x..max.x).forEach { x ->
            var empty = true
            this[Point(x,y)]?.let {
                empty = false
                print(
                    when (it) {
                        WALL -> '#'
                        MOVED -> ' '
                        OXYGEN -> 'O'
                        else -> ' '
                    }
                )
            }
            if (empty) {
                print(' ')
            }
        }
        println()
    }
}
