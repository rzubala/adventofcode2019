package day08

import utils.readInput

const val width = 25
const val height = 6

fun main() {
    val data = readInput("src/day08/input.data")[0].toCharArray().map { it.toString().toInt() }
    var index = 0
    val layers: MutableList<MutableList<List<Int>>> = mutableListOf()
    val numLayers = data.size.div(width).div(height)
    (0 until numLayers).forEach {
        val layer: MutableList<List<Int>> = mutableListOf()
        (0 until height).forEach { j ->
            val row = data.subList(index, index + width)
            layer.add(row)
            index += width
        }
        layers.add(layer)
    }
    count(layers)
}

fun count(layers: MutableList<MutableList<List<Int>>>) {
    var minZero = Int.MAX_VALUE
    var result = 0
    (0 until layers.size).forEach { l ->
        val layer = layers[l]
        val zeros = getDigitCount(layer, 0)
        if (zeros < minZero) {
            minZero = zeros
            result = getDigitCount(layer, 1) * getDigitCount(layer, 2)
        }
    }
    println("part1: $result")
}

fun getDigitCount(layer: MutableList<List<Int>>, d: Int): Int {
    var cnt = 0
    (0 until height).forEach {r ->
        cnt += layer[r].count { it == d }
    }
    return cnt
}
