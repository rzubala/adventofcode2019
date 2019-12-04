package day04

const val from = 124075
const val to = 580769

fun main() {
    (from..to).countPasswords(false)
    (from..to).countPasswords(true)
}

fun IntRange.countPasswords(singlePair: Boolean) {
    println("sum: ${this.filter { isPassword(it, singlePair) }.count()}")
}

fun isPassword(password: Int, singlePair: Boolean): Boolean {
    val nums = password.toString().toCharArray()
    var last = nums[0].toInt()
    val pairs = mutableMapOf<Int, Int>()
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
    this[num] = this[num]?.plus(1) ?: 2
}

fun MutableMap<Int, Int>.hasSinglePair(): Boolean {
    return this.values.any { it == 2 }
}