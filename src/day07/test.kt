package day07

import kotlin.concurrent.thread

private val lock = java.lang.Object()

fun main() {
    while (true) {
        thread(start = true) {
            consume()
        }
        thread(start = true) {
            produce()
        }
    }
}

var items = 0

var maxItems = 10

fun produce() = synchronized(lock) {
    while (items >= maxItems) {
        lock.wait()
    }
    Thread.sleep(100.toLong())
    items++
    println("Produced, count is $items: ${Thread.currentThread()}")
    lock.notifyAll()
}

fun consume() = synchronized(lock) {
    while (items <= 0) {
        lock.wait()
    }
    Thread.sleep(100.toLong())
    items--
    println("Consumed, count is $items: ${Thread.currentThread()}")
    lock.notifyAll()
}