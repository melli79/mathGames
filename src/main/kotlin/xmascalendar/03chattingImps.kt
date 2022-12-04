package xmascalendar

import kotlin.math.roundToInt

fun main() {
    val expect = (1..12).sumOf { i -> 1.0/i }
    println("There will be approximately ${expect.roundToInt()} gifts ready per day.")
    val p1 = (2..12).fold(1.0) { p, i :Int -> p*(1-1.0/i) }
    println("The probability that only 1 gift will be ready is ${"%.1f".format(p1*100)}%.")
}