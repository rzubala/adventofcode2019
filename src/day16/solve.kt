package day16

import utils.readInput

const val PHASES = 100
const val REPEAT = 10000

val basePattern = mutableListOf<Int>(0, 1, 0, -1)

fun main() {
    val data = readInput("src/day16/input.data")[0].toCharArray().map { it.toString().toLong() }.toMutableList()

    //Part1
    var result = data
    (0 until PHASES).forEach { _ ->
        result = calculatePhase(result)
    }
    println("Part1 ${result.subList(0, 8).joinToString("") { it.toString() }}")

    //Part2
    /*
    val input = mutableListOf<Long>()
    (0 until REPEAT).forEach { _ ->
        input.addAll(data)
    }
    var result = input
    (0 until PHASES).forEach { p ->
        println("phase $p")
        result = calculatePhase(result)
    }
    */
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
    return sum.toString().toCharArray().last().toString().toLong()
}
