package day16

import utils.readInput

const val PHASES = 100
const val REPEAT = 10000

val basePattern = mutableListOf<Long>(0, 1, 0, -1)

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
    (0L until data.size).forEach { inPosition ->
        val v = data[inPosition.toInt()]
        if (v != 0L) {
            val pattern = getPattern(outPosition, inPosition)
            if (pattern != 0L) {
                sum += (v.times(pattern))
            }
        }
    }
    return sum.toString().toCharArray().last().toString().toLong()
}

fun getPattern(outP: Long, inP: Long): Long {
    val repeat = outP.plus(1)
    val patternSize = repeat.times(basePattern.size)
    val index = (inP.plus(1) % patternSize).div(repeat)
    return basePattern[index.toInt()]
}
