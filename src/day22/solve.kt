package day22

import utils.copy
import utils.readInput
import java.lang.IllegalStateException
import java.lang.Math.abs

const val INC = "deal with increment"
const val NEW = "deal into new stack"
const val CUT = "cut"

const val CARDS = 10007
//const val CARDS = 10

fun main() {

    var list: MutableList<Int> = (0 until CARDS).toMutableList()

    val data = readInput("src/day22/input.data").forEach { line ->
        println(line)
        when {
            line.startsWith(INC) -> {
                val num = line.drop(INC.length).trim().toInt()
                list = inc(list, num)
                println("inc $num")
            }
            line.startsWith(NEW) -> {
                println("new")
                list.reverse()
            }
            line.startsWith(CUT) -> {
                val num =line.drop(CUT.length).trim().toInt()
                println("cut $num")
                list = cut(list, num)
            }
            else -> throw IllegalStateException("Not known command")
        }
    }
    //println(list.toString())
    println("Part1 ${list.indexOf(2019)}")

    test()
}

fun test() {
//    var list: MutableList<Int> = (0 until CARDS).toMutableList()
//    cut(list, 3)
//    list = (0 until CARDS).toMutableList()
//    cut(list, -4)
//    var list: MutableList<Int> = (0 until 10).toMutableList()
//    println(list)
//    list = inc(list, 3)
//    println(list)
}

fun inc(data: MutableList<Int>, num: Int): MutableList<Int> {
    val list = MutableList(data.size) { 0 }
    var cnt = 0
    data.forEachIndexed{ i, v ->
        list[cnt] = v
        cnt = (cnt + num) % list.size
    }
    return list
}

fun cut(data: MutableList<Int>, cut: Int): MutableList<Int> {
    var list = data
    //println(list)
    var toTake = cut
    if (cut < 0) {
       toTake = list.size - kotlin.math.abs(cut)
    }
    val tmp = list.take(toTake)
    list = list.drop(toTake).toMutableList()
    list.addAll(tmp)
    //println(list)
    return list
}

