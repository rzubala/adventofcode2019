package day09

import day05.copy
import day05.intCode
import utils.readInput

fun main() {
    val opcodes = readInput("src/day09/input.data")[0].split(",").map { it.toLong() }
    intCode(opcodes.copy(), {1}) {out -> println(out)}
    intCode(opcodes.copy(), {2}) {out -> println(out)}
}