package fuzzylogic

import kotlin.test.*

class PentaryLogicTester {
    val domain = mapOf(Pair("F", 0.0), Pair("u", 0.25), Pair("M", 0.5), Pair("l", 0.75), Pair("T", 1.0))

    @Test fun tables() {
        for ((op, name) in listOf(Pair(this::and, "and"), Pair(this::or, "or "), Pair(this::transp, "tra"),
                Pair(this::eq, "eq "), Pair(this::diam, "dia"))) {
            println(" x ${name}:y " + domain.keys.joinToString("  "))
            for (x in domain.keys) {
                print(" $x     ")
                for (y in domain.keys) {
                    print("  ${op(x, y)}")
                }
                println()
            }
            println("\n")
        }
    }


    /* an xor should be a binary relation,
     * * symmetric: y*x = x*y,
     * * associative: x*(y*z) = (x*y)*z,
     * * its adjoints involutions: x*y*x = y for all x, y
     **/
    @Test fun findXor() {
        assertTrue(false, "There is no such xor, because 5 is not a power of 2.")
    }

    val UNDEF = 0.toByte()

    fun canAssociative(xor :Array<ByteArray>) :Boolean {
        for (x in arrayOf(1,2,3,4,5)) {
            for (y in arrayOf(1,2,3,4,5)) {
                for (z in arrayOf(1,2,3,4,5)) {
                    val yz = xor[y-1][z-1];  val xy = xor[x-1][y-1]
                    if (yz!=UNDEF && xy!=UNDEF && xor[x-1][yz-1]!=UNDEF && xor[xy-1][z-1]!=UNDEF &&
                            xor[x-1][yz-1]!=xor[x-1][yz-1])
                        return false
                }
            }
        }
        return true
    }


    fun and(x :String, y :String) = approx(fuzzyAnd(domain[x]!!, domain[y]!!))

    fun or(x :String, y :String) = approx(fuzzyOr(domain[x]!!, domain[y]!!))

    fun transp(x :String, y :String) = approx(fuzzyTranspose(domain[x]!!, domain[y]!!))

    fun eq(x :String, y :String) = approx(fuzzyEq(domain[x]!!, domain[y]!!))

    fun diam(x :String, y :String) = approx(fuzzyDiamond(domain[x]!!, domain[y]!!))

    fun approx(x :Double) = when {
        x < 0.125 -> "F"
        0.825 < x -> "T"
        x < 0.375 -> "u"
        0.625 < x -> "l"
        else -> "M"
    }
}
