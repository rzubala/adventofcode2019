package day01

import utils.readInput

fun main() {
    val lines = readInput("src/day01/input.data").map {Integer.valueOf(it)}
    val res1 = lines.sumBy{ operation(it) }
    println("sum: $res1")
    val res2 = lines.sumBy { operation2(it) }
    println("sum: $res2")
}

fun operation2(value: Int): Int {
    var sum = 0
    var tmp = value
    do {
        tmp = operation(tmp)
        if (tmp <= 0) {
            break
        }
        sum += tmp
    } while (true)
    return sum
}

inline fun operation(value: Int) = value.div(3).minus(2)