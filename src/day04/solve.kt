package day04

fun main() {
    //count(100, 999)
    count(124075, 580769)
}

fun count(from: Int, to: Int) {
    var sum = 0
    (from..to).forEach { i ->
        val nums = i.toString().toCharArray()
        if (isPassword(nums)) {
            sum++
        }
    }
    println("sum: $sum")
}

fun isPassword(nums: CharArray): Boolean {
    var last = nums[0].toInt()
    var pair = false
    (1 until nums.size).forEach { n ->
        val num = nums[n].toInt()
        if (num < last) {
            return false
        }
        if (!pair && num == last) {
            pair = true
        }
        last = num
    }
    if (pair) {
        println("${nums.joinToString("")}")
    }
    return pair
}

