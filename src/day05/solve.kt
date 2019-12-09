package day05

import utils.readInput

fun main() {
    val opcodes = readInput("src/day05/input.data")[0].split(",").map { it.toLong() }
    println("${intCode(opcodes.copy(), {1}) {} }")
    println("${intCode(opcodes.copy(), {5}) {} }")
}

fun intCode(opcodes: MutableList<Long>, input: () -> Long, output: (value: Long) -> Unit): Long {
    var relativeBase = 0L
    var i = 0L
    var out = 0L
    while (i < opcodes.size) {
        val instruction = opcodes[i.toInt()].toString().padStart(5, '0')
        val op = "${instruction[3]}${instruction[4]}".toLong()
        val mode1 = instruction[2].toString().toLong()
        val mode2 = instruction[1].toString().toLong()
        when (op) {
            1L -> {
                val dst = opcodes[(i + 3).toInt()]
                opcodes.setData(dst, opcodes.getData(i + 1, mode1, relativeBase) + opcodes.getData(i + 2, mode2, relativeBase))
                i += 4
            }
            2L -> {
                val dst = opcodes[(i + 3).toInt()]
                opcodes.setData(dst, opcodes.getData(i + 1, mode1, relativeBase) * opcodes.getData(i + 2, mode2, relativeBase))
                i += 4
            }
            3L -> {
                val dst = opcodes[(i + 1).toInt()]
                opcodes.setData(dst, input())
                i += 2
            }
            4L -> {
                out = opcodes.getData(i + 1, mode1, relativeBase)
                output(out)
                i += 2
            }
            5L -> {
                i = if (opcodes.getData(i + 1, mode1, relativeBase) > 0L) {
                    opcodes.getData(i + 2, mode2, relativeBase)
                } else {
                    i + 3
                }
            }
            6L -> {
                i = if (opcodes.getData(i + 1, mode1, relativeBase) == 0L) {
                    opcodes.getData(i + 2, mode2, relativeBase)
                } else {
                    i + 3
                }
            }
            7L -> {
                val dst = opcodes[(i + 3).toInt()]
                opcodes.setData(dst,if (opcodes.getData(i + 1, mode1, relativeBase) < opcodes.getData(i + 2, mode2, relativeBase)) {
                    1
                } else {
                    0
                })
                i += 4
            }
            8L -> {
                val dst = opcodes[(i + 3).toInt()]
                opcodes.setData(dst,if (opcodes.getData(i + 1, mode1, relativeBase) == opcodes.getData(i + 2, mode2, relativeBase)) {
                    1
                } else {
                    0
                })
                i += 4
            }
            9L -> {
                val offset = opcodes.getData(i + 1, mode1, relativeBase)
                relativeBase += offset
                i += 2
            }
            99L -> return out
            else -> throw IllegalStateException("Operation not found $op")
        }
    }
    throw IllegalStateException("Illegal state")
}

fun MutableList<Long>.getData(pointer: Long, mode: Long, base: Long): Long {
    val index = when(mode) {
        0L -> this[pointer.toInt()]
        1L -> pointer
        2L -> this[pointer.toInt()] + base
        else -> throw IllegalStateException("Mode $mode not supported")
    }
    if (index > this.size - 1) {
        this.grow(index)
    }
    return this[index.toInt()]
}

fun MutableList<Long>.setData(index: Long, value: Long) {
    if (index > this.size - 1) {
        this.grow(index)
    }
    this[index.toInt()] = value
}

fun MutableList<Long>.grow(index: Long) {
    this.addAll(MutableList(index.toInt() + 1 - this.size) {0L})
}

fun List<Long>.copy(): MutableList<Long> = mutableListOf(*this.toTypedArray())