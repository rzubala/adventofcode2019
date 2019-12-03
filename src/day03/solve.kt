package day03

import day03.Point.Companion.start
import utils.readInput
import kotlin.math.abs

data class Point(val x:Int, val y:Int) {
    companion object {
        val start = Point(0, 0)
    }
}

fun Point.dist() = abs(x) + abs(y)

val moveMap: Map<Char, (Point, Int) -> List<Point>> = mapOf(
        'U' to { point: Point, offset: Int -> range(point, offset) { p: Point -> Point(p.x, p.y + 1) } },
        'D' to { point: Point, offset: Int -> range(point, offset) { p: Point -> Point(p.x, p.y - 1) } },
        'L' to { point: Point, offset: Int -> range(point, offset) { p: Point -> Point(p.x - 1, p.y) } },
        'R' to { point: Point, offset: Int -> range(point, offset) { p: Point -> Point(p.x + 1, p.y) } }
    )

fun main() {
    val data = readInput("src/day03/input.data")
    val path1 = createPath(data[0].split(","))
    val path2 = createPath(data[1].split(","))

    //part1
    val crossings: List<Point> = findMinDistance(path1, path2)

    //part2
    findMinSteps(crossings, path1, path2)
}

fun range(point: Point, offset: Int, op: (Point)->Point): List<Point> {
    val result: MutableList<Point> = mutableListOf()
    var last = point
    (1..offset).forEach { _ ->
        last = op(last)
        result.add(last)
    }
    return result
}

fun findMinSteps(crossings: List<Point>, path1: List<Point>, path2: List<Point>) {
    var min = Integer.MAX_VALUE
    crossings.forEach {
        val sum: Int = path1.indexOf(it) + 1 + path2.indexOf(it) + 1
        if (it != start && sum < min) {
            min = sum
        }
    }
    println("steps: $min")
}

fun findMinDistance(path1: List<Point>, path2: List<Point>): List<Point> {
    var min = Integer.MAX_VALUE
    val results: MutableList<Point> = mutableListOf()
    path1.forEach {
        if (it != start && path2.contains(it)) {
            results.add(it)
            val dist = it.dist()
            if (dist < min) {
                min = dist
            }
        }
    }
    println("distance: $min")
    return results
}

fun createPath(data: List<String>): List<Point> {
    var last = start.copy()
    val result: MutableList<Point> = mutableListOf()
    data.forEach {
        val offset = it.substring(1).toInt()
        val dir = it.first()
        val points: List<Point>? = moveMap[it.first()]?.invoke(last, offset) ?: throw IllegalStateException("Direction not found $dir")
        points?.apply {
            result.addAll(points)
            last = points.last()
        }
    }
    return result
}
