package day11

import day10.Point
import utils.IntCode
import utils.copy
import utils.readInput

const val BLACK = 0L
const val WHITE = 1L
const val LEFT = 0L
const val RIGHT = 1L

enum class Direction {NORTH, EAST, SOUTH, WEST}

fun main() {
    val opcodes = readInput("src/day11/input.data")[0].split(",").map { it.toLong() }
    val points: MutableMap<Point, Long> = mutableMapOf()

    class HullRobot(private val code: List<Long>) {
        private var dir = Direction.NORTH
        private var directionOutput = false
        private val position = Point(0,0)

        fun start(): Long {
            return IntCode(code.copy()).run({ points.getColor(position) }) { out ->
                if (directionOutput) {
                    dir = dir.turn(out, position)
                    //println("$out: $dir $position ${points.size}")
                } else {
                    points[position.copy()] = out
                    //println("$out: $position")
                }

                directionOutput = !directionOutput
            }
        }
    }

    HullRobot(opcodes.copy()).start()
    println("Painted ${points.size}")
}

fun MutableMap<Point, Long>.getColor(point: Point): Long {
    var output = get(point)
    output?.let {
        return output
    }
    return BLACK
}

fun Point.addX(offset: Int) {
    x += offset
}

fun Point.addY(offset: Int) {
    y += offset
}

fun Direction.turn(value: Long, point: Point): Direction {
    return when(value) {
        LEFT -> when(this) {
            Direction.NORTH -> {
                point.addX(-1)
                Direction.WEST
            }
            Direction.EAST -> {
                point.addY(-1)
                Direction.NORTH
            }
            Direction.SOUTH -> {
                point.addX(1)
                Direction.EAST
            }
            Direction.WEST -> {
                point.addY(1)
                Direction.SOUTH
            }
        }
        RIGHT -> when (this) {
            Direction.NORTH -> {
                point.addX(1)
                Direction.EAST
            }
            Direction.EAST -> {
                point.addY(1)
                Direction.SOUTH
            }
            Direction.SOUTH -> {
                point.addX(-1)
                Direction.WEST
            }
            Direction.WEST -> {
                point.addY(-1)
                Direction.NORTH
            }
        }
        else -> throw IllegalStateException("Can not turn $value")
    }
}