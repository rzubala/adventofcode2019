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
            addPair(pairs, num)
        }
        last = num
    }
    if (singlePair) {
        return pairs.hasSinglePair()
    }
    return pairs.isNotEmpty()
}

fun addPair(pairs: MutableMap<Int, Int>, num: Int) {
    val cnt = pairs[num]
    pairs[num] = cnt?.plus(1) ?: 2
}

fun MutableMap<Int, Int>.hasSinglePair(): Boolean {
    this.values.forEach {
        if (it == 2) {
            return true
        }
    }
    return false
}

