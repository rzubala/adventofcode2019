package day01

import utils.readInput

fun Int.operation() = this.div(3).minus(2)

fun Int.operation2(): Int {
    val result = this.operation()
    return if (result <= 0) {
        0
    } else {
        result + result.operation2()
    }
}

fun main() {
    val lines = readInput("src/day01/input.data").map {it.toInt()}
    val res1 = lines.sumBy{ it.operation() }
    println("sum: $res1")
    val res2 = lines.sumBy { it.operation2() }
    println("sum: $res2")
}
