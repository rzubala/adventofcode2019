package day10

import utils.readInput
import java.lang.Math.PI

data class Point(val x: Int, val y: Int)

fun main() {
    val map: MutableList<List<Char>> = mutableListOf()
    readInput("src/day10/input.data").forEach { r ->
        val row = mutableListOf<Char>().apply { addAll(r.toCharArray().toList() ) }
        map.add(row)
    }
    map.print()
    val data: List<Point> = map.toData()
    findAsteoroids(data)
}

fun findAsteoroids(data: List<Point>) {
    var max = Int.MIN_VALUE
    var p: Point? = null
    data.forEach { p1 ->
        val set: MutableSet<Double> = mutableSetOf()
        data.forEach { p2 ->
            if (p1 != p2) {
                set.add(p1.angle(p2))
            }
        }
        if (set.size > max) {
            max = set.size
            p = p1
        }
    }
    println("$p $max")
}

fun List<List<Char>>.toData(): List<Point> {
    val result: MutableList<Point> = mutableListOf()
    this.indices.forEach {r ->
        val row = this[r]
        row.indices.forEach { c ->
            if (row[c] == '#') {
                result.add(Point(c, r))
            }
        }
    }
    return result
}

fun List<List<Char>>.print() {
    this.forEach {row ->
        row.forEach{
            print(it)
        }
        println()
    }
}

fun Point.angle(p: Point): Double {
    return getAngle(x, y, p.x, p.y)
}

fun getAngle(x1: Int, y1: Int, x2: Int, y2: Int): Double {
    return kotlin.math.atan2((y2 - y1).toDouble(), (x2 - x1).toDouble()) * 180 / PI
}