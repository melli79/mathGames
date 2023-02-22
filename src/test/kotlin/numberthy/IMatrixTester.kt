package numberthy

import org.junit.jupiter.api.assertThrows
import kotlin.test.*

class IMatrixTester {
    @Test fun addition() {
        val m = IMatrix(longArrayOf(0, 1, 2, 3), 2u)
        val m1 = IMatrix(longArrayOf(1, 2, 0, 4), 2u)
        val result = m+m1
        println("M + M_1 = $result")
        assertEquals(IMatrix(longArrayOf(1, 3, 2, 7), 2u), result)
    }

    @Test fun multiplication() {
        val m = IMatrix(longArrayOf(0, 1, 2, 3), 2u)
        val m1 = IMatrix(longArrayOf(1, 0, 3, 2), 2u)
        val result = m*m1
        println("M * M_1 = $result")
        assertEquals(2u, result.n)
        assertEquals(longArrayOf(3,2, 11,6).toList(), result.cs.toList())
    }

    @Test fun trivialInverses() {
        assertThrows<IMatrixException> { IMatrix.zero(2u).inv() }
        val id = IMatrix.id(2u)
        val result = id.inv()
        assertEquals(id, result*id)
        val j = IMatrix(longArrayOf(0,1, 1,0), 2u)
        val result2 = j.inv()
        assertEquals(id, result2*j)
    }

    @Test fun division() {
        val m = IMatrix(longArrayOf(1,0, 1,1), 2u)
        val m1 = IMatrix(longArrayOf(0,1, 1,1), 2u)
        val result = m.ldiv(m1)
        println("m1 \\ m = $result")
        assertEquals(m, m1*result)
    }

    @Test fun irreducibles() {
        val units = setOf(IMatrix.id(2u), IMatrix(longArrayOf(0,1, 1,0), 2u))
        val primes = mutableListOf<IMatrix>()
        val counterExamples = mutableListOf<Divisor>()
        val l  = 16L
        (0L until l).forEach { a ->
            (0L until l).forEach { b ->
                (0L until l).forEach { c ->
                    for (d in 0L until l) {
                        val m = IMatrix(longArrayOf(a,b, c,d), 2u)
                        if (m.l1()>l.toULong())
                            break
                        if (m.det()!=0L && m !in units) {
                            val factor = primes.canFactor(m)
                            if (factor==Divisor.Absent) {
                                primes.add(m)
                            } else if (factor is Divisor.Pair && factor.l !in units && factor.r !in units && !m.isTrivial()) {
                                println("@Alon: $m = ${factor.l} * ${factor.r}")
                                counterExamples.add(factor)
                            }
                        }
                    }
                }
            }
        }
        pruneReducibles(primes)
        println("in total ${primes.size} irreducibles and ${counterExamples.size} counter examples.")
    }

    private fun Iterable<IMatrix>.canFactor(m :IMatrix) :Divisor {
        for (m1 in this) {
            try {
                val r = m.ldiv(m1)
                if (r.isMplus()) {
                    return Divisor.Pair(m1, r)
                }
            } catch (_ :IMatrixException) {
                // ignore
            }
            try {
                val l = m.rdiv(m1)
                if (l.isMplus()) {
                    return Divisor.Pair(l, m1)
                }
            } catch (_ :IMatrixException) {
                // ignore
            }
        }
        return Divisor.Absent
    }

    private fun pruneReducibles(primes :MutableList<IMatrix>) {
        for (p in primes.toList()) {
            val irred = primes.filter { p1 -> p != p1 }.all { p1 ->
                !canLDiv(p, p1) && !canRDiv(p, p1)
            }
            if (!irred) {
                println("removing: $p")
                primes.remove(p)
            }
        }
    }

    private fun canRDiv(p :IMatrix, p1 :IMatrix) :Boolean {
        try {
            val r = p.rdiv(p1)
            return r.isMplus()
        } catch (_ :IMatrixException) {
            return false
        }
    }

    private fun canLDiv(p :IMatrix, p1 :IMatrix) :Boolean {
        try {
            val q = p.ldiv(p1)
            return q.isMplus()
        } catch (_ :IMatrixException) {
            return false
        }
    }
}

private fun IMatrix.isTrivial() :Boolean {
    if (n!=2u)  return false
    if (cs[0]>=cs[2] && cs[1]>=cs[3])  return true
    if (cs[2]>=cs[0] && cs[3]>=cs[1])  return true
    if (cs[0]>=cs[1] && cs[2]>=cs[3])  return true
    return cs[1]>=cs[0] && cs[3]>=cs[2]
}

sealed interface Divisor {
    data class Pair(val l :IMatrix, val r :IMatrix) :Divisor {}

    object Absent :Divisor
}
