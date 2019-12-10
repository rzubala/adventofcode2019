package day05

import utils.IntCode
import utils.copy
import utils.readInput

fun main() {
    val opcodes = readInput("src/day05/input.data")[0].split(",").map { it.toLong() }
    println("${IntCode(opcodes.copy()).run({1}) {} }")
    println("${IntCode(opcodes.copy()).run({5}) {} }")
}