package day21

import utils.IntCode
import utils.copy
import utils.readInput

fun main() {
    val code = readInput("src/day21/input.data")[0].split(",").map { it.toLong() }
    part1(code)
    part2(code)
}

fun part2(code: List<Long>) {
    val springCode: String =
        "NOT A J\n" +
        "NOT B T\n" +
        "OR T J\n" +
        "NOT C T\n" +
        "AND D T\n" +
        "OR T J\n" +
        "NOT A T\n" +
        "OR T J\n" +
        "AND H J\n" +
        "OR E J\n" +
        "AND D J\n"

    val inputCode = toIntCode(springCode + "RUN\n").iterator()
    println("Part2 ${IntCode(code.copy()).run({
        inputCode.next()
    }) { out ->
        //print(out.toChar())
    }
    }")
}

fun part1(code: List<Long>) {
    val springCode: String =
                "NOT A J\n" +
                "NOT B T\n" +
                "OR T J\n" +
                "NOT C T\n" +
                "OR T J\n" +
                "AND D J\n"
    val inputCode = toIntCode(springCode + "WALK\n").iterator()
    println("Part1 ${IntCode(code.copy()).run({
        inputCode.next()
    }) { out ->
        //print(out.toChar())
    }
    }")
}

fun toIntCode(program: String): List<Long> {
    return program.toCharArray().toList().map{ it.toInt().toLong() }
}
