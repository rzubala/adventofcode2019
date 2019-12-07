package day07

import day02.copy
import day05.intCode
import utils.readInput

fun main() {
    val opcodes = readInput("src/day07/input.data")[0].split(",").map { it.toInt() }

    var max = Integer.MIN_VALUE
    var maxPhases = ""
    val phases = (0..4)

    for (p0 in phases) {
        for (p1 in phases) {
            if (p1 == p0) {
                continue
            }
            for (p2 in phases) {
                if (p2 == p1 || p2 == p0) {
                    continue
                }
                for (p3 in phases) {
                    if (p3 == p2 || p3 == p1 || p3 == p0) {
                        continue
                    }
                    for (p4 in phases) {
                        if (p4 == p3 || p4 == p2 || p4 == p1 || p4 == p0) {
                            continue
                        }
                        var output = 0
                        output = intCode(opcodes.copy(), listOf(p0, output))
                        output = intCode(opcodes.copy(), listOf(p1, output))
                        output = intCode(opcodes.copy(), listOf(p2, output))
                        output = intCode(opcodes.copy(), listOf(p3, output))
                        output = intCode(opcodes.copy(), listOf(p4, output))
                        if (output > max) {
                            max = output
                            maxPhases = "$p0$p1$p2$p3$p4"
                        }
                    }
                }
            }
        }
    }

    println("max: $max $maxPhases")
}