package day02

import utils.readInput

const val part2value = 19690720

fun main() {
    val opcodes = readInput("src/day02/input.data")[0].split(",").map { it.toInt() }.toMutableList()
    //part1
    val list = mutableListOf(*opcodes.toTypedArray())
    println("part1: ${calculate(list, 12, 2)}")

    //part2
    (0..99).forEach { noun ->
        (0..99).forEach { verb ->
            val list = mutableListOf(*opcodes.toTypedArray())
            val res = calculate(list, noun, verb)
            if (res == part2value) {
                println("part2: ${noun * 100 + verb}")
                return
            }
        }
    }
}

private fun calculate(opcodes: MutableList<Int>, noun: Int, verb: Int): Int {
    opcodes.apply {
        this[1] = noun
        this[2] = verb
    }
    (0..opcodes.size step 4).forEach { i ->
        val op = opcodes[i]
        if (op == 99) {
            return opcodes[0]
        }
        val data1 = opcodes[i + 1]
        val data2 = opcodes[i + 2]
        val dst = opcodes[i + 3]
        opcodes[dst] = when (op) {
            1 -> opcodes[data1] + opcodes[data2]
            2 -> opcodes[data1] * opcodes[data2]
            else -> throw IllegalStateException("Operation not found $dst")
        }
    }
    throw IllegalStateException("Illegal state")
}