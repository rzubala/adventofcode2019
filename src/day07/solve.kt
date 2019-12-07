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
    permute((0..4).toList()).forEach { it ->
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

fun permute(list: List<Int>): List<List<Int>> {
    if (list.size == 1) {
        return listOf(list)
    }
    val permutations = mutableListOf<List<Int>>()
    val first = list.first()
    for (sublist in permute(list.drop(1)))
        for (i in 0..sublist.size) {
            val newPerm = sublist.toMutableList()
            newPerm.add(i, first)
            permutations.add(newPerm)
        }
    return permutations
}