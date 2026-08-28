package graph

import algebra.ZPoly
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestMethodOrder
import kotlin.test.*

@TestMethodOrder(MethodOrderer.MethodName::class)
class MalatyaTester {

    @Test fun split() {
        val k3 = Graph.complete(3u) as ALGraph
        val l3 = k3.split1(1)
        println(l3.describe())
        assertEquals(4u, l3.numVertices)
        assertEquals(setOf(2, 4), l3.findNeighbors(0))
        assertEquals(setOf(0, 3), l3.findNeighbors(2))
    }

    @Test fun centrality() {
        val g4 = graphOf(4u, listOf(0 to 1, 1 to 2, 1 to 3, 2 to 3))
        (0..g4.maxV()).forEach { v ->
            println("mcentrality($v) = %.3f".format(g4.mcentrality(v)))
        }
        val c = g4.maxCentrality()
        println("maximal centrality at $c.")
        assertEquals(1, c)
    }

    @Test fun colorize() {
        val k3 = Graph.complete(3u) as ALGraph
        val cs :List<Int> = k3.colorize()
        println("Colorization of $k3 gives: $cs")
        assertEquals(3, cs.size)
        assertNotEquals(cs[0], cs[1])
        assertNotEquals(cs[0], cs[2])
        assertNotEquals(cs[2], cs[1])
    }

    @Test fun colorizeGermany() {
        println(germany.describe()+"${germany.numEdges} pieces of inner border.")
        val polit = germany.colorize()
        println("Political map of Germany: "+ polit.mapIndexed{ pr, c -> Germany.entries[pr].name+": $c" }.joinToString(",  "))
        germany.getEdges().forEach { e ->
            assertNotEquals(polit[e.v0], polit[e.v1], "Edge ${Germany.entries[e.v0]}-${Germany.entries[e.v1]} has same color.")
        }
        Germany.entries.forEach { pr ->
            assertTrue( polit[pr.ordinal] in 0..3, "Greedy colorization failed at $pr: assigned color ${polit[pr.ordinal]} out of range (0..3).")
        }
    }

    @Test fun colorizeIndia() {
        println(india.describe()+"${india.numEdges} pieces of inner border.")
        val polit = india.colorize()
        println("Political map of India: "+ polit.mapIndexed{ pr, c -> Pair(India.entries[pr], c) }.groupBy { it.second }
            .entries.joinToString("\n"){ (c, prs) -> "$c: "+prs.joinToString { it.first.name} })
        india.getEdges().forEach { e ->
            assertNotEquals(polit[e.v0], polit[e.v1], "Edge ${India.entries[e.v0]}-${India.entries[e.v1]} has same color.")
        }
        India.entries.forEach { pr ->
            assertTrue( polit[pr.ordinal] in 0..3, "Greedy colorization failed at $pr: assigned color ${polit[pr.ordinal]} out of range (0..3).")
        }
    }

    @Tag("slow")
    @Test fun xxColorizeUSA() {
        val cs = usa.colorize()
        println("political map of USA: "+cs.mapIndexed { p, c -> Pair(US.entries[p].name, c) }.groupBy { it.second }
            .entries.joinToString("\n") { (c, ps) -> "$c: "+ps.joinToString { it.first } })
        assertEquals(US.entries.size, cs.size)
        for (p in US.entries)
            assertTrue(cs[p.ordinal] in 0..< 4, "Greedy colorization failed at $p: assigned color ${cs[p.ordinal]} out of range (0..3).")
        usa.getEdges().forEach { e ->
            assertNotEquals(cs[e.v0], cs[e.v1], "Edge ${US.entries[e.v0]}-${US.entries[e.v1]} has same color.")
        }
//        val p :ZPoly = cpoly(usa)
//        println("number of colorizations of USA: 2: ${p(2)},  3: ${p(3)},  4: ${p(4)},  5: ${p(5)}, ...")
    }

    @Test fun colorizeChina() {
        val polit = china.colorize()
        println("political map of China: "+ polit.mapIndexed { pr, c -> Pair(China.entries[pr], c) }
            .groupBy{ it.second }.entries.joinToString("\n") { (c, prs) -> "$c "+ prs.joinToString() { it.first.name } })
        china.getEdges().forEach { e ->
            assertNotEquals(polit[e.v0], polit[e.v1])
        }
        for (pr in China.entries)
            if (polit[pr.ordinal] !in 0..3)
                println("Greedy colorization failed at $pr: assigned color ${polit[pr.ordinal]} out of range (0..3).")
        val polit2 = china.colorizeBt()
        assertNotNull(polit2)
        println("\n\nTight colorization of China: "+ polit2.mapIndexed { pr, c -> Pair(China.entries[pr], c) }
            .groupBy{ it.second }.entries.joinToString("\n") { (c, prs) -> "$c "+ prs.joinToString() { it.first.name } })
        china.getEdges().forEach { e ->
            assertNotEquals(polit2[e.v0], polit2[e.v1], "Edge ${China.entries[e.v0]}-${China.entries[e.v1]} has same color.")
        }
        for (pr in China.entries)
            assertTrue(polit2[pr.ordinal] in 0..3,
                "Tight colorization failed at $pr: assigned color ${polit2[pr.ordinal]} out of range (0..3).")
    }

    infix fun Int.to(v1 :Int) = Graph.Edge.of(this, v1)
}
