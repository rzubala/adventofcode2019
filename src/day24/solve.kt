package day24

import utils.copy
import utils.readInput
import java.lang.IllegalStateException
import kotlin.math.pow

const val SIZE = 5

typealias MatrixBool = MutableList<MutableList<Boolean>>
fun main() {
    val map: MatrixBool = mutableListOf()
    readInput("src/day24/test.data").forEach { line ->
        map.add(line.toCharArray().toList().map {
            when(it) {
                '.' -> false
                '#' -> true
                else -> throw IllegalStateException("Not known symbol $it")
            }
        }.toMutableList())
    }

    map.print()
    //part1(map)

    part2(map.copy())
}

fun part2(mapOrg: MatrixBool) {
    val levels: MutableMap<Int, MatrixBool> = mutableMapOf()
    levels[0] = mapOrg

    step2(levels, 0)
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

fun step2(levels: MutableMap<Int, MutableList<MutableList<Boolean>>>, level: Int) {
    val map = levels[level] ?: createEmpty()
    val newMap: MatrixBool = mutableListOf()
    for (y in (0 until map.size)) {
        val row = mutableListOf<Boolean>()
        newMap.add(row)
        for (x in (0 until map[y].size)) {
            val b = map[y][x]
            //middle element
            if (y == 2 || x == 2) {
                row.add(false)
                continue
            }
            //around next level
            if (((x == 1 || x == 3) && y == 2) || ((y == 1 || y == 3) && x == 2)){
                //check outside level (inner part)
                continue
            }

            //outside
            if (x == 0 || y == 0 || x == SIZE - 1 || y == SIZE - 1) {
                //check inner level (outside part)
                continue
            }

            //else
            val newB = when(map.bugsAround(x, y)) {
                1 -> true
                2 -> !b
                else -> false
            }
            row.add(newB)
        }
    }
}

fun createEmpty(): MutableList<MutableList<Boolean>> = MutableList(SIZE) { MutableList(SIZE) { false } }

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

fun MatrixBool.copy(): MatrixBool {
    val map: MatrixBool = mutableListOf()
    forEach { line ->
        map.add(line.copy())
    }
    return map
}