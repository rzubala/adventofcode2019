package test

fun main() {
    val list = mutableListOf<String>("one", "two", "three", "four", "five", "six", "seven", "eight")

    val toTest = mutableListOf<MutableList<String>>()
    for (i in 0 until list.size - 1) {
        println("take ${list[i]}")
        for (j in (i+1 until list.size)) {
            println("take ${list[j]}")
        }
        println("DROP")
    }
}


