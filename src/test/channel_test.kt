package test

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main() = runBlocking {
    launch {
        repeat(5) { println(receive()) }
    }

    launch {
        // this might be heavy CPU-consuming computation or async logic, we'll just send five squares
        for (x in 1..5) {
            send(x*x)
        }
    }
    println("Done!")
}

val channel = Channel<Int>()

suspend fun send(value: Int) {
    channel.send(value)
}

suspend fun receive(): Int {
    return channel.receive()
}