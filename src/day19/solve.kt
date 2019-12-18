package day19

import utils.IntCode
import utils.copy
import utils.readInput

fun main() {
    val code = readInput("src/day19/input.data")[0].split(",").map { it.toLong() }

    class Processor {
        fun start() {
            IntCode(code.copy()).run({ getInput() }) { out ->
                println(out)
            }
        }
        private fun getInput(): Long {
            return 0L
        }
    }

    Processor().apply {
        start()
    }
}