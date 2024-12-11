package xmasaoc

import numberthy.ipow
import java.io.BufferedReader
import kotlin.math.log10

private fun parse(input :String) = input.split(delimiters= " \t\n".toCharArray()).map { it.toULong() }
    .groupBy { it }.map { entry -> Pair(entry.key, entry.value.size.toLong()) }.toMap()

private val thousands = 2024uL

private fun List<ULong>.mutate() = flatMap { number ->
    val numDigits = log10(number.toDouble()).toInt() +1
    when {
    number == 0uL -> listOf(1uL)
    numDigits % 2 == 0 -> {
        val millions = ipow(10uL, (numDigits/2).toUByte())
        listOf(number.div(millions), number.mod(millions))
    }
    else -> listOf(number.times(thousands))
} }

private fun mutate(seq :Map<ULong, Long>) :Map<ULong, Long> {
    val result = mutableMapOf<ULong, Long>()
    for ((value, num) in seq) {
        listOf(value).mutate().forEach { v ->
            result[v] = (result[v]?:0) + num
        }
    }
    return result
}

fun main() {
    // val input = "125 17"
    val input = getInput(null, "2411stones.txt").toReader().use(BufferedReader::readLine).let { parse(it) }
    var seq = input
    println("Input: $seq")
    repeat(7) {
        seq = mutate(seq)
        println("${seq.values.sum()} stones up to ${log10(seq.keys.max().toDouble()).toInt()+1} digits: $seq")
    }
    println("The result has ${seq.size} numbers.\n")
    seq = input
    for (n in 1..75) {
        if (n%10==0) {
            print("$n: "); System.out.flush()
        }
        seq = mutate(seq)
        println("${seq.values.sum() } stones up to ${log10(seq.keys.max().toDouble()).toInt()+1} digits.")
    }
    println("The result has ${seq.values.sum()} numbers.")
}
