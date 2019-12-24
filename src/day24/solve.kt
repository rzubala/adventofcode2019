package day24

import utils.copy
import utils.readInput
import java.lang.IllegalStateException
import java.util.Collections.max
import java.util.Collections.min
import kotlin.math.pow

const val SIZE = 5

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
    part1(map.copy())
    part2(map.copy())
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
            println("Part1: $sum")
            break
        }
        set.add(hash)
        i++
    }
}

fun part2(map: MatrixBool) {
    var levels: MutableMap<Int, MatrixBool> = mutableMapOf()
    levels[0] = map
    repeat(200) {
        val newLevels = mutableMapOf<Int, MatrixBool>()
        val min = min(levels.keys)
        val max = max(levels.keys)
        (min-1..max+1).forEach {level ->
            val newMap = step2(levels, level)
            if (newMap.bugs() > 0) {
                newLevels[level] = newMap
            }
        }
        levels = newLevels
    }
    println("Part2: ${count(levels)}")
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
            val bug = get(y)[x]
            val bugsAround = bugsAround(x, y)
            row.add(isNewBug(bugsAround, bug))
        }
    }
    return map
}

fun step2(levels: MutableMap<Int, MutableList<MutableList<Boolean>>>, level: Int): MatrixBool {
    val map = levels[level] ?: createEmpty()
    val newMap: MatrixBool = mutableListOf()
    for (y in (0 until map.size)) {
        val newRow = mutableListOf<Boolean>()
        newMap.add(newRow)
        for (x in (0 until map[y].size)) {
            if (y == 2 && x == 2) {
                newRow.add(false)
                continue
            }
            var bugsAround =
            if (((x == 1 || x == 3) && y == 2) || ((y == 1 || y == 3) && x == 2)){
                innerBugsAround(x, y, levels[level+1])
            } else if (x == 0 || y == 0 || x == SIZE - 1 || y == SIZE - 1) {
                outerBugsAround(x, y, levels[level-1])
            } else {
                0
            }
            bugsAround += map.bugsAround(x ,y)
            val bug = map[y][x]
            newRow.add(isNewBug(bugsAround, bug))
        }
    }
    return newMap
}

fun isNewBug(bugsAround: Int, bug: Boolean) = when (bugsAround) {
                                                1 -> true
                                                2 -> !bug
                                                else -> false
                                            }

fun createEmpty(): MutableList<MutableList<Boolean>> = MutableList(SIZE) { MutableList(SIZE) { false } }

fun innerBugsAround(x: Int, y: Int, map: MatrixBool?): Int {
    map?.let { iMap ->
        return if (x == 2 && y == 1) {
            iMap[0].map { if(it) 1 else 0}.sum()
        } else if (x == 2 && y == 3) {
            iMap[SIZE-1].map { if(it) 1 else 0}.sum()
        } else if (x == 1 && y == 2) {
            iMap.sumColumn(0)
        } else if (x == 3 && y == 2) {
            iMap.sumColumn(SIZE-1)
        } else {
            throw IllegalStateException("Not know $x $y")
        }
    }
    return 0
}

fun outerBugsAround(x: Int, y: Int, map: MatrixBool?): Int {
    map?.let { iMap ->
        return when {
            y == 0 -> {
                var cnt = iMap.getValue(2, 1)
                if (x == 0) {
                    cnt += iMap.getValue(1, 2)
                } else if (x == SIZE - 1) {
                    cnt += iMap.getValue(3, 2)
                }
                cnt
            }
            y == SIZE - 1 -> {
                var cnt = iMap.getValue(2, 3)
                if (x == 0) {
                    cnt += iMap.getValue(1, 2)
                } else if (x == SIZE - 1) {
                    cnt += iMap.getValue(3, 2)
                }
                cnt
            }
            x == 0 -> {
                iMap.getValue(1, 2)
            }
            x == SIZE - 1 -> {
                iMap.getValue(3, 2)
            }
            else -> {
                throw IllegalStateException("Not know $x $y")
            }
        }
    }
    return 0
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

fun MatrixBool.sumColumn(c: Int): Int {
    var sum = 0
    forEach { line ->
        sum += if (line[c]) 1 else 0
    }
    return sum
}

fun MatrixBool.copy(): MatrixBool {
    val map: MatrixBool = mutableListOf()
    forEach { line ->
        map.add(line.copy())
    }
    return map
}

fun MatrixBool.bugs(): Int {
    var sum = 0
    forEach { line ->
        sum += line.map{ if(it) 1 else 0}.sum()
    }
    return sum
}

fun count(levels: MutableMap<Int, MutableList<MutableList<Boolean>>>): Int {
    var cnt = 0
    levels.keys.forEach{ k ->
        cnt += levels[k]?.bugs() ?: 0
    }
    return cnt
}
