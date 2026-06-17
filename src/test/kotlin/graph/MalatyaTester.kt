package graph

import kotlin.test.*

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

   enum class Province {
       BB, BE, BW, BY, HB, HE, HH, MV, NI, NW, RP, SH, SL, SN, ST, TH;

       infix fun to(v1 :Province) = Graph.Edge.of(this.ordinal, v1.ordinal)
   }
    val germany = graphOf(Province.entries.size.toUInt(), listOf(
        listOf(Province.BW, Province.HE, Province.SN, Province.TH).map { Province.BY to it },
        listOf(Province.RP, Province.HE).map { Province.BW to it },
        listOf(Province.BE, Province.MV, Province.NI, Province.SN, Province.ST).map { Province.BB to it },
        listOf(Province.HB to Province.NI),
        listOf(Province.NW, Province.SH).map { Province.HH to it },
        listOf(Province.NW, Province.NI, Province.RP, Province.TH).map { Province.HE to it },
        listOf(Province.NI, Province.SH).map { Province.MV to it },
        listOf(Province.NW, Province.SH, Province.ST, Province.TH).map { Province.NI to it },
        listOf(Province.NW to Province.RP),
        listOf(Province.RP to Province.SL),
        listOf(Province.SN to Province.TH),
        listOf(Province.ST to Province.TH),
    ).flatten(), "G16")

    @Test fun colorizeGermany() {
        println(germany.describe()+"${germany.numEdges} pieces of inner border.")
        val polit = germany.colorize()
        println("Political map of Germany: "+ polit.mapIndexed{ pr, c -> Province.entries[pr].name+": $c" }.joinToString(",  "))
        germany.getEdges().forEach { e ->
            assertNotEquals(polit[e.v0], polit[e.v1])
        }
        Province.entries.forEach { pr ->
            assertTrue( polit[pr.ordinal] in 0..3 )
        }
    }

    infix fun Int.to(v1 :Int) = Graph.Edge.of(this, v1)
}
