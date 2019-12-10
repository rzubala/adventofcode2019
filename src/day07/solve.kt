package day07

import utils.IntCode
import utils.copy
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

class Amplifier(private val code: List<Long>, private val dataInput: DataInput, private val pipeTo: DataInput) {
    fun start(): Long {
        return IntCode(code.copy()).run({dataInput.get()}) { out -> pipeTo.add(out)}
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
        thread(start = true) {
            Amplifier(opcodes, in0, in1).start()
        }
        thread(start = true) {
            Amplifier(opcodes, in1, in2).start()
        }
        thread(start = true) {
            Amplifier(opcodes, in2, in3).start()
        }
        thread(start = true) {
            Amplifier(opcodes, in3, in4).start()
        }
        thread(start = true) {
            val output = Amplifier(opcodes, in4, in0).start()
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