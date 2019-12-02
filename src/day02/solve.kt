package day02

import utils.readInput

const val part2value = 19690720

fun main() {
    val opcodes = readInput("src/day02/input.data")[0].split(",").map { it.toInt() }.toMutableList()
    //part1
    val list = mutableListOf(*opcodes.toTypedArray())
    println("part1: ${calculate(list, 12, 2)}")

    //part2
    for(noun in 0..99) {
        for(verb in 0..99) {
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
    opcodes[1] = noun
    opcodes[2] = verb
    for (i in 0..opcodes.size step 4) {
        val op = opcodes[i]
        if (op == 99) {
            break
        }
        val data1 = opcodes[i + 1]
        val data2 = opcodes[i + 2]
        val dst = opcodes[i + 3]
        opcodes[dst] = when (op) {
            1 -> opcodes[data1] + opcodes[data2]
            2 -> opcodes[data1] * opcodes[data2]
            else -> {
                println("error!")
                return -1
            }
        }
    }
    return opcodes[0]
}