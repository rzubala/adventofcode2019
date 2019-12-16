package day16

import utils.copy
import utils.readInput
import kotlin.math.abs

const val PHASES = 100
const val REPEAT = 10000

fun main() {
    val data = readInput("src/day16/input.data")[0].toCharArray().map { it.toString().toLong() }.toMutableList()

    var result = data.copy()
    (0 until PHASES).forEach { _ ->
        result = calculatePhase(result)
    }
    println("Part1: ${result.take(8).joinToString("") { it.toString() }}")
    part2(data.copy())
}

private fun calculatePhase(data: List<Long>): MutableList<Long> {
    val result = mutableListOf<Long>()
    (0L until data.size).forEach { n ->
        val value = calculate(data, n)
        result.add(value)
    }
    return result
}

fun calculate(data: List<Long>, outPosition: Long): Long {
    var sum = 0L
    val repeat = outPosition.plus(1).toInt()
    var iterator = outPosition.toInt()
    var add = true
    do {
        if (iterator >= data.size) {
            break
        }
        var to = iterator + repeat
        if (to >= data.size) {
            to = data.size
        }
        val sumIt = data.subList(iterator, to).sum()
        if (add) {
            sum += sumIt
        } else {
            sum -= sumIt
        }
        add = !add
        iterator += repeat.times(2)
    } while (true)
    return abs(sum % 10)
}

fun part2(data: List<Long>) {
    val result = mutableListOf<Long>()
    (0 until REPEAT).forEach { _ ->
        result.addAll(data)
    }
    val offset = data.take(7).joinToString("") { it.toString() }.toLong()
    (0 until PHASES).forEach { _ ->
        (result.size - 2 downTo offset).forEach { index ->
            val sum = result[index.toInt() + 1] + result[index.toInt()]
            result[index.toInt()] = abs(sum % 10)
        }
    }
    println("Part2: ${result.subList(offset.toInt(), offset.toInt() + 8).joinToString("") { it.toString() }}")
}
