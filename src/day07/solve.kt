package day07

import day02.copy
import day05.IntInput
import day05.IntOutput
import day05.intCode
import utils.readInput
import java.lang.IllegalStateException

fun main() {
    val opcodes = readInput("src/day07/input.data")[0].split(",").map { it.toInt() }
    part1(opcodes)
}

class DataInput : IntInput {
    private var list: MutableList<Int> = mutableListOf()
    fun add(value: Int) {
        list.add(value)
    }
    override fun get(): Int {
        if (list.isEmpty()) {
            throw IllegalStateException("Empty list")
        }
        return list.removeAt(0)
    }
}

class DataOutput(private val input: DataInput): IntOutput {
    override fun handle(out: Int) {
        input.add(out)
    }
}

class Amplifier(private val code: MutableList<Int>, private val dataInput: DataInput, private val dataOutput: IntOutput) {
    fun start(): Int {
        return intCode(code, dataInput, dataOutput)
    }
}

private fun part1(opcodes: List<Int>) {
    var max = Integer.MIN_VALUE

    permute((0..4).toList()).forEach { 
        val in0 = DataInput().apply { add(it[0]); add(0) }
        val in1 = DataInput().apply { add(it[1]) }
        val in2 = DataInput().apply { add(it[2]) }
        val in3 = DataInput().apply { add(it[3]) }
        val in4 = DataInput().apply { add(it[4]) }

        val out0 = DataOutput(in1)
        val out1 = DataOutput(in2)
        val out2 = DataOutput(in3)
        val out3 = DataOutput(in4)
        val out4 = object : IntOutput {
            override fun handle(out: Int) {
                if (out > max) {
                    max = out
                }
            }
        }

        val amp0 = Amplifier(opcodes.copy(), in0, out0)
        val amp1 = Amplifier(opcodes.copy(), in1, out1)
        val amp2 = Amplifier(opcodes.copy(), in2, out2)
        val amp3 = Amplifier(opcodes.copy(), in3, out3)
        val amp4 = Amplifier(opcodes.copy(), in4, out4)

        amp0.start()
        amp1.start()
        amp2.start()
        amp3.start()
        amp4.start()
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