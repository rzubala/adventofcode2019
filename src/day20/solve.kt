package day20

import day10.Point
import day18.getValue
import day18.isWall
import day18.neighbors
import utils.readInput
import java.lang.IllegalStateException
import java.util.Collections.min

const val PATH = '.'
const val MAX_PATH = 7000
const val MAX_DEPTH = 100

typealias MatrixChar = MutableList<MutableList<Char>>
fun main() {
    val data = readInput("src/day20/input.data")
    val gates: GatePoints = mutableMapOf()
    val map = buildMap(data, gates)
    //map.print()
    //gates.print()
    val start = gates["AA"]!![0]
    println("Start $start")
    println("Part1 " + findPath(map, gates, PointLevel(start, 0)) { _, _, lvl -> lvl })
    println("Part2 " + findPath(map, gates, PointLevel(start, 0)) { m, p, lvl -> getLevel(m, p, lvl) })
}

typealias PointLevel = Pair<Point, Int>

fun findPath(map: MatrixChar, gates: GatePoints, point: PointLevel, nextLevel: (map: MatrixChar, p: Point, lvl: Int) -> Int): Int {
    val deque = mutableListOf<PointLevel>()
    deque.add(PointLevel(point.first.copy(), point.second))
    val distances = mutableMapOf<PointLevel, Int>()
    distances[point] = 0
    val path = mutableListOf<Int>()
    while(deque.isNotEmpty()) {
        val current = deque.removeAt(0)
        val lvl = current.second
        for (n in current.first.neighbors()) {
            run neighbors@{
                val ch = map.getValue(n)
                if (ch.isWall() || ch.isUpperCase()) {
                    return@neighbors
                }
                if (gates.isKey(n, "AA")) {
                    return@neighbors
                }
                val currentDist = distances[current]!!
                if (gates.isKey(n, "ZZ")) {
                    if (lvl != 0) {
                        return@neighbors
                    }
                    //println("Found Z")
                    path.add(currentDist+1)
                    return@neighbors
                }
                val dist = distances[PointLevel(n, lvl)]
                dist?.let {
                    if (it < currentDist + 1) {
                        return@neighbors
                    }
                }
                distances[PointLevel(n, lvl)] = currentDist + 1

                if (distances[PointLevel(n, lvl)]!! > MAX_PATH) {
                    return@neighbors
                }
                if (lvl > MAX_DEPTH) {
                    return@neighbors
                }

                if (ch == PATH) {
                    deque.add(PointLevel(n, lvl))
                    //println("Add $n")
                    if (gates.isGate(n)) {
                        val nn = gates.next(n)
                        val nLvl = nextLevel(map, nn, lvl)
                        deque.add(PointLevel(nn, nLvl))
                        distances[PointLevel(nn, nLvl)] = distances[PointLevel(n, lvl)]!! + 1
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

fun GatePoints.print() {
    keys.forEach{
        println("$it -> ${get(it).toString()}")
    }
}

fun MatrixChar.print() {
    forEach { line ->
        println(line.joinToString (""){ it.toString() })
    }
}