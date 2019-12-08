package day08

import utils.readInput

const val width = 25
const val height = 6
const val black = 0
const val transparent = 2

fun main() {
    val data = readInput("src/day08/input.data")[0].toCharArray().map { it.toString().toInt() }
    val layers: List<List<List<Int>>> = createLayers(data)
    count(layers)
    merge(layers).print()
}

fun createLayers(data: List<Int>): List<List<List<Int>>> {
    var index = 0
    val layers: MutableList<MutableList<List<Int>>> = mutableListOf()
    val numLayers = data.size.div(width).div(height)
    (0 until numLayers).forEach { _ ->
        val layer: MutableList<List<Int>> = mutableListOf()
        (0 until height).forEach { _ ->
            val row = data.subList(index, index + width)
            layer.add(row)
            index += width
        }
        layers.add(layer)
    }
    return layers
}

fun merge(layers: List<List<List<Int>>>): List<List<Int>> {
    val image: MutableList<MutableList<Int>> = fillTransparentImage()
    layers.indices.forEach { l->
        val layer = layers[l]
        layer.indices.forEach { r ->
            layer[r].indices.forEach { c ->
                val pixel = image[r][c]
                if (pixel == transparent) {
                    image[r][c] = layer[r][c]
                }
            }
        }
    }
    return image
}

fun count(layers: List<List<List<Int>>>) {
    var minZero = Int.MAX_VALUE
    var result = 0
    layers.indices.forEach { l ->
        val layer = layers[l]
        val zeros = layer.getDigitCount(0)
        if (zeros < minZero) {
            minZero = zeros
            result = layer.getDigitCount(1) * layer.getDigitCount(2)
        }
    }
    println("part1: $result")
}

fun List<List<Int>>.print() {
    this.indices.forEach { r ->
        this[r].indices.forEach { c ->
            print(if (this[r][c] == black) {
                " "
            } else {
                "."
            })
        }
        println()
    }
}

fun List<List<Int>>.getDigitCount(d: Int): Int {
    var cnt = 0
    this.indices.forEach {r ->
        cnt += this[r].count { it == d }
    }
    return cnt
}

fun fillTransparentImage(): MutableList<MutableList<Int>> {
    val image: MutableList<MutableList<Int>> = mutableListOf()
    repeat(height) {
        val row = mutableListOf<Int>()
        repeat(width) {
            row.add(transparent)
        }
        image.add(row)
    }
    return image
}
