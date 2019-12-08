package day08

import utils.readInput

const val width = 25
const val height = 6
const val black = 0
const val white = 1
const val transparent = 2

fun main() {
    val data = readInput("src/day08/input.data")[0].toCharArray().map { it.toString().toInt() }
    var index = 0
    val layers: MutableList<MutableList<List<Int>>> = mutableListOf()
    val numLayers = data.size.div(width).div(height)
    (0 until numLayers).forEach { _ ->
        val layer: MutableList<List<Int>> = mutableListOf()
        (0 until height).forEach { j ->
            val row = data.subList(index, index + width)
            layer.add(row)
            index += width
        }
        layers.add(layer)
    }
    count(layers)
    val image = merge(layers)
    printImage(image)
}

fun printImage(image: List<List<Int>>) {
    (0 until height).forEach { r ->
        (0 until width).forEach { c ->
            if (image[r][c] == black) {
                print(" ")
            } else {
                print(".")
            }
        }
        println()
    }
}

fun merge(layers: MutableList<MutableList<List<Int>>>): List<List<Int>> {
    val image: MutableList<MutableList<Int>> = mutableListOf()
    repeat(height) {
        val row = mutableListOf<Int>()
        repeat(width) {
            row.add(transparent)
        }
        image.add(row)
    }
    layers.indices.forEach { l->
        val layer = layers[l]
        (0 until height).forEach { r ->
            (0 until width).forEach { c ->
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
        val zeros = getDigitCount(layer, 0)
        if (zeros < minZero) {
            minZero = zeros
            result = getDigitCount(layer, 1) * getDigitCount(layer, 2)
        }
    }
    println("part1: $result")
}

fun getDigitCount(layer: List<List<Int>>, d: Int): Int {
    var cnt = 0
    (0 until height).forEach {r ->
        cnt += layer[r].count { it == d }
    }
    return cnt
}
