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
    val dst = BigInteger.valueOf(2020L)
    var a = BigInteger.valueOf(1L)
    var b = BigInteger.valueOf(0L)
    input.reverse()
    input.forEach { line ->
        when {
            line.startsWith(INC) -> {
                val inc = line.drop(INC.length).trim().toInt().toLong()
                val p = pow(BigInteger.valueOf(inc), cards.minus(BigInteger.valueOf(2)), cards)
                a = a.times(p)
                b = b.times(p)
            }
            line.startsWith(NEW) -> {
                b = b.plus(BigInteger.valueOf(1))
                b = b.times(BigInteger.valueOf(-1))
                a = a.times(BigInteger.valueOf(-1))
            }
            line.startsWith(CUT) -> {
                val inc =line.drop(CUT.length).trim().toInt().toLong()
                b = b.plus(BigInteger.valueOf(inc))
            }
            else -> throw IllegalStateException("Not known command")
        }
        a = a.mod(BigInteger.valueOf(CARDS2))
        b = b.mod(BigInteger.valueOf(CARDS2))
    }
    //solution from https://github.com/sophiebits/adventofcode/blob/master/2019/day22.py
    //q
    //aq + b
    //a(aq+b) + b = a^2q + ab + b
    //a(a^2q + ab + b) = a^3q + a^2b + ab + b
    //...
    //a^t q + b * (a^t - 1) / (a - 1)
    val result = (pow(a, shuffles, cards) * dst + b * (pow(a, shuffles, cards) + cards.minus(BigInteger.valueOf(1)) ) * pow(a.minus(BigInteger.valueOf(1)), cards.minus(BigInteger.valueOf(2)), cards)).mod(cards)
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

fun pow(a: BigInteger, b: BigInteger, m: BigInteger): BigInteger = a.modPow(b, m)