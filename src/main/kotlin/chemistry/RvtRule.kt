package chemistry

import common.math.linReg
import java.util.Random
import kotlin.math.ln
import kotlin.math.sqrt

val random = Random(System.currentTimeMillis())

/*
 * given a normal distribution, what is the dependency of the TOP10 on the size of population?
 *
 * Claim[Arrhenius, RVT rule] rising the population 2folds the power of the TOP10
 */

fun estimateTop10(n :UInt) :Pair<Double, Double> {
    val top10s = mutableListOf<Double>()
    repeat(100+10_000/n.toInt()) {
        val samples = mutableListOf<Double>()
        repeat (n.toInt()) {
            val p = random.nextGaussian()
            samples.insertSorted(p)
            while (samples.size>10)
                samples.removeFirst()
        }
        top10s.add(samples.sum())
    }
    return Pair(top10s.average(), top10s.stddevAM())
}

private fun List<Double>.stddevAM(): Double {
    val n = size
    if (n<2) return 0.0
    val x = sum()/n
    val x2 = sumOf { it*it }/n
    return sqrt((x2-x*x)/(n-1))
}

private fun <E :Comparable<E>> MutableList<E>.insertSorted(x: E) {
    if (isEmpty()) add(x)
    var mi = 0;  var ma = size
    while (mi<ma) {
        val i = (mi+ma)/2
        val cmp = get(i).compareTo(x)
        when {
            cmp < 0 -> mi = i+1
            cmp >0 -> ma = i
            else -> { mi = i;  ma = i }
        }
    }
    add(mi, x)
}

fun List<Double>.average() = sum()/size

fun main() {
    val xys = mutableListOf<Pair<Double, Double>>()
    var n = 10
    while (n<500_000) {
        val (p, dp) = estimateTop10(n.toUInt())
        println("$n: %.1f±%.1f".format(p, dp))
        xys.add(Pair(ln(n.toDouble()), p))
        n *= 2
    }
    val (kappa, b, dk, db) = linReg(xys) { i -> xys[i].first/3 }
    println("p = \\kappa\\ln N +b,  \\kappa = %.1f±%.1f, b = %.1f±%.1f".format(kappa, dk, b, db))
}
