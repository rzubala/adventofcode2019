package day03

import utils.readInput
import kotlin.math.abs

data class Point(val x:Int, val y:Int)
val moveMap: Map<Char, (Point, Int) -> List<Point>> = mapOf(
        'U' to { point: Point, offset: Int -> range(point, offset) { p: Point -> Point(p.x, p.y + 1) } },
        'D' to { point: Point, offset: Int -> range(point, offset) { p: Point -> Point(p.x, p.y - 1) } },
        'L' to { point: Point, offset: Int -> range(point, offset) { p: Point -> Point(p.x - 1, p.y) } },
        'R' to { point: Point, offset: Int -> range(point, offset) { p: Point -> Point(p.x + 1, p.y) } }
    )

fun range(point: Point, offset: Int, op: (Point)->Point): List<Point> {
    val result: MutableList<Point> = mutableListOf()
    var last = point
    for (i in 1..offset) {
        last = op(last)
        result.add(last)
    }
    return result
}

fun main() {
    val data = readInput("src/day03/input.data")
    val path1 = createPath(data[0].split(","))
    val path2 = createPath(data[1].split(","))

    //part1
    val crossings: List<Point> = findMinDistance(path1, path2)

    //part2
    findMinSteps(crossings, path1, path2)
}

fun findMinSteps(crossings: List<Point>, path1: List<Point>, path2: List<Point>) {
    var min = Integer.MAX_VALUE
    crossings.forEach {
        val sum: Int = path1.indexOf(it) + 1 + path2.indexOf(it) + 1
        if (it != Point(0,0) && sum < min) {
            min = sum
        }
    }
    println("steps: $min")
}

fun findMinDistance(path1: List<Point>, path2: List<Point>): List<Point> {
    var min = Integer.MAX_VALUE
    val results: MutableList<Point> = mutableListOf()
    path1.forEach {
        if (it != Point(0,0) && path2.contains(it)) {
            results.add(it)
            val dist = dist(it)
            if (dist < min) {
                min = dist
            }
        }
    }
    println("distance: $min")
    return results
}

inline fun dist(point: Point): Int = abs(point.x) + abs(point.y)

fun createPath(data: List<String>): List<Point> {
    var last = Point(0, 0)
    val result: MutableList<Point> = mutableListOf()
    data.forEach {
        val offset = it.substring(1).toInt()
        val dir = it.first()
        val points: List<Point>? = moveMap[dir]?.invoke(last, offset)
        points?.let {
            result.addAll(points)
            last = points.last()
        }
    }
    return result
}
