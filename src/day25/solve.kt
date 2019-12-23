package day25

import utils.IntCode
import utils.copy
import utils.readInput

fun main() {
    val code = readInput("src/day25/input.data")[0].split(",").map { it.toLong() }
    IntCode(code.copy()).run({
        0L
    }) { out ->
        println(out)
    }
}