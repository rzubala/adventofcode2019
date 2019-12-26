package test

fun main() {
    val list = mutableListOf<String>("mutex", "manifold", "klein bottle", "mug", "polygon", "loom", "hypercube", "pointer")
    val dropAll = dropAll(list)

    val toTest = mutableListOf<MutableList<String>>()
    for (i in 0 until list.size) {
        println("take ${list[i]}")
        for (j in (0 until list.size)) {
            if (i == j) {
                continue
            }
            println("take ${list[j]}")
        }
        println(dropAll)
    }
    for (i in (1..127)) {
        var pos = 0
        i.toString(2).split("").forEach{ n ->
            if (n.isNotEmpty()) {
                if (n == "1") {
                    println("take ${list[i]}")
                }
                pos++
            }
        }
    }
}

fun dropAll(list: List<String>): String {
    var result = ""
    list.forEach { result += "drop $it" }
    return result
}



