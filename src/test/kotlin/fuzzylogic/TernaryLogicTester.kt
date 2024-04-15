package fuzzylogic

import kotlin.test.*

class TernaryLogicTester {
    val domain = mapOf(Pair("F", 0.0), Pair("U", 0.5), Pair("T", 1.0))

    @Test fun tables() {
        println(" x  y  and  or  tra eq diam")
        for (x in domain.keys) {
            for (y in domain.keys) {
                println(" $x  $y   ${and(x,y)}   ${or(x,y)}    ${transp(x,y)}  ${eq(x,y)}   ${diam(x, y)}")
            }
        }
    }

    fun and(x :String, y :String) = approx(fuzzyAnd(domain[x]!!, domain[y]!!))

    fun or(x :String, y :String) = approx(fuzzyOr(domain[x]!!, domain[y]!!))

    fun transp(x :String, y :String) = approx(fuzzyTranspose(domain[x]!!, domain[y]!!))

    fun eq(x :String, y :String) = approx(fuzzyEq(domain[x]!!, domain[y]!!))

    fun diam(x :String, y :String) = approx(fuzzyDiamond(domain[x]!!, domain[y]!!))

    fun approx(x :Double) = when {
        x < 0.25 -> "F"
        0.75 < x -> "T"
        else -> "U"
    }
}
