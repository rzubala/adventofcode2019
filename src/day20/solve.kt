package day20

import day10.Point
import day18.getValue
import day18.neighbors
import utils.readInput
import java.lang.IllegalStateException

const val PATH = '.'
const val WALL = '#'

typealias MatrixChar = MutableList<MutableList<Char>>
fun main() {
    val data = readInput("src/day20/test.data")
    val gates: GatePoints = mutableMapOf()
    val map = buildMap(data, gates)
    map.print()
    gates.print()
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
    }.toString()

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