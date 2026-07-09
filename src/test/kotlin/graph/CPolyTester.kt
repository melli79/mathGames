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
class CPolyTester {

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
    
    @Test fun colorizeGermany() {
        val colors = 0..<4
        val cMap =mutableMapOf<Int, Int>()
        cMap[Germany.BY.ordinal] = 0
        cMap[Germany.BW.ordinal] = 1
        cMap[Germany.HE.ordinal] = 2
        cMap[Germany.TH.ordinal] = 1
        cMap[Germany.RP.ordinal] = 0
        cMap[Germany.SL.ordinal] = 1
        cMap[Germany.NW.ordinal] = 1
        cMap[Germany.NI.ordinal] = 0
        cMap[Germany.HB.ordinal] = 1
        cMap[Germany.ST.ordinal] = 2
        cMap[Germany.SN.ordinal] = 3
        cMap[Germany.BB.ordinal] = 1
        cMap[Germany.BE.ordinal] = 0
        cMap[Germany.MV.ordinal] = 2
        cMap[Germany.SH.ordinal] = 1
        cMap[Germany.HH.ordinal] = 0
        println(cMap.entries.joinToString { (k :Int, v :Int) -> "${Germany.entries[k]} -> $v" })
        germany.getEdges().forEach { e :Graph.Edge ->
            assertNotEquals(cMap[e.v0], cMap[e.v1])
        }
        assertEquals(colors.toSet(), cMap.values.toSet())
    }

    @Tag("slow")
    @Test fun xxCpolyGermany() {
        val size0 = cpoly.cache.size
        val chi :ZPoly = cpoly(germany)
        println(chi)
        println("3 colors: ${chi(3.0)};  4 colors: ${chi(4.0)}")
        println("memory size: before= ${size0} entries,  after= ${cpoly.cache.size} entries")
    }

    @Tag("slow")
    @Test fun xxCpolyChina() {
        val size0 = cpoly.cache.size
        val poly :ZPoly = cpoly(china)
        println(poly)
        println("3 colors: ${poly(3.0)};  4 colors: ${poly(4.0)};  5 colors: ${poly(5.0)}")
        println("memory size: before= ${size0} entries,  after= ${cpoly.cache.size} entries")
    }
}

fun Double.roundTo(digits :Int) :Double = round(this*10.0.pow(digits))*10.0.pow(-digits)
