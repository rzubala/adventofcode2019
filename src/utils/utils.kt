package utils

import java.io.File

fun readInput(fname: String): List<String> = File(fname).bufferedReader().readLines()

fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

fun clearConsole() {
    //println("\u001Bc")
    //print("\u001b[H\u001b[2J")
}