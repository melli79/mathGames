package graph

import algebra.ZPoly
import algebra.sqr
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestMethodOrder
import kotlin.math.pow
import kotlin.math.round
import kotlin.test.*

@TestMethodOrder(MethodOrderer.MethodName::class)
class ColorizationTester {

    @Test fun trivial() {
        val g = graphOf(0u)
        val result = cpoly(g)
        assertEquals(ZPoly.const(1), result)
    }

    @Test fun point() {
        val g = graphOf(1u, emptyList(), "K1")
        val result = cpoly(g)
        println("cpoly($g) = $result")
        assertEquals(ZPoly.X, result)
    }

    @Test fun twoPoints() {
        val g = graphOf(2u,emptyList(), "K2,0")
        val result = cpoly(g)
        println("cpoly($g) = $result")
        assertEquals(ZPoly.monomial(2u), result)
    }

    @Test fun line() {
        val g = graphOf(2u, listOf(Graph.Edge.of(0,1)), "K2")
        val result = cpoly(g)
        println("cpoly($g) = $result")
        println("1 color: ${result(1)};  2 colors: ${result(2)};  3 colors: ${result(3)}")
        assertEquals((ZPoly.X-1)*ZPoly.X, result)
    }

    @Test fun twoSteps() {
        val g = graphOf(3u, listOf(Graph.Edge.of(0,1), Graph.Edge.of(1,2)), "L3")
        val result = cpoly(g)
        println("cpoly($g) = $result")
        println("1 color: ${result(1)};  2 colors: ${result(2)};  3 colors: ${result(3)};  4 colors: ${result(4)}")
        assertEquals(sqr(ZPoly.X - 1)*ZPoly.X, result)
    }

    @Test fun triangle() {
        val g = graphOf(3u, listOf(Graph.Edge.of(0,1), Graph.Edge.of(1,2), Graph.Edge.of(2,0)), "K3")
        val result = cpoly(g)
        println("cpoly($g) = $result")
        println("1 color: ${result(1)};  2 colors: ${result(2)};  3 colors: ${result(3)};  4 colors: ${result(4)}")
        assertEquals(3, result.deg)
        assertEquals(0, result(0))
        assertEquals(0, result(1))
        assertEquals(0, result(2))
        assertEquals(6, result(3))
    }

    @Test fun quadrangle() {
        val g = graphOf(4u, listOf(Graph.Edge.of(0,1), Graph.Edge.of(1,2),
            Graph.Edge.of(2,3), Graph.Edge.of(3,0)), "C4")
        val result = cpoly(g)
        println("cpoly($g) = $result")
        println("1 color: ${result(1)};  2 colors: ${result(2)};  3 colors: ${result(3)};  4 colors: ${result(4)}")
        assertEquals(4, result.deg)
        assertEquals(0, result(0))
        assertEquals(0, result(1))
        assertEquals(2, result(2))
        assertEquals(3*2*(2+1), result(3))
        assertEquals(1, result.lc)
    }

    @Test fun hashing() {
        val g = graphOf(3u, listOf(Graph.Edge.of(0,1), Graph.Edge.of(1,2),))
        val h = g.hashCode()
        val g2 = graphOf(3u, listOf(Graph.Edge.of(0,1), Graph.Edge.of(1,2),))
        val h2 = g2.hashCode()
        assertEquals(h, h2)
        assertEquals(g, g2)
    }

    enum class Province {
        BY, BW, BB, BE, HB, HH, HE, MV, NI, NW, RP, SL, SN, ST, SH, TH;
    }
    fun Province.to(v1: Province) = Graph.Edge.of(ordinal, v1.ordinal)

    val germany = graphOf(Province.entries.size.toUInt(), listOf(
        listOf(Province.BW, Province.HE, Province.SN, Province.TH).map { Province.BY.to(it) },
        listOf(Province.RP, Province.HE).map { Province.BW.to(it) },
        listOf(Province.BE, Province.MV, Province.NI, Province.SN, Province.ST).map { Province.BB.to(it) },
        listOf(Province.NI).map { Province.HB.to(it) },
        listOf(Province.NW, Province.SH).map { Province.HH.to(it) },
        listOf(Province.NW, Province.NI, Province.RP, Province.TH).map { Province.HE.to(it) },
        listOf(Province.NI, Province.SH).map { Province.MV.to(it) },
        listOf(Province.NW, Province.SH, Province.ST, Province.TH).map { Province.NI.to(it) },
        listOf(Province.NW.to(Province.RP)),
        listOf(Province.RP.to(Province.SL)),
        listOf(Province.SN.to(Province.ST)),
        listOf(Province.ST.to(Province.TH)),
    ).flatten())

    @Test fun colorizeGermany() {
        val colors = 0..<4
        val cMap =mutableMapOf<Int, Int>()
        cMap[Province.BY.ordinal] = 0
        cMap[Province.BW.ordinal] = 1
        cMap[Province.HE.ordinal] = 2
        cMap[Province.TH.ordinal] = 1
        cMap[Province.RP.ordinal] = 0
        cMap[Province.SL.ordinal] = 1
        cMap[Province.NW.ordinal] = 1
        cMap[Province.NI.ordinal] = 0
        cMap[Province.HB.ordinal] = 1
        cMap[Province.ST.ordinal] = 2
        cMap[Province.SN.ordinal] = 3
        cMap[Province.BB.ordinal] = 1
        cMap[Province.BE.ordinal] = 0
        cMap[Province.MV.ordinal] = 2
        cMap[Province.SH.ordinal] = 1
        cMap[Province.HH.ordinal] = 0
        germany.getEdges().forEach { e :Graph.Edge ->
            assertNotEquals(cMap[e.v0], cMap[e.v1])
        }
        assertEquals(colors.toSet(), cMap.values.toSet())
        println(cMap.entries.joinToString { (k :Int, v :Int) -> "${Province.entries[k]} -> $v" })
    }

    @Tag("slow")
    @Test fun xxCpolyGermany() {
        val size0 = cpoly.cache.size
        val chi :ZPoly = cpoly(germany)
        println(chi)
        println("3 colors: ${chi(3.0)};  4 colors: ${chi(4.0)}")
        println("memory size: before= ${size0} entries,  after= ${cpoly.cache.size} entries")
    }
}

fun Double.roundTo(digits :Int) :Double = round(this*10.0.pow(digits))*10.0.pow(-digits)
