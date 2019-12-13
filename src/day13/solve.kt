package day13

import day10.Point
import utils.IntCode
import utils.copy
import utils.readInput

data class Tile(val id: Int)

const val WALL = 1
const val BLOCK = 2
const val H_PADDLE = 3
const val BALL = 4

fun main() {
    val opcodes = readInput("src/day13/input.data")[0].split(",").map { it.toLong() }
    val board: MutableMap<Point, Tile> = mutableMapOf()
    class Game(private val code: List<Long>) {
        var outCnt = 0
        val tmp = MutableList(3) { 0 }
        fun start(): Long {
            return IntCode(code.copy()).run({ 0 }) { out ->
                tmp[outCnt] = out.toInt()
                if (outCnt == 2) {
                    board[Point(tmp[0], tmp[1])] = Tile(tmp[2])
                }
                outCnt = (outCnt + 1) % 3
            }
        }
    }
    Game(opcodes.copy()).start()
    println(board.values.filter { it.id == BLOCK }.size)
}

