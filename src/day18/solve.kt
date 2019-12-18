package day18

import day10.Point
import day17.Directions
import day17.up
import utils.readInput
import java.lang.IllegalStateException

fun main() {
    val keysMap: MutableMap<Char, Point> = mutableMapOf()
    val doorsMap: MutableMap<Char, Point> = mutableMapOf()
    var start: Point? = null
    val map = mutableListOf<MutableList<Char>>()
    var y = 0
    val data = readInput("src/day18/test.data").forEach { l ->
        val row = mutableListOf<Char>()
        map.add(row)
        var x = 0
        l.toCharArray().forEach { v ->
            row.add(v)
            when {
                v.isLowerCase() -> keysMap[v] = Point(x,y)
                v.isUpperCase() -> doorsMap[v] = Point(x,y)
                v == '@' -> start = Point(x,y)
            }
            x++
        }
        y++
    }
    map.print()
    println(keysMap.toString())
    println(doorsMap.toString())
    println(start)
}

fun List<List<Char>>.findKeys(position: Point, foundKeys: MutableList<Char>) {
    val up = getValue(position.up())
}

fun MutableList<MutableList<Char>>.findKey(position: Point, path: MutableList<Directions>, keys: MutableList<Char>) {
    val up = getValue(position.up())
    var moveUp = true
    when {
        up.isLowerCase() -> keys.add(up)
        up.isUpperCase() -> moveUp = keys.contains(up.toLowerCase())
        up == '#' -> moveUp = false
    }
    if (moveUp) {
        path.add(Directions.U)
        findKey(position.up(), path, keys)
    }

    if (path.isEmpty()) {
        throw IllegalStateException("No move left")
    }
    val last = path.removeAt(path.size - 1)
}

fun List<List<Char>>.getValue(point: Point): Char {
    val x = point.x
    val y = point.y
    if (x < 0 || y < 0 || y >= size || x >= this[0].size) {
        return '#'
    }
    return this[y][x]
}

fun List<List<Char>>.print() {
    forEach { row ->
        row.forEach { print(it.toString()) }
        println()
    }
}
