package search

import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.random.nextUInt

fun biasedSearch(range :UIntRange, goal :UInt, bias :Double =0.5) :UInt {
    assert(goal in range)
    var min = range.first;  var max = range.last
    var numGuesses = 0u
    var numNos = 0u
    while (min<max) {
        val mid = (bias*min.toInt()+(1-bias)*max.toInt()).toUInt()
        ++numGuesses
        if (mid < goal) {
            min = mid + 1u
        }
        else if (goal < mid) {
            max = mid
            ++numNos
        } else {
            min = mid
            --max
        }
    }
    return numNos
}

fun main() {
    val random = Random(System.currentTimeMillis())
    val range = 0u..< 1024u
    val numRounds = 1000000
    val error = 5/sqrt(numRounds.toDouble())
    for (bias in listOf(0.1, 0.3, 0.5, 0.7, 0.9, 0.95, 0.975, 0.9875, 0.995, 0.9995)) {
        val numNos = (1..numRounds).sumOf { biasedSearch(range, random.nextUInt(range), bias) }
        println("%.4f: %.3f±%.3f".format(bias, numNos.toDouble() / numRounds, error))
    }
}
