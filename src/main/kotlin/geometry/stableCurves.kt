package geometry

import common.Quadruple
import common.math.statistics.choose
import common.math.cb
import kotlin.math.ln
import kotlin.math.sqrt

object stableCurves {
    private val numbers = mutableListOf(1uL, 1uL)

    fun computeNd(d :UShort) :ULong {
        for (d1 in numbers.size.toUInt()..d.toUInt()) {
            var sum = 0uL
            for (a in 1u..< d1) {
                val b = d1-a
                sum += numbers[a.toInt()]* numbers[b.toInt()]*a*a*b*(
                        b*choose((3u*d1-4u).toUShort(),(3u*a-2u).toUShort())
                        -a* choose((3u*d1-4u).toUShort(), (3u*a-1u).toUShort()))
            }
            numbers.add(sum)
        }
        return numbers[d.toInt()]
    }
}

fun main() {
    val ys = mutableListOf(0.0)
    for (d in 1u..10u) {
        val n = stableCurves.computeNd(d.toUShort()) // n(10) has an integer overflow
        val l = ln(n.toDouble())
        println("$d: $n, %.3f".format(l))
        ys.add(l)
    }
    val (m, em, b, eb) = linReg(ys)
    println("ln N_d ≈ md + b,  m=%.1f±%.1f, b = %.1f±%.1f".format(m, em, b, eb))
}

fun linReg(ys :List<Double>) :Quadruple<Double, Double, Double, Double> {
    val xs = (0..<ys.size).map { it.toDouble() }
    val ws = xs
    val n = ws.sumOf { it }
    val x1 = (xs zip ws).sumOf { (x, w) -> x*w };  val x2 = xs.sumOf { cb(it) }
    val y = (ys zip ws).sumOf { (y, w) -> y*w }
    val yx = (ys zip xs).sumOf { (y, x) -> y*x*x }
    val yy = (ys zip ws).sumOf { (y, w) -> y*y*w }
    val m = (yx*n-y*x1)/(x2*n-x1*x1)
    val b = (y -m*x1) / n
    val sigma = sqrt((yy+m*m*x2+b*b -2*(m*yx+b*y+m*b*x1)) / (n-2))
    return Quadruple(m, sigma*x1/(x2*n-x1*x1), b, sigma/sqrt(n))
}
