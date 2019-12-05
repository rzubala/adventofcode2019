package day05

import utils.readInput

fun main() {
    val opcodes = readInput("src/day05/input.data")[0].split(",").map { it.toInt() }
    process(opcodes.copy(), "1")
    process(opcodes.copy(), "5")
}

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
                opcodes[dst] = opcodes.getData(i + 1, mode1) + opcodes.getData(i + 2, mode2)
                i += 4
            }
            2 -> {
                val dst = opcodes[i + 3]
                opcodes[dst] = opcodes.getData(i + 1, mode1) * opcodes.getData(i + 2, mode2)
                i += 4
            }
            3 -> {
                val dst = opcodes[i + 1]
                opcodes[dst] = input.toInt()
                i += 2
            }
            4 -> {
                println("out> ${opcodes.getData(i + 1, mode1)}")
                i += 2
            }
            5 -> {
                i = if (opcodes.getData(i + 1, mode1) > 0) {
                    opcodes.getData(i + 2, mode2)
                } else {
                    i + 3
                }
            }
            6 -> {
                i = if (opcodes.getData(i + 1, mode1) == 0) {
                    opcodes.getData(i + 2, mode2)
                } else {
                    i + 3
                }
            }
            7 -> {
                val dst = opcodes[i + 3]
                opcodes[dst] = if (opcodes.getData(i + 1, mode1) < opcodes.getData(i + 2, mode2)) {
                    1
                } else {
                    0
                }
                i += 4
            }
            8 -> {
                val dst = opcodes[i + 3]
                opcodes[dst] = if (opcodes.getData(i + 1, mode1) == opcodes.getData(i + 2, mode2)) {
                    1
                } else {
                    0
                }
                i += 4
            }
            99 -> return
            else -> throw IllegalStateException("Operation not found $op")
        }
    }
    throw IllegalStateException("Illegal state")
}

fun MutableList<Int>.getData(pointer: Int, mode: Int): Int {
    return when(mode) {
        0 -> this[this[pointer]]
        1 -> this[pointer]
        else -> throw IllegalStateException("Mode $mode not supported")
    }
}

fun List<Int>.copy(): MutableList<Int> = mutableListOf(*this.toTypedArray())