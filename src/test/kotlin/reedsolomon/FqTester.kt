package reedsolomon

import numberthy.ipow
import kotlin.test.*

class FqTester {
    val p0 = 5uL
    val x = FpPolynomial.x(p0)
    val one = FpPolynomial.one(p0)

    val p = FpPolynomial.xPow(p0, 3u) +x +one

    @Test fun primitiveElement2() {
        val p = FpPolynomial.xPow(p0, 2u) +FpPolynomial.of(Fp(p0, 2u))
        val ONE = Fq.one(p)
        val gen = Fq.gen(p)
        var prod = ONE
        while (true) {
            prod *= gen
            print("$prod, ")
            if (prod.isInFp())
                println()
            if (prod == ONE)
                break
        }
    }

    @Test fun primitiveElement3() {
        val gen = Fq.gen(p)
        val ONE = Fq.one(p)
        var prod = ONE
        val len = (p0*(p0-1u)/2u).toUInt()
        var c = 1u
        for (i in 1u..(ipow(p0, p.deg.toUByte())-1u).toUInt()) {
            prod *= gen
            print("$prod, ")
            if (c%len==0u||prod.isInFp()) {
                println()
                c = 0u
            }
            if (prod==ONE)
                break
            c++
        }
    }

    @Test fun addition() {
        val ONE = Fq.one(p)
        val alpha = Fq.alpha(p)
        println((0u until p0.toUInt()).joinToString("\n") { c0 ->
            (0u until p0.toUInt()).joinToString("\t") { c1 ->
                (ONE* Fp(p0, c0.toULong()) +alpha* Fp(p0, c1.toULong())).toString()
            }
        })
    }

    @Test fun multiplier() {
        val ZERO = Fq.zero(p)
        val ONE = Fq.one(p)
        val alpha = Fq.alpha(p)
        for (a in listOf(ZERO, ONE, ONE+ONE, alpha, alpha + ONE, alpha*alpha)) {
            println("\nM_{$a} =\n"+ a.multiplier())
        }
    }

    @Test fun norm() {
        println(
            Fq.zero(p).range().toList().mapIndexed { i, a -> Pair(i.toUInt() + p0.toUInt() - 2u, a) }
            .groupBy { p -> p.first/(p0.toUInt()-1u) }.entries
            .joinToString("\n") { (_, row) -> row.joinToString { (_, a) -> "|$a|= ${a.norm().a}" } }
        )
    }
}
