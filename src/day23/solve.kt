package day23

import utils.IntCode
import utils.copy
import utils.readInput
import kotlin.concurrent.thread

data class Packet(var x: Long, var y: Long = -1L, var hasY: Boolean = true, var readX: Boolean = false)

const val LAST = 49

fun main() {
    val code = readInput("src/day23/input.data")[0].split(",").map { it.toLong() }

    val data: MutableMap<Int, MutableList<Packet>> = mutableMapOf()
    (0..LAST).forEach { i ->
        data[i] = mutableListOf<Packet>().apply { add(Packet(i.toLong(), hasY = false)) }
    }

    class Nat {
        var packet = Packet(-1, -1)
        var lastY = -1L
        var start: Boolean = false
            set(value) {
                if (!start) {
                    println("NAT start ${packet.y}")
                }
                field = value
            }
        fun monitor() {
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

        fun isIdle(): Boolean {
            data.keys.forEach{ k ->
                if (data[k]!!.isNotEmpty()) {
                    //println("Not empty $k")
                    return false
                }
            }
            return true
        }
    }
    val nat = Nat()

    class Computer(val address: Int) {
        var to: Int = 0
        var outCnt = 0
        val packet = Packet(-1, -1)
        fun start(): Long {
            return IntCode(code.copy()).run({
                val i = getInput()
                //if (i>-1) println("[$address] < $i")
                i
            }
            ) { out ->
                when(outCnt) {
                    0 -> to = out.toInt()
                    1 -> {
                        if (to > LAST) {
                            packet.x = out
                            //println("[$address] X $outCnt $out")
                        } else {
                            //data[to!!]!!.add(out)
                            packet.x = out
                        }
                    }
                    2 -> {
                        if (to > LAST) {
                            //println("FOUND $to $out")
                            packet.y = out
                            nat.packet = packet.copy()
                            nat.start = true
                            //println("[$address] Y $outCnt $out")
                        } else {
                            //data[to!!]!!.add(out)
                            packet.y = out
                            data[to]?.add(packet.copy())
                        }
                    }

                }
                //println("[$address] $outCnt > $out")
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

    (0..LAST).forEach { i ->
        thread(start = true) {
            Computer(i).start()
        }
    }

    thread(start=true) {
        while(true) {
            Thread.sleep(10)
            nat.monitor()
        }
    }
}
