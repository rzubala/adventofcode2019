package day16

import utils.readInput

const val PHASES = 100

val basePattern = mutableListOf(0, 1, 0, -1)

fun main() {
    val data = readInput("src/day16/input.data")[0].toCharArray().map { it.toString().toInt() }.toMutableList()

    //Part1
    var result = data
    (0 until PHASES).forEach { _ ->
        result = calculatePhase(result)
    }
    println("Part1 ${result.subList(0, 8).joinToString("") { it.toString() }}")

    //Part2
    /*
    val input = mutableListOf<Int>()
    (0 until 10000).forEach { _ ->
        input.addAll(data)
    }
    var result = input
    (0 until PHASES).forEach { p ->
        println("phase $p")
        result = calculatePhase(result)
    }
    */
}

private fun calculatePhase(data: List<Int>): MutableList<Int> {
    val result = mutableListOf<Int>()
    data.indices.forEach { n ->
        val value = calculate2(data, n)
        result.add(value)
    }
    return result
}

fun calculate2(data: List<Int>, outPosition: Int): Int {
    var sum = 0
    data.forEachIndexed { inPosition, v ->
        if (v != 0) {
            val pattern = getPattern(outPosition, inPosition)
            if (pattern != null) {
                sum += (v.times(pattern))
            }
        }
    }
    return sum.toString().toCharArray().last().toString().toInt()
}

fun getPattern(outP: Int, inP: Int): Int {
    val repeat = outP.plus(1)
    val patternSize = repeat.times(basePattern.size)
    val index = (inP.plus(1) % patternSize).div(repeat)
    return basePattern[index]
}
