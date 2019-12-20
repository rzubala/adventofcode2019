package day18

import day10.Point
import day17.*
import utils.copy
import utils.readInput

typealias MatrixChar = MutableList<MutableList<Char>>
fun main() {
    var starts = mutableListOf<Point>()
    val map = mutableListOf<MutableList<Char>>()
    val data = readInput("src/day18/input.data").toMutableList()
    data.forEach { l ->
        val row = mutableListOf<Char>()
        map.add(row)
        l.toCharArray().forEach { v ->
            row.add(v)
        }
        row.mapIndexed { i: Int, c: Char ->  if (c == '@') i else null}.filterNotNull().toList().forEach { x ->
            starts.add(Point(x, map.size - 1))
        }
    }
    println("Part1: ${calculate(map, starts, mutableListOf())}")

    val ref = Point(starts[0].x, starts[0].y)
    starts.clear()
    starts.apply {
        add(Point(ref.x-1, ref.y-1))
        add(Point(ref.x+1, ref.y-1))
        add(Point(ref.x-1, ref.y+1))
        add(Point(ref.x+1, ref.y+1))
    }
    map[ref.x][ref.y] = '#'
    map[ref.x-1][ref.y] = '#'
    map[ref.x+1][ref.y] = '#'
    map[ref.x][ref.y-1] = '#'
    map[ref.x][ref.y+1] = '#'
    starts.forEach { map[it.x][it.y] = '@' }
    cache.clear()
    println("Part2: ${calculate(map, starts, mutableListOf())}")
}

typealias PointsWithKeys = Pair<List<Point>, String>
val cache = mutableMapOf<PointsWithKeys, Int>()

fun calculate(map: MatrixChar, points: List<Point>, myKeys: MutableList<Char>): Int {
    cache[PointsWithKeys(points, myKeys.toSortedString())]?.let {
        return it
    }
    var result = 0
    val keys = collect4(map, points, myKeys)
    if (keys.isNotEmpty()) {
        val positions = mutableListOf<Int>()
        for (keyItem in keys.entries) {
            val key = keyItem.key
            val point = keyItem.value.first
            val distance = keyItem.value.second
            val robot = keyItem.value.third
            val npoints = mutableListOf<Point>()
            points.forEachIndexed { i, p ->
                if (i == robot) {
                    npoints.add(point)
                } else {
                    npoints.add(p)
                }
            }
            positions.add(distance + calculate(map, npoints, myKeys.copy().apply { add(key) }))
        }
        result = positions.min()!!
    }
    cache[PointsWithKeys(points, myKeys.toSortedString())] = result
    return result
}

fun List<Char>.toSortedString() = sorted().joinToString("") { it.toString() }

typealias PointWithDistanceWithRobot = Triple<Point, Int, Int>
fun collect4(map: MatrixChar, points: List<Point>, myKeys: MutableList<Char>): Map<Char, PointWithDistanceWithRobot> {
    val resultKeyData = mutableMapOf<Char, PointWithDistanceWithRobot>()
    points.forEachIndexed { i, point ->
        collectKeys(map, point, myKeys).forEach { data ->
            resultKeyData[data.key] = PointWithDistanceWithRobot(data.value.first, data.value.second, i)
        }
    }
    return resultKeyData
}

typealias PointWithDistance = Pair<Point, Int>
fun collectKeys(map: MatrixChar, point: Point, myKeys: MutableList<Char>): Map<Char, PointWithDistance> {
    val resultKeyData = mutableMapOf<Char, PointWithDistance>()
    val deque = mutableListOf<Point>()
    deque.add(point)
    val distances = mutableMapOf<Point, Int>()
    distances[point] = 0
    while (deque.isNotEmpty()) {
        val current = deque.removeAt(0)
        for (n in current.neighbors()) {
            run neighbors@{
                val value = map.getValue(n)
                if (value.isWall()) {
                    return@neighbors
                }
                distances[n]?.let {
                    return@neighbors
                }
                distances[n] = distances[current]!!.plus(1)
                if (value.isUpperCase() && !myKeys.contains(value.toLowerCase())) {
                    return@neighbors
                }
                if (value.isLowerCase() && !myKeys.contains(value)) {
                    resultKeyData[value] = PointWithDistance(n, distances[n]!!)
                } else {
                    deque.add(n)
                }
            }
        }
    }
    return resultKeyData
}

fun Char.isWall() = this == '#'

fun Point.neighbors(): List<Point> {
    val point = this
    return mutableListOf<Point>().apply {
        add(point.up())
        add(point.left())
        add(point.down())
        add(point.right())
    }
}

fun List<List<Char>>.getValue(point: Point): Char {
    val x = point.x
    val y = point.y
    if (x < 0 || y < 0 || y >= size || x >= this[y].size) {
        return '#'
    }
    return this[y][x]
}
