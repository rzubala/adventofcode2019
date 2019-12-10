package day09

import utils.copy
import utils.intCode
import utils.readInput

fun main() {
    val opcodes = readInput("src/day09/input.data")[0].split(",").map { it.toLong() }
    intCode(opcodes.copy(), {1}) {out -> println(out)}
    intCode(opcodes.copy(), {2}) {out -> println(out)}
}