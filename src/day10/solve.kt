package day10

import utils.readInput
import java.lang.Math.PI
import kotlin.math.abs

data class Point(val x: Int, val y: Int)

const val index = 200.minus(1)

fun main() {
    val map: MutableList<List<Char>> = mutableListOf()
    readInput("src/day10/input.data").forEach { r ->
        map.add(mutableListOf<Char>().apply { addAll(r.toCharArray().toList() ) })
    }
    val data: List<Point> = map.toData()
    val point = findAsteroid(data)
    destroyOrder(point, data)
}

fun findAsteroid(data: List<Point>): Point {
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
    return p!!
}

fun destroyOrder(point: Point, data: List<Point>) {
    val map: MutableMap<Double, MutableList<Point>> = mutableMapOf()
    data.forEach { other ->
        if (other != point) {
            val angle = point.angle(other)
            map.add(angle, point, other)
        }
    }
    val flatList = map.toFlat()
    println("200th: ${flatList[index]} ${flatList[index].x*100 + flatList[index].y}")
}

fun Map<Double, MutableList<Point>>.toFlat(): List<Point> {
    val result: MutableList<Point>  = mutableListOf()
    val keys = toSortedMap().keys
    var empty = false
    while (!empty) {
        empty = true
        keys.forEach{ k ->
            val list = this[k]
            list?.let{
                if (it.isNotEmpty()) {
                    result.add(it.removeAt(0))
                }
                if (it.isNotEmpty()) {
                    empty = false
                }
            }
        }
    }
    return result
}

fun MutableMap<Double, MutableList<Point>>.add(angle: Double, source: Point, point: Point) {
    val list: MutableList<Point>? = get(angle)
    list?.let {
        list.add(point)
        list.sortBy { source.dist(it) }
        return
    }
    this[angle] = mutableListOf(point)
}


fun Point.dist(other: Point): Int {
    return abs(other.x - x) + abs(other.y - y)
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

fun Point.angle(p: Point): Double {
    return getAngle(x, y, p.x, p.y)
}

fun getAngle(x1: Int, y1: Int, x2: Int, y2: Int): Double {
    var angle = kotlin.math.atan2((y2 - y1).toDouble(), (x2 - x1).toDouble()) * 180 / PI + 90
    if (angle < 0) {
        angle += 360
    }
    return angle
}