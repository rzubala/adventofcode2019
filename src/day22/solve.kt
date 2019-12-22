package day22

import utils.copy
import utils.readInput
import java.lang.IllegalStateException
import java.math.BigInteger

const val INC = "deal with increment"
const val NEW = "deal into new stack"
const val CUT = "cut"

const val CARDS = 10007L
const val CARDS2 = 119315717514047
const val SHUFFLES = 101741582076661

typealias ListOperation = (list: MutableList<Long>) -> MutableList<Long>

fun main() {
    val input = mutableListOf<String>()
    readInput("src/day22/input.data").forEach { line ->
        input.add(line)
    }
    part1(input.copy())
    part2(input.copy())
}

fun part1(input: List<String>) {
    val operations: MutableList<ListOperation> = mutableListOf()
    input.forEach { line ->
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
    var data: MutableList<Long> = (0 until CARDS).toMutableList()
    operations.forEach { op ->
        data = op(data)
    }
    println("Part1 ${data.indexOf(2019)}")
}

fun part2(input: MutableList<String>) {
    val cards = BigInteger.valueOf(CARDS2)
    val shuffles = BigInteger.valueOf(SHUFFLES)
    val toFind = BigInteger.valueOf(2020L)
    var offset = BigInteger.valueOf(0L)
    var increment = BigInteger.valueOf(1L)
    input.reverse()
    input.forEach { line ->
        when {
            line.startsWith(INC) -> {
                val inc = BigInteger.valueOf(line.drop(INC.length).trim().toLong())
                val tmp = inc.inv(cards)
                increment = increment.times(tmp)
                offset = offset.times(tmp)
            }
            line.startsWith(NEW) -> {
                offset = offset.plus(BigInteger.ONE)
                offset = offset.times(-BigInteger.ONE)
                increment = increment.times(-BigInteger.ONE)
            }
            line.startsWith(CUT) -> {
                val inc =line.drop(CUT.length).trim().toLong()
                offset = offset.plus(BigInteger.valueOf(inc))
            }
            else -> throw IllegalStateException("Not known command")
        }
        increment = increment.mod(cards)
        offset = offset.mod(cards)
    }
    val result = (increment.modPow(shuffles, cards) * toFind
                + offset * (increment.modPow(shuffles, cards) + cards.minus(BigInteger.ONE)) * increment.minus(BigInteger.ONE).inv(cards)
            ).mod(cards)
    println("Part2 $result")
}

fun reverse(data: MutableList<Long>): MutableList<Long> {
    data.reverse()
    return data
}

fun inc(data: MutableList<Long>, num: Int): MutableList<Long> {
    val list = MutableList(data.size) { 0L }
    var cnt = 0
    data.forEachIndexed{ _, v ->
        list[cnt] = v
        cnt = (cnt + num) % list.size
    }
    return list
}

fun cut(data: MutableList<Long>, cut: Int): MutableList<Long> {
    var list = data
    var toTake = cut
    if (cut < 0) {
       toTake = list.size - kotlin.math.abs(cut)
    }
    val tmp = list.take(toTake)
    list = list.drop(toTake).toMutableList()
    list.addAll(tmp)
    return list
}

fun BigInteger.inv(n: BigInteger): BigInteger = modPow(n.minus(BigInteger.TWO), n)