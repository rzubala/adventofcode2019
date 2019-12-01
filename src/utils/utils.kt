package utils

import java.io.File

fun readInput(fname: String): List<String> = File(fname).bufferedReader().readLines()