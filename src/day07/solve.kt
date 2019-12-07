package day07

import day02.copy
import day05.intCode
import utils.readInput

fun main() {
    val opcodes = readInput("src/day07/input.data")[0].split(",").map { it.toInt() }
    part1(opcodes)
}

private fun part1(opcodes: List<Int>) {
    var max = Integer.MIN_VALUE
    val phases = (0..4)
    generatePhases(phases).forEach { it ->
        var output = 0
        output = intCode(opcodes.copy(), listOf(it[0], output))
        output = intCode(opcodes.copy(), listOf(it[1], output))
        output = intCode(opcodes.copy(), listOf(it[2], output))
        output = intCode(opcodes.copy(), listOf(it[3], output))
        output = intCode(opcodes.copy(), listOf(it[4], output))
        if (output > max) {
            max = output
        }
    }
    println("max: $max")
}

fun generatePhases(phases: IntRange): List<IntArray> {
    val result = mutableListOf<IntArray>()
    for (p0 in phases) {
        for (p1 in phases) {
            if (p1 == p0) {
                continue
            }
            for (p2 in phases) {
                if (p2 == p1 || p2 == p0) {
                    continue
                }
                for (p3 in phases) {
                    if (p3 == p2 || p3 == p1 || p3 == p0) {
                        continue
                    }
                    for (p4 in phases) {
                        if (p4 == p3 || p4 == p2 || p4 == p1 || p4 == p0) {
                            continue
                        }
                        result.add(intArrayOf(p0, p1, p2, p3 ,p4))
                    }
                }
            }
        }
    }
    return result
}