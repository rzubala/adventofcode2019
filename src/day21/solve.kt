package day21

import utils.IntCode
import utils.copy
import utils.readInput

fun main() {
    val code = readInput("src/day21/input.data")[0].split(",").map { it.toLong() }


    val springCode: String =
            "NOT A J\n" +
            "NOT B T\n" +
            "OR T J\n" +
            "NOT C T\n" +
            "OR T J\n" +
            "AND D J\n"

    val inputCode = toIntCode(springCode + "WALK\n").iterator()

    println(IntCode(code.copy()).run({
        inputCode.next()
    }) { out ->
        print(out.toChar())
    })
}

fun toIntCode(program: String): List<Long> {
    return program.toCharArray().toList().map{ it.toInt().toLong() }
}
