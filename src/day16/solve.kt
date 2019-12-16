package day16

import utils.readInput

fun main() {
    val basePattern = mutableListOf<Int>(0, 1, 0, -1)
    val data = readInput("src/day16/input.data")[0].toString().toCharArray().map { it.toString().toInt() }
    var result = data
    (0 until 100).forEach { _ ->
        result = calculatePhase(result, basePattern)
        println(result.toString())
    }
    println(result.subList(0, 8).joinToString("") { it.toString() })
}

private fun calculatePhase(data: List<Int>, basePattern: MutableList<Int>): List<Int> {
    val size = data.size
    val result = mutableListOf<Int>()
    (1..size).forEach { n ->
        val pattern = createPatter(basePattern, n, size)
        val value = calculate(data, pattern)
        result.add(value)
    }
    return result
}

fun calculate(data: List<Int>, pattern: List<Int>): Int {
    var sum = 0
    data.forEachIndexed { i, v ->
        sum += (v.times(pattern[i]))
    }
    return sum.toString().toCharArray().last().toString().toInt()
}

fun createPatter(pattern: List<Int>, pos: Int, size: Int): List<Int> {
    val result = mutableListOf<Int>()
    while (true) {
        pattern.forEach { i ->
            (0 until pos).forEach { p ->
                result.add(i)
            }
        }
        if (result.size > size) {
            break
        }
    }
    return result.drop(1)
}