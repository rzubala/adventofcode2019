package day07

import utils.copy
import utils.intCode
import utils.readInput
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock

fun main() {
    val opcodes = readInput("src/day07/input.data")[0].split(",").map { it.toLong() }
    generate(opcodes, 0L..4L)
    generate(opcodes, 5L..9L)
}

class DataInput {
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()
    private var list: MutableList<Long> = mutableListOf()
    fun add(value: Long) = lock.withLock {
        list.add(value)
        condition.signalAll()
    }
    fun get(): Long = lock.withLock {
        while (list.isEmpty()) {
            condition.await()
        }
        return list.removeAt(0)
    }
}

class Amplifier(private val code: MutableList<Long>, private val dataInput: DataInput, private val pipeTo: DataInput) {
    fun start(): Long {
        return intCode(code, {dataInput.get()}) { out -> pipeTo.add(out)}
    }
}

private fun generate(opcodes: List<Long>, range: LongRange) {
    var max = Long.MIN_VALUE
    range.toList().permute().forEach {
        val in0 = DataInput().apply { add(it[0]); add(0) }
        val in1 = DataInput().apply { add(it[1]) }
        val in2 = DataInput().apply { add(it[2]) }
        val in3 = DataInput().apply { add(it[3]) }
        val in4 = DataInput().apply { add(it[4]) }
        val amp0 = Amplifier(opcodes.copy(), in0, in1)
        val amp1 = Amplifier(opcodes.copy(), in1, in2)
        val amp2 = Amplifier(opcodes.copy(), in2, in3)
        val amp3 = Amplifier(opcodes.copy(), in3, in4)
        val amp4 = Amplifier(opcodes.copy(), in4, in0)
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

fun List<Long>.permute(): List<List<Long>> {
    if (size == 1) {
        return listOf(this)
    }
    val permutations = mutableListOf<List<Long>>()
    val first = first()
    for (sublist in drop(1).permute())
        for (i in 0..sublist.size) {
            val newPerm = sublist.toMutableList()
            newPerm.add(i, first)
            permutations.add(newPerm)
        }
    return permutations
}