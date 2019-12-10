package day05

import utils.copy
import utils.intCode
import utils.readInput

fun main() {
    val opcodes = readInput("src/day05/input.data")[0].split(",").map { it.toLong() }
    println("${intCode(opcodes.copy(), {1}) {} }")
    println("${intCode(opcodes.copy(), {5}) {} }")
}