package day20

import day10.Point
import day17.down
import day17.left
import day17.right
import day17.up
import day18.getValue
import day18.isWall
import day18.neighbors
import utils.readInput
import java.lang.IllegalStateException
import java.util.Collections.min

const val PATH = '.'
const val MAX_PATH = 7000

data class Point3(var x: Int, var y: Int, var z: Int) {
    constructor(p: Point, z: Int) : this(p.x, p.y, z)
}

typealias MatrixChar = MutableList<MutableList<Char>>
fun main() {
    val data = readInput("src/day20/input.data")
    val gates: GatePoints = mutableMapOf()
    val map = buildMap(data, gates)
    val start = gates["AA"]!![0]
    println("Start $start")
    println("Part1 " + findPath(map, gates, Point3(start, 0)) { _, _, lvl -> lvl })
    println("Part2 " + findPath(map, gates, Point3(start, 0)) { m, p, lvl -> getLevel(m, p, lvl) })
}

fun findPath(map: MatrixChar, gates: GatePoints, point: Point3, nextLevel: (map: MatrixChar, p: Point, lvl: Int) -> Int): Int {
    val deque = mutableListOf<Point3>()
    deque.add(point.copy())
    val distances = mutableMapOf<Point3, Int>()
    distances[point] = 0
    val path = mutableListOf<Int>()
    while(deque.isNotEmpty()) {
        val current = deque.removeAt(0)
        for (n in current.neighbors()) {
            run neighbors@{
                val ch = map.getValue(n.twoD())
                if (ch.isWall() || ch.isUpperCase()) {
                    return@neighbors
                }
                if (gates.isKey(n.twoD(), "AA")) {
                    return@neighbors
                }
                val currentDist = distances[current]!!
                if (gates.isKey(n.twoD(), "ZZ")) {
                    if (n.z > 0) {
                        return@neighbors
                    }
                    path.add(currentDist+1)
                    return@neighbors
                }
                val dist = distances[n]
                dist?.let {
                    if (it <= currentDist ) {
                        return@neighbors
                    }
                }
                if (currentDist > MAX_PATH) {
                    return@neighbors
                }
                distances[n] = currentDist + 1
                if (ch == PATH) {
                    deque.add(n.copy())
                    if (gates.isGate(n.twoD())) {
                        val nn = gates.next(n.twoD())
                        val nLvl = nextLevel(map, n.twoD(), n.z)
                        if (nLvl < 0) {
                            return@neighbors
                        }
                        deque.add(Point3(nn, nLvl))
                        distances[Point3(nn, nLvl)] = distances[n]!! + 1
                    }
                }
            }
        }
    }
    return min(path)
}


fun getLevel(map: MutableList<MutableList<Char>>, nn: Point, lvl: Int): Int {
    val line = map[nn.y]
    val size = line.size
    if (nn.x == 2 || nn.x == size - 3 || nn.y == 2 || nn.y == map.size - 3) {
        return lvl - 1
    }
    return lvl + 1
}

typealias GatePoints = MutableMap<String, MutableList<Point>>
fun buildMap(data: List<String>, gates: GatePoints): MatrixChar {
    val map = mutableListOf<MutableList<Char>>()
    data.forEach { line ->
        map.add(line.toCharArray().toMutableList())
    }

    var y = 0
    map.forEach { line ->
        line.mapIndexed{i,c -> if (c.isUpperCase()) i to c else null}.filterNotNull().forEach { iter ->
            val x = iter.first
            val c = iter.second
            val p = Point(x,y)
            for (n in p.neighbors()) {
                if (n.x < p.x || n.y < p.y) {
                    continue
                }
                val h = map.getValue(n)
                if (h.isUpperCase()) {
                    if (p.x == n.x) {
                        val minY = kotlin.math.min(p.y, n.y)
                        val maxY = kotlin.math.max(p.y, n.y)
                        when (PATH) {
                            map.getValue(Point(p.x, minY - 1)) -> {
                                gates.add(c, h, Point(p.x, minY - 1))
                            }
                            map.getValue(Point(p.x, maxY + 1)) -> {
                                gates.add(c, h,  Point(p.x, maxY + 1))
                            }
                            else -> {
                                throw IllegalStateException("Can no find gate $c")
                            }
                        }
                    } else {
                        val minX = kotlin.math.min(p.x, n.x)
                        val maxX = kotlin.math.max(p.x, n.x)
                        when (PATH) {
                            map.getValue(Point(minX - 1, p.y)) -> {
                                gates.add(c, h, Point(minX - 1, p.y))
                            }
                            map.getValue(Point(maxX + 1, p.y)) -> {
                                gates.add(c, h,  Point(maxX + 1, p.y))
                            }
                            else -> {
                                throw IllegalStateException("Can no find gate $c")
                            }
                        }
                    }
                    break
                }
            }
        }
        y++
    }
    return map
}

fun key(x: Char, y: Char): String = "$x$y"

fun GatePoints.add(c: Char, h: Char, p: Point) {
    val key = key(c, h)
    get(key)?.let{
        if (it.contains(p)) {
            return
        }
        it.add(p)
        return
    }
    put(key, mutableListOf(p))
}

fun GatePoints.isGate(point: Point): Boolean {
    for (key in keys) {
        if (get(key)?.contains(point)!!) {
            return true
        }
    }
    return false
}

fun GatePoints.isKey(point: Point, k: String): Boolean {
    for (key in keys) {
        if (get(key)?.contains(point)!!) {
            return k == key
        }
    }
    return false
}

fun GatePoints.next(point: Point): Point {
    for (key in keys) {
        val list = get(key)
        if (list?.contains(point)!!) {
            var index = list.indexOf(point)
            index = (index + 1)%2
            return list[index]
        }
    }
    throw IllegalStateException("Empty gate $point")
}

fun Point3.neighbors(): List<Point3> {
    val point = this
    val z = point.z
    return mutableListOf<Point3>().apply {
        add(Point3(point.twoD().up(), z))
        add(Point3(point.twoD().left(), z))
        add(Point3(point.twoD().down(), z))
        add(Point3(point.twoD().right(), z))
    }
}

fun Point3.twoD(): Point = Point(x, y)