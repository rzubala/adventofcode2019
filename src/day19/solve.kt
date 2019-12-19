package day19

import day10.Point
import utils.IntCode
import utils.copy
import utils.readInput

const val LAST_INDEX = 49


fun main() {
    val code = readInput("src/day19/input.data")[0].split(",").map { it.toLong() }

    var sum = 0L
    (0..LAST_INDEX).forEach { y ->
        (0..LAST_INDEX).forEach { x->
            val position = Point(x,y)
            var isX = true
            IntCode(code.copy()).run({
                val value = if (isX) position.x.toLong() else position.y.toLong()
                isX = !isX
                value
            }) { out ->
                sum += out
            }
        }
    }
    println("sum $sum")
}