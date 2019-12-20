package day20

import day10.Point
import day18.getValue
import day18.isWall
import day18.neighbors
import utils.readInput
import java.lang.IllegalStateException

const val PATH = '.'
const val WALL = '#'

typealias MatrixChar = MutableList<MutableList<Char>>
fun main() {
    val data = readInput("src/day20/input.data")
    val gates: GatePoints = mutableMapOf()
    val map = buildMap(data, gates)
    map.print()
    gates.print()

    findPaths(map, gates, gates["AA"]!![0])
}

fun findPaths(map: MatrixChar, gates: GatePoints, point: Point) {
    val deque = mutableListOf<Point>()
    deque.add(point)
    val distances = mutableMapOf<Point, Int>()
    val toZ = mutableListOf<Int>()
    distances[point] = 0
    while (deque.isNotEmpty()) {
        val current = deque.removeAt(0)
        //println("Current: $current")
        for (n in current.neighbors()) {
            run neighbors@{
                val ch = map.getValue(n)
                if (ch.isWall() || ch.isUpperCase()) {
                    //println("   Wall $ch")
                    return@neighbors
                }
                val currentDist = distances[current]!!
                distances[n]?.let {
                    if (it < currentDist.plus(1)) {
                        return@neighbors
                    }
                    //println("been $n: $it")
                    //println("   Exists $ch")
                }
                distances[n] = currentDist.plus(1)
                if (gates.isEnd(n)) {
                    println("Z")
                    toZ.add(distances[n]!!)
                    return@neighbors
                }
                println("$n: ${distances[n]}")
                if (ch == PATH) {
                    if (gates.isGate(n)) {
                        val nn = gates.next(n)
                        deque.add(Point(nn.x, nn.y))
                        distances[nn] = distances[n]!!.plus(1)
                    } else {
                        //println("   Add $n to deque")
                        deque.add(Point(n.x, n.y))
                    }
                }
            }
        }
    }
    println(toZ.toString())
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

fun key(x: Char, y: Char): String =
    mutableListOf(x, y).apply{
        sort()
    }.joinToString (""){ it.toString() }

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
            println("$key")
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