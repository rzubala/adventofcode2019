package utils

class IntCode(private val opcodes: MutableList<Long>) {
    private var relativeBase = 0L
    private var programCnt = 0L
    private var out = 0L

    fun run(input: () -> Long, output: (value: Long) -> Unit): Long {
        while (programCnt < opcodes.size) {
            val instruction = opcodes[programCnt.toInt()].toString().padStart(5, '0')
            val op = "${instruction[3]}${instruction[4]}".toLong()
            val mode1 = instruction[2].toString().toLong()
            val mode2 = instruction[1].toString().toLong()
            val mode3 = instruction[0].toString().toLong()
            when (op) {
                1L -> {
                    val dst = opcodes.getIndex(mode3, 3)
                    opcodes.setData(dst, opcodes.getData(1, mode1).plus(opcodes.getData(2, mode2)))
                    programCnt += 4
                }
                2L -> {
                    val dst = opcodes.getIndex(mode3, 3)
                    opcodes.setData(dst, opcodes.getData(1, mode1).times(opcodes.getData(2, mode2)))
                    programCnt += 4
                }
                3L -> {
                    val dst = opcodes.getIndex(mode1, 1)
                    opcodes.setData(dst, input())
                    programCnt += 2
                }
                4L -> {
                    out = opcodes.getData(1, mode1)
                    output(out)
                    programCnt += 2
                }
                5L -> {
                    programCnt = if (opcodes.getData(1, mode1) > 0L) {
                        opcodes.getData(2, mode2)
                    } else {
                        programCnt + 3
                    }
                }
                6L -> {
                    programCnt = if (opcodes.getData(1, mode1) == 0L) {
                        opcodes.getData(2, mode2)
                    } else {
                        programCnt + 3
                    }
                }
                7L -> {
                    val dst = opcodes.getIndex(mode3, 3)
                    opcodes.setData(dst, if (opcodes.getData(1, mode1) < opcodes.getData(2, mode2)) {
                        1
                    } else {
                        0
                    })
                    programCnt += 4
                }
                8L -> {
                    val dst = opcodes.getIndex(mode3, 3)
                    opcodes.setData(dst,if (opcodes.getData(1, mode1) == opcodes.getData(2, mode2)) {
                        1
                    } else {
                        0
                    })
                    programCnt += 4
                }
                9L -> {
                    val offset = opcodes.getData(1, mode1)
                    relativeBase += offset
                    programCnt += 2
                }
                99L -> return out
                else -> throw IllegalStateException("Operation not found $op")
            }
        }
        throw IllegalStateException("Illegal state")
    }

    private fun MutableList<Long>.getData(indexOffset: Long, mode: Long): Long {
        val index = getIndex(mode, indexOffset)
        if (index > this.size - 1) {
            this.grow(index)
        }
        return this[index.toInt()]
    }

    private fun MutableList<Long>.getIndex(mode: Long, indexOffset: Long): Long {
        return when (mode) {
            0L -> this[programCnt.plus(indexOffset).toInt()]
            1L -> programCnt.plus(indexOffset)
            2L -> this[programCnt.plus(indexOffset).toInt()] + relativeBase
            else -> throw IllegalStateException("Mode $mode not supported")
        }
    }

    private fun MutableList<Long>.setData(index: Long, value: Long) {
        if (index > this.size - 1) {
            this.grow(index)
        }
        this[index.toInt()] = value
    }

    private fun MutableList<Long>.grow(index: Long) {
        this.addAll(MutableList(index.toInt() + 1 - this.size) {0L})
    }
}

fun List<Long>.copy(): MutableList<Long> = mutableListOf(*this.toTypedArray())