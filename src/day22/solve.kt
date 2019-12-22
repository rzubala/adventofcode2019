package day22

import utils.copy
import utils.readInput
import java.lang.IllegalStateException
import java.lang.Math.abs

const val INC = "deal with increment"
const val NEW = "deal into new stack"
const val CUT = "cut"

const val CARDS = 10007L
//const val CARDS = 119315717514047
const val SHUFFLES = 101741582076661

fun main() {
    var data: MutableList<Long> = (0 until CARDS).toMutableList()

    val operations: MutableList<(list: MutableList<Long>) -> MutableList<Long>> = mutableListOf()

    readInput("src/day22/input.data").forEach { line ->
        when {
            line.startsWith(INC) -> {
                val num = line.drop(INC.length).trim().toInt()
                operations.add { list: MutableList<Long> -> inc(list, num) }
            }
            line.startsWith(NEW) -> {
                operations.add { list: MutableList<Long> -> reverse(list) }
            }
            line.startsWith(CUT) -> {
                val num =line.drop(CUT.length).trim().toInt()
                operations.add { list: MutableList<Long> -> cut(list, num) }
            }
            else -> throw IllegalStateException("Not known command")
        }
    }

    operations.forEach { op ->
        data = op(data)
    }

    println("Part1 ${data.indexOf(2019)}")

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

fun reverse(data: MutableList<Long>): MutableList<Long> {
    data.reverse()
    return data
}

fun inc(data: MutableList<Long>, num: Int): MutableList<Long> {
    val list = MutableList(data.size) { 0L }
    var cnt = 0
    data.forEachIndexed{ i, v ->
        list[cnt] = v
        cnt = (cnt + num) % list.size
    }
    return list
}

fun cut(data: MutableList<Long>, cut: Int): MutableList<Long> {
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

