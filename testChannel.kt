import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
    var x = start
    while (true) { 
        println("Generating ${x}")
        send(x++) // infinite stream of integers from start
    }
}

fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce<Int> {
    for (x in numbers) {
        println("Filtering ${x} with ${prime}")
        if (x % prime != 0) send(x)
    }
}

fun CoroutineScope.primeNumbers(count: Int) = produce<Int> {
    var cur = numbersFrom(2)
    repeat(count) {
        val prime = cur.receive()
        println("Received ${prime}")
        send(prime)
        cur = filter(cur, prime)
    }
    close()
}

fun main() = runBlocking {
	primeNumbers(30).consumeEach { println(it) }
    coroutineContext.cancelChildren()
}
