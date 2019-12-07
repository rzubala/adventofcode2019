package day07

import day02.copy
import day05.IntInput
import day05.IntOutput
import day05.intCode
import utils.readInput
import kotlin.concurrent.thread

fun main() {
    val opcodes = readInput("src/day07/input.data")[0].split(",").map { it.toInt() }
    generate(opcodes, 0..4)
    generate(opcodes, 5..9)
}

class DataInput(private val lock: Object) : IntInput {
    private var list: MutableList<Int> = mutableListOf()
    fun add(value: Int) = synchronized(lock) {
        list.add(value)
        lock.notifyAll()
    }
    override fun get(): Int = synchronized(lock) {
        while (list.isEmpty()) {
            lock.wait()
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

private fun generate(opcodes: List<Int>, range: IntRange) {
    var max = Integer.MIN_VALUE
    permute(range.toList()).forEach {
        val in0 = DataInput(Object()).apply { add(it[0]); add(0) }
        val in1 = DataInput(Object()).apply { add(it[1]) }
        val in2 = DataInput(Object()).apply { add(it[2]) }
        val in3 = DataInput(Object()).apply { add(it[3]) }
        val in4 = DataInput(Object()).apply { add(it[4]) }

        val out0 = DataOutput(in1)
        val out1 = DataOutput(in2)
        val out2 = DataOutput(in3)
        val out3 = DataOutput(in4)
        val out4 = DataOutput(in0)

        val amp0 = Amplifier(opcodes.copy(), in0, out0)
        val amp1 = Amplifier(opcodes.copy(), in1, out1)
        val amp2 = Amplifier(opcodes.copy(), in2, out2)
        val amp3 = Amplifier(opcodes.copy(), in3, out3)
        val amp4 = Amplifier(opcodes.copy(), in4, out4)

        thread(start = true) {
            amp0.start()
        }
        thread(start = true) {
            amp1.start()
        }
        thread(start = true) {
            amp2.start()
        }
        thread(start = true) {
            amp3.start()
        }
        thread(start = true) {
            val output = amp4.start()
            if (output > max) {
                max = output
            }
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