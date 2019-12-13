package day13

import day10.Point
import utils.IntCode
import utils.copy
import utils.readInput

const val BLOCK = 2
const val H_PADDLE = 3
const val BALL = 4

const val LEFT = -1L
const val RIGHT = 1L

fun main() {
    val opcodes = readInput("src/day13/input.data")[0].split(",").map { it.toLong() }
    play(opcodes.copy())
}

private fun play(opcodes: MutableList<Long>) {
    var paddle: Point? = null
    var ball: Point? = null
    var blocks = 0
    opcodes[0] = 2

    class Game(private val code: List<Long>) {
        var outCnt = 0
        val data = MutableList(3) { 0 }
        fun start(): Long {
            return IntCode(code.copy()).run({ getInput() }) { out ->
                data[outCnt] = out.toInt()
                if (outCnt == 2) {
                    when {
                        data[2] == BALL -> {
                            ball = Point(data[0], data[1])
                        }
                        data[2] == H_PADDLE -> {
                            paddle = Point(data[0], data[1])
                        }
                        data[2] == BLOCK -> {
                            blocks++
                        }
                    }
                }
                outCnt = (outCnt + 1) % 3
            }
        }
        private fun getInput(): Long {
            ball?.let { itb->
                paddle?.let {itp ->
                    return when {
                        itp.x < itb.x -> {
                            RIGHT
                        }
                        itp.x > itb.x -> {
                            LEFT
                        }
                        else -> {
                            0
                        }
                    }
                }
            }
            return 0
        }
        fun score() {
            println("Blocks: $blocks")
            println("Score ${data[2]}")
        }
    }
    Game(opcodes.copy()).apply {
        start()
        score()
    }
}
