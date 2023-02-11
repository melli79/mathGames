package reedsolomon

import kotlin.test.*

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalStdlibApi::class)
class FpTester {
    val p = 5uL
    val ZERO = Fp.zero(p)
    val ONE = Fp.one(p)
    val TWO = Fp(p, 2uL)
    val THREE = Fp(p, 3uL)
    val FOUR = Fp(p, 4uL)

    val zero = FpPolynomial.zero(p)
    val one = FpPolynomial.one(p)
    val x = FpPolynomial.x(p)

    @Test fun multiplication() {
        println(ONE.rangeMult().toList().joinToString("\n") { x ->
            ONE.range().toList().joinToString { y ->
                (x * y).a.toString()
            }
        })
    }

    @Test fun addition() {
        println(ZERO.range().toList().joinToString("\n") { x ->
            ZERO.range().toList().joinToString { y ->
                (x + y).a.toString()
            }
        })
    }

    @Test fun inverses() {
        val mults = ONE.rangeMult().toList()
        println("a   : $mults")
        println("a^-1: "+ mults.map { a ->
            val inv = a.inv()
            assertEquals(ONE, a*inv)
            inv
        })
    }
    @Test fun ex() {
        println("X: $x, deg=${x.deg}")
    }

    @Test fun polyAddition() {
        println(ZERO.range().toList().joinToString { c ->
            (x + FpPolynomial.Companion.of(c)).toString()
        })
    }

    @Test fun polyMultiplication() {
        println(ZERO.range().toList().joinToString("\n"){ c ->
            ZERO.range().toList().joinToString("  \t") { c1 ->
                ((x+ FpPolynomial.of(c1))*(x+FpPolynomial.of(c))).toString()
            }
        })
    }

    @Test fun polyDivision() {
        val poly = FpPolynomial(p, ulongArrayOf(3uL, 0uL, 0uL, 1uL))
        val d = x + FpPolynomial.of(Fp(p, 2uL))
        val result = poly.div(d)
        println("($poly) / ($d) = ${result.first} rem ${result.second}")
        val expected = (x + FpPolynomial.of(THREE) )*x + FpPolynomial.of(FOUR)
        println("($d) * ($expected) = ${d*expected}")
        assertEquals(zero, result.second)
        assertEquals(ONE, result.first.lc())
        assertEquals(expected, result.first)
    }

    val lexicographicSorter = Comparator.comparing<FpPolynomial, ULong> { p -> p.p }
        .thenComparing { p -> p.deg }
        .thenComparing { p -> p.lc() }
        .thenComparing { p, q -> p.cs.zip(q.cs).reversed().foldRight(0) {
                (cp, cq), e -> if (e!=0) e  else cp.compareTo(cq)
        } }

    @Test fun irreducibles() {
        assert (zero==zero)
        val z = zero.clone()
        assert (z==zero)
        var irreducibles = genIrred(p, 1u)
        println("Irreducible polynomials mod $p, deg=1: $irreducibles")
        val tests = ZERO.range().toList().map { c -> x+ FpPolynomial.Companion.of(c) }.toMutableList()
        assertEquals(tests.toSet(), irreducibles.toSet())

        irreducibles = genIrred(p, 2u)
        print("Irreducible polynomials mod $p, deg=2: ")
        val x2 = x*x
        ZERO.range().toList().forEach { c1 ->
            ZERO.range().toList().forEach { c0 ->
                val p = x2 +x*c1 + FpPolynomial.Companion.of(c0)
                if (tests.all { f -> f.deg>p.deg/2 || p.div(f).second!=zero }) {
                    tests.add(p)
                    print("$p, ")
                }
            }
        }
        tests.sortWith(lexicographicSorter)
        assertEquals(tests, irreducibles.sortedWith(lexicographicSorter))

        irreducibles = genIrred(p, 3u)
        print("\nIrreducible polynomials mod $p, deg=3: ")
        val x3 = x2*x
        ZERO.range().toList().forEach { c2 ->
            ZERO.range().toList().forEach { c1 ->
                ZERO.range().toList().forEach { c0 ->
                    val p = x3 +x2*c2 +x*c1 + FpPolynomial.Companion.of(c0)
                    if (tests.all { f -> f.deg>p.deg/2 || p.div(f).second!=zero }) {
                        tests.add(p)
                        print("$p, ")
                    }
                }
            }
        }
        tests.sortWith(lexicographicSorter)
        assertEquals(tests, irreducibles.sortedWith(lexicographicSorter))

        irreducibles = genIrred(p, 4u)
        val x4 = x2*x2
        val irr4 = mutableListOf<FpPolynomial>()
        ZERO.range().toList().forEach { c3 ->
            ZERO.range().toList().forEach { c2 ->
                ZERO.range().toList().forEach { c1 ->
                    ZERO.range().toList().forEach { c0 ->
                        val p = x4+ x3*c3 +x2*c2 +x*c1 + FpPolynomial.Companion.of(c0)
                        if (tests.all { f -> f.deg>p.deg/2 || p.div(f).second!=zero }) {
                            irr4.add(p)
                        }
                    }
                }
            }
        }
        println("\nIrreducible polynomials mod $p, deg=4: %s, ...".format(irr4.take(4)))
        assertEquals((tests+irr4).sortedWith(lexicographicSorter), irreducibles.sortedWith(lexicographicSorter))

        irreducibles = genIrred(p, 5u)
        val x5 = x4*x
        val irr5 = mutableListOf<FpPolynomial>()
        ZERO.range().toList().forEach { c4 ->
            ZERO.range().toList().forEach { c3 ->
                ZERO.range().toList().forEach { c2 ->
                    ZERO.range().toList().forEach { c1 ->
                        ZERO.range().toList().forEach { c0 ->
                            val p = x5 +x4*c4+ x3*c3 +x2*c2 +x*c1 + FpPolynomial.Companion.of(c0)
                            if (tests.all { f -> f.deg>p.deg/2 || p.div(f).second!=zero }) {
                                irr5.add(p)
                            }
                        }
                    }
                }
            }
        }
        println("Irreducible polynomials mod $p, deg=5: %s, ...".format(irr5.take(4)))
        assertEquals((tests+irr4+irr5).sortedWith(lexicographicSorter), irreducibles.sortedWith(lexicographicSorter))
    }

    @Test fun factor() {
        val p = FpPolynomial.xPow(p, 9u) - one
        val result = factor(p)
        println("$p = $result")
        result.forEach { f -> assertTrue(isIrr(f)) }
        assertEquals(p.deg, result.sumOf { f -> f.deg })
    }

    @Test fun gcd() {
        val p1 = x-one
        val p2 = FpPolynomial.xPow(p, 3u) - one
        val result = gcd(p1, p2)
        println("gcd( $p1, $p2) = $result")
        assertEquals(p1, result)
    }

    @Test fun euclid() {
        val p1 = x-one
        val p2 = FpPolynomial.xPow(p, 3u) - one
        val (f1, f2, g) = euclid(p1, p2)
        println("gcd($p1, $p2) = $f1*$p1 +$f2*$p2 = $g")
        val expected = gcd(p1, p2)
        assertEquals(expected, g)
        assertEquals(expected, f1*p1 +f2*p2)
    }
}
