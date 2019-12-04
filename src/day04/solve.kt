package day04

fun main() {
    count(124075, 580769, false)
    count(124075, 580769, true)
}

fun count(from: Int, to: Int, singlePair: Boolean) {
    val sum = (from..to).filter { isPassword(it, singlePair) }.count()
    println("sum: $sum")
}

fun isPassword(num: Int, singlePair: Boolean): Boolean {
    val nums = num.toString().toCharArray()
    var last = nums[0].toInt()
    var pairs = mutableMapOf<Int, Int>()
    (1 until nums.size).forEach { n ->
        val num = nums[n].toInt()
        if (num < last) {
            return false
        }
        if (num == last) {
            pairs.addPair(num)
        }
        last = num
    }
    return if (singlePair) {
        pairs.hasSinglePair()
    } else {
        pairs.isNotEmpty()
    }
}

fun MutableMap<Int, Int>.addPair(num: Int) {
    val cnt = this[num]
    this[num] = cnt?.plus(1) ?: 2
}

fun MutableMap<Int, Int>.hasSinglePair(): Boolean {
    this.values.forEach {
        if (it == 2) {
            return true
        }
    }
    return false
}

