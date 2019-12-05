package day05

import utils.readInput

fun main() {
    val opcodes = readInput("src/day05/input.data")[0].split(",").map { it.toInt() }
    process(opcodes.copy(), "1")
}

fun List<Int>.copy(): MutableList<Int> = mutableListOf(*this.toTypedArray())

private fun process(opcodes: MutableList<Int>, input: String) {
    var i = 0
    while (i < opcodes.size) {
        val instruction = opcodes[i].toString().padStart(5, '0')
        val op = "${instruction[3]}${instruction[4]}".toInt()
        val mode1 = instruction[2].toString().toInt()
        val mode2 = instruction[1].toString().toInt()
        when (op) {
            1 -> {
                val dst = opcodes[i + 3]
                opcodes[dst] = getData(opcodes, i + 1, mode1) + getData(opcodes, i + 2, mode2)
                i += 4
            }
            2 -> {
                val dst = opcodes[i + 3]
                opcodes[dst] = getData(opcodes, i + 1, mode1) * getData(opcodes, i + 2, mode2)
                i += 4
            }
            3 -> {
                val dst = opcodes[i + 1]
                opcodes[dst] = input.toInt()
                i += 2
            }
            4 -> {
                println("out> ${getData(opcodes, i + 1, mode1)}")
                i += 2
            }
            99 -> return
            else -> throw IllegalStateException("Operation not found $op")
        }
    }
    throw IllegalStateException("Illegal state")
}

fun getData(data: MutableList<Int>, pointer: Int, mode: Int): Int {
    return when(mode) {
        0 -> data[data[pointer]]
        1 -> data[pointer]
        else -> throw IllegalStateException("Mode $mode not supported")
    }
}
