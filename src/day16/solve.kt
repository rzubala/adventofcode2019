package day16

import utils.copy
import utils.readInput
import kotlin.math.abs

const val PHASES = 100
const val REPEAT = 10000

fun main() {
    val data = readInput("src/day16/input.data")[0].toCharArray().map { it.toString().toInt() }
    var result = data.copy()
    (0 until PHASES).forEach { _ ->
        result = calculatePhase(result)
    }
    println("Part1: ${result.take(8).joinToString("") { it.toString() }}")
    part2(data.copy())
}

private fun calculatePhase(data: List<Int>): MutableList<Int> {
    val result = mutableListOf<Int>()
    data.indices.forEach { n ->
        val value = calculate(data, n)
        result.add(value)
    }
    return result
}

fun calculate(data: List<Int>, outPosition: Int): Int {
    var sum = 0
    val repeat = outPosition.plus(1)
    var from = outPosition
    var add = true
    do {
        if (from >= data.size) {
            break
        }
        var to = from.plus(repeat)
        if (to >= data.size) {
            to = data.size
        }
        val sumIt = data.subList(from, to).sum()
        sum += if (add) sumIt else -sumIt
        add = !add
        from += repeat.times(2)
    } while (true)
    return abs(sum) % 10
}

fun part2(data: List<Int>) {
    val result = mutableListOf<Int>()
    (0 until REPEAT).forEach { _ ->
        result.addAll(data)
    }
    val offset = data.take(7).joinToString("") { it.toString() }.toInt()
    (0 until PHASES).forEach { _ ->
        (result.size - 2 downTo offset).forEach { index ->
            val sum = result[index.plus(1)].plus(result[index])
            result[index] = abs(sum) % 10
        }
    }
    println("Part2: ${result.subList(offset, offset.plus(8)).joinToString("") { it.toString() }}")
}
