package day24

import utils.readInput
import java.lang.IllegalStateException
import kotlin.math.pow

typealias MatrixBool = MutableList<MutableList<Boolean>>
fun main() {
    val map: MatrixBool = mutableListOf()
    readInput("src/day24/input.data").forEach { line ->
        map.add(line.toCharArray().toList().map {
            when(it) {
                '.' -> false
                '#' -> true
                else -> throw IllegalStateException("Not known symbol $it")
            }
        }.toMutableList())
    }

    map.print()
    part1(map)
}

fun part1(mapOrg: MatrixBool) {
    val set = mutableSetOf<String>()
    var map = mapOrg
    var i = 0
    while (true) {
        map = map.step()
        val hash = map.hash()
        if (set.contains(hash)) {
            val sum = map.count()
            println("found $sum $hash")
            map.print()
            break
        }
        set.add(hash)
        i++
    }
}

fun MatrixBool.hash(): String {
    var result = ""
    forEach { line ->
        result += line.joinToString (""){ if (it) "#" else "." }
    }
    return result
}

fun MatrixBool.count(): Long {
    var sum = 0L
    var n = 0
    forEach { line ->
        line.forEach { c ->
            sum += if (c) 2.0.pow(n.toDouble()).toLong() else 0
            n++
        }
    }
    return sum
}

fun MatrixBool.step(): MatrixBool {
    val map: MatrixBool = mutableListOf()
    for (y in (0 until size)) {
        val row = mutableListOf<Boolean>()
        map.add(row)
        for (x in (0 until get(y).size)) {
            val b = get(y)[x]
            val newB = when(bugsAround(x, y)) {
                1 -> true
                2 -> !b
                else -> false
            }
            row.add(newB)
        }
    }
    return map
}

fun MatrixBool.bugsAround(x: Int, y: Int): Int {
    var cnt = getValue(x-1, y)
    cnt += getValue(x, y-1)
    cnt += getValue(x+1, y)
    cnt += getValue(x, y+1)
    return cnt
}

fun MatrixBool.getValue(x: Int, y: Int): Int {
    if (x < 0 || y < 0 || y >= size || x >= get(y).size) {
        return 0
    }
    return if (get(y)[x]) 1 else 0
}

fun MatrixBool.print() {
    forEach { line ->
        line.forEach { c ->
            print(if(c) '#' else '.')
        }
        println()
    }
}