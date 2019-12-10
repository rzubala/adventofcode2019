package day11

import utils.IntCode
import utils.copy
import utils.readInput

fun main() {
    val opcodes = readInput("src/day11/input.data")[0].split(",").map { it.toLong() }
    IntCode(opcodes.copy()).run({1}) { out -> println(out)}
}