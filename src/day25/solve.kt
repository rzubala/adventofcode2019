package day25

import day10.Point
import day11.Direction
import day17.down
import day17.left
import day17.right
import day17.up
import day21.toIntCode
import utils.IntCode
import utils.copy
import utils.readInput
import java.lang.IllegalStateException
import kotlin.math.pow

const val DOORS = "Doors here lead:"
const val ITEMS = "Items here:"
const val SECURITY = "== Security Checkpoint =="
const val regexStr = "by typing (\\d+) on the keypad"

fun main() {
    val code = readInput("src/day25/input.data")[0].split(",").map { it.toLong() }
    val forbidden = mutableListOf<String>("molten lava", "infinite loop", "escape pod", "photons", "giant electromagnet")
    val path = mutableListOf<Direction>()
    val seen = mutableSetOf<Point>()
    val items = mutableListOf<String>()
    val combinations = mutableListOf<String>()

    class Droid {
        var output: String = ""
        var command: Iterator<Long>? = null
        var position = Point(0, 0)
        var securityCommand = ""
        var iter = 0
        var passwordFound = false
        fun start() {
            seen.add(position)
            IntCode(code.copy()).run(
            {
                var value = -1L
                command?.let {
                    if (it.hasNext()) {
                        value = it.next()
                    }
                }
                if (value > -1L) {
                    value
                } else {
                    val data = parseDoors(output)
                    var checkpoint = false
                    var printInv = ""
                    if (output.contains(SECURITY)) {
                        if (combinations.isEmpty()) {
                            buildCombinations(combinations, items)
                        }
                        checkpoint = true
                        printInv = combinations[iter++]
                        printInv += "inv\n"
                    }
                    output = ""
                    val nextMove = if (checkpoint && securityCommand.isNotEmpty()) {
                        securityCommand
                    } else {
                        getNextMove(data)
                    }
                    if (checkpoint) {
                        securityCommand = nextMove
                    }
                    command = toIntCode("${printInv}${nextMove}").iterator()
                    command!!.next()
                }
            })
            { out ->
                output += out.toChar().toString()
                if (!passwordFound && securityCommand.isNotEmpty()) {
                    parsePassword()
                }
            }
        }
        private fun parsePassword() {
            val regex = regexStr.toRegex()
            val matchResult = regex.find(output)
            matchResult?.let {
                println("Password ${matchResult.groupValues[1].toInt()}")
                passwordFound = true
            }
        }

        private fun buildCombinations(combinations: MutableList<String>, list: MutableList<String>) {
            val drop = dropAll(list)
            for (i in (1 until (2.0.pow(items.size)-1).toInt())) {
                var pos = 0
                combinations.add(drop)
                i.toString(2).split("").forEach{ n ->
                    if (n.isNotEmpty()) {
                        if (n == "1") {
                            combinations.add("take ${list[pos]}\n")
                        }
                        pos++
                    }
                }
            }
        }

        fun dropAll(list: List<String>): String {
            var result = ""
            list.forEach { result += "drop $it\n" }
            return result
        }

        private fun getNextMove(data: Pair<MutableList<Direction>, String>): String {
            val directions = data.first
            val item = data.second
            var itemCommand = ""
            if (item.isNotEmpty() && !forbidden.contains(item)) {
                items.add(item)
                itemCommand = "take $item\n"
            }
            when {
                directions.contains(Direction.NORTH) && !seen.contains(position.up())-> {
                    position = position.up()
                    path.add(Direction.NORTH)
                       seen.add(position.copy())
                    return "${itemCommand}north\n"
                }
                directions.contains(Direction.SOUTH) && !seen.contains(position.down())-> {
                    position = position.down()
                    path.add(Direction.SOUTH)
                    seen.add(position.copy())
                    return "${itemCommand}south\n"
                }
                directions.contains(Direction.WEST) && !seen.contains(position.left())-> {
                    position = position.left()
                    path.add(Direction.WEST)
                    seen.add(position.copy())
                    return "${itemCommand}west\n"
                }
                directions.contains(Direction.EAST) && !seen.contains(position.right())-> {
                    position = position.right()
                    path.add(Direction.EAST)
                    seen.add(position.copy())
                    return "${itemCommand}east\n"
                }
            }
            if (path.isNotEmpty()) {
                return when(path.removeAt(path.size - 1)) {
                    Direction.NORTH -> {
                        position = position.down()
                        "${itemCommand}south\n"
                    }
                    Direction.SOUTH -> {
                        position = position.up()
                        "${itemCommand}north\n"
                    }
                    Direction.WEST -> {
                        position = position.right()
                        "${itemCommand}east\n"
                    }
                    Direction.EAST -> {
                        position = position.left()
                        "${itemCommand}west\n"
                    }
                }
            }
            throw IllegalStateException("Not know what to do")
        }
    }
    Droid().start()
}

typealias DoorsData = Pair<MutableList<Direction>, String>
fun parseDoors(input: String): DoorsData {
    var doors = false
    var items = false
    val result = mutableListOf<Direction>()
    var item = ""
    input.split("\n").forEach { line ->
        if (line.contains(DOORS)) {
            doors = true
        }
        if (line.contains(ITEMS)) {
            doors = false
            items = true
        }
        if (doors) {
            when {
                line.contains("- north") -> result.add(Direction.NORTH)
                line.contains("- south") -> result.add(Direction.SOUTH)
                line.contains("- west") -> result.add(Direction.WEST)
                line.contains("- east") -> result.add(Direction.EAST)
            }
        }
        if (items) {
            if (line.startsWith("- ")) {
                item = line.drop(2)
            }
        }
    }
    return Pair(result, item)
}
