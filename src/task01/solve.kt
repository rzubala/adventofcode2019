package task01

import utils.readInput

fun main() {
    val lines = readInput("src/task01/input.data")
    val res = lines.map { Integer.valueOf(it).div(3).minus(2) }.sum()
    println("sum: $res")
}