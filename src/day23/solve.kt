package day23

import utils.IntCode
import utils.copy
import utils.readInput
import kotlin.concurrent.thread

fun main() {
    val code = readInput("src/day23/input.data")[0].split(",").map { it.toLong() }

    val data: MutableMap<Int, MutableList<Long>> = mutableMapOf()
    (0..49).forEach { i ->
        data[i] = mutableListOf<Long>().apply { add(i.toLong()) }
    }

    class Nat() {
        var x: Long? = null
        var y: Long? = null

        fun monitor() {
            x?.let { ix ->
                y?.let { iy ->
                    if (isIdle()) {
                        data[0]?.add(ix)
                        data[0]?.add(iy)
                        println("NAT $iy")
                    }
                }
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
        var to: Int? = null
        var outCnt = 0
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
                        if (to!! > 49) {
                            nat.x = out
                            //println("[$address] X $outCnt $out")
                        } else {
                            data[to!!]!!.add(out)
                        }
                    }
                    2 -> {
                        if (to!! > 49) {
                            println("FOUND 255 $to $out")
                            nat.y = out
                            //println("[$address] Y $outCnt $out")
                        } else {
                            data[to!!]!!.add(out)
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
            return input.removeAt(0)
        }
    }

    (0..49).forEach { i ->
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
