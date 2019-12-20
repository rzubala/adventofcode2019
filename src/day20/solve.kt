package day20

import day10.Point
import day18.getValue
import day18.isWall
import day18.neighbors
import utils.readInput
import java.lang.IllegalStateException
import java.util.Collections.min

const val PATH = '.'

typealias MatrixChar = MutableList<MutableList<Char>>
fun main() {
    val data = readInput("src/day20/input.data")
    val gates: GatePoints = mutableMapOf()
    val map = buildMap(data, gates)
    //map.print()
    //gates.print()
    val start = gates["AA"]!![0]
    println("Start $start")
    //println("Part1 ${go1(map, gates, start, mutableSetOf<Point>().apply{add(start.copy())}, 0)}")
    println("Part2 ${go2(map, gates, PointLevel(start, 0), mutableSetOf<PointLevel>().apply{add(PointLevel(start.copy(), 0))}, 0)}")
}

typealias PointLevel = Pair<Point, Int>
fun go2(map: MatrixChar, gates: GatePoints, point: PointLevel, seen: Set<PointLevel>, dist: Int): Int {
    val list = mutableListOf<Int>()
    val pLvl = point.second
    for (n in point.first.neighbors()) {
        val ch = map.getValue(n)
        if (ch.isWall() || ch.isUpperCase()) {
            continue
        }
        if (seen.contains(PointLevel(n, pLvl))) {
            continue
        }
        if (gates.isEnd(n)) {
            println("End at $n: ${dist+1} $pLvl")
            if (pLvl != 0) {
                return -1
            }
            return dist + 1
        }
        var res = -1
        if (ch == PATH) {
            res = if (gates.isGate(n)) {
                val nn = gates.next(n)
                val nLvl = getLevel(map, nn, pLvl)
                if (nLvl < 0 || nLvl > 10) {
                    return res
                }
                go2(map, gates, PointLevel(nn, nLvl), mutableSetOf<PointLevel>().apply{
                    addAll(seen);
                    add(PointLevel(n.copy(), pLvl));
                    add(PointLevel(nn.copy(), nLvl))
                }, dist + 2)
            } else {
                go2(map, gates, PointLevel(n, pLvl), mutableSetOf<PointLevel>().apply{
                    addAll(seen);
                    add(PointLevel(n.copy(), pLvl))
                }, dist + 1)
            }
        }
        if (res > 0) {
            list.add(res)
        }
    }
    if (list.isEmpty()) {
        return -1
    }
    return min(list)
}

fun getLevel(map: MutableList<MutableList<Char>>, nn: Point, lvl: Int): Int {
    val line = map[nn.y]
    val size = line.size
    if (nn.x == 2 || nn.x == size - 3 || nn.y == 2 || nn.y == map.size - 3) {
        return lvl - 1
    }
    return lvl + 1
}


fun go1(map: MatrixChar, gates: GatePoints, point: Point, seen: Set<Point>, dist: Int): Int {
    val list = mutableListOf<Int>()
    for (n in point.neighbors()) {
        val ch = map.getValue(n)
        if (ch.isWall() || ch.isUpperCase()) {
            continue
        }
        if (seen.contains(n)) {
            continue
        }
        if (gates.isEnd(n)) {
            println("End at $n: ${dist+1}")
            return dist + 1
        }
        var res = -1
        if (ch == PATH) {
            res = if (gates.isGate(n)) {
                val nn = gates.next(n)
                go1(map, gates, nn, mutableSetOf<Point>().apply{addAll(seen);add(n.copy());add(nn.copy())}, dist + 2)
            } else {
                go1(map, gates, n, mutableSetOf<Point>().apply{addAll(seen);add(n.copy())}, dist + 1)
            }
        }
        if (res > 0) {
            list.add(res)
        }
    }
    if (list.isEmpty()) {
        return -1
    }
    return min(list)
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

fun GatePoints.isEnd(point: Point): Boolean {
    for (key in keys) {
        if (get(key)?.contains(point)!!) {
            return "ZZ" == key
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