package day02

import utils.readInput

fun main() {
    val opcodes = readInput("src/day02/input.data")[0].split(",").map { it.toInt() }.toMutableList()
    opcodes[1] = 12
    opcodes[2] = 2
    println(opcodes)
    for (i in 0..opcodes.size step 4) {
        val op = opcodes[i]
        if (op == 99) {
            break
        }
        val data1 = opcodes[i + 1]
        val data2 = opcodes[i + 2]
        val dst = opcodes[i + 3]
        opcodes[dst] = when(op){
            1 -> opcodes[data1] + opcodes[data2]
            2 -> opcodes[data1] * opcodes[data2]
            else -> {
                println("error!")
                opcodes[dst]
            }
        }
    }
    println(opcodes[0])
}