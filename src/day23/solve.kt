package day23

import utils.IntCode
import utils.copy
import utils.readInput
import kotlin.concurrent.thread

data class Packet(var x: Long, var y: Long = -1L, var hasY: Boolean = true, var readX: Boolean = false)

const val LAST = 49

private val data: MutableMap<Int, MutableList<Packet>> = mutableMapOf()

fun main() {
    val code = readInput("src/day23/input.data")[0].split(",").map { it.toLong() }
    (0..LAST).forEach { i ->
       data[i] = mutableListOf<Packet>().apply { add(Packet(i.toLong(), hasY = false)) }
    }
    val nat = Nat()
    (0..LAST).forEach { i ->
        thread(start = true) {
            Computer(i, code.copy(), nat).start()
        }
    }
}

class Nat {
    var packet = Packet(-1, -1)
    private var lastY = -1L
    var start: Boolean = false
        set(value) {
            if (value && !start) {
                println("NAT start ${packet.y}")
                run()
                field = value
            }
        }
    fun run() {
        println("NAT run")
        thread(start=true) {
            while(true) {
                Thread.sleep(200)
                monitor()
            }
        }
    }
    private fun monitor() {
        if (start && packet.x > 0 && packet.y > 0 && isIdle()) {
            data[0]?.add(packet.copy())
            if (lastY == packet.y) {
                println("NAT repeat ${packet.y}")
            }
            lastY = packet.y
            packet.x = -1
            packet.y = -1
        }
    }

    private fun isIdle(): Boolean {
        data.keys.forEach{ k ->
            if (data[k]!!.isNotEmpty()) {
                return false
            }
        }
        return true
    }
}

class Computer(private val address: Int, private val code: MutableList<Long>, private val nat: Nat) {
    var to: Int = 0
    var outCnt = 0
    private val packet = Packet(-1, -1)
    fun start(): Long {
        return IntCode(code).run({
            getInput()
        }
        ) { out ->
            when(outCnt) {
                0 -> to = out.toInt()
                1 -> packet.x = out
                2 -> {
                    packet.y = out
                    if (to > LAST) {
                        nat.packet = packet.copy()
                        nat.start = true
                    } else {
                        data[to]?.add(packet.copy())
                    }
                }
            }
            outCnt = (outCnt + 1) % 3
        }
    }
    private fun getInput(): Long {
        val input = data[address]!!
        if (input.isEmpty()) {
            return -1L
        }
        val packet = input[0]
        return if (packet.readX) {
            input.removeAt(0)
            if (packet.hasY) {
                packet.y
            } else {
                getInput()
            }
        } else {
            packet.readX = true
            packet.x
        }
    }
}
