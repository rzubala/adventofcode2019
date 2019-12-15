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

const val limit = 1000

fun main() {
    val code = readInput("src/day15/input.data")[0].split(",").map { it.toLong() }

    val map: MutableMap<Point, Long> = mutableMapOf()
    val path: MutableList<Long> = mutableListOf()

    class Droid {
        val position = Point(0, 0)
        var lastMove = NORTH
        var lastStatus = MOVED
        var cnt = 0
        var back = false
        fun start() {
            map[Point(0, 0)] = MOVED

            IntCode(code.copy()).run({ getInput() }) { out ->
                lastStatus = out
                map[position.newPosition(lastMove)] = out
                println("Map $cnt: ${position.newPosition(lastMove)} = $lastStatus  -> $lastMove")
                if (out != WALL) {
                    position.move(lastMove)
                    if (!back) {
                        path.add(lastMove)
                    }
                }

                cnt++
                //map.print(position)
                if (cnt > limit) {
                    throw IllegalStateException("STOP")
                }
            }
        }
        fun getInput(): Long {
            if (lastStatus == OXYGEN) {
                println (path.size)
                //map.print(position)
                println("OXYGEN")
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
            back = false
            val west = map[pos.newPosition(WEST)] ?: return WEST
            val north = map[pos.newPosition(NORTH)] ?: return NORTH
            val east = map[pos.newPosition(EAST)] ?: return EAST
            val south = map[pos.newPosition(SOUTH)] ?: return SOUTH

            if (path.isEmpty()) {
                println("Path is empty $pos")
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