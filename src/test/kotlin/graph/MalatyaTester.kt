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

    enum class US {
        AL, AK, AZ, AR, CA, CO, CT, DE, DC, FL, GA, HI, ID, IL, IN, IA,
        KS, KY, LA, ME, MD, MA, MI, MN, MS, MO, MT, NE, NV, NH, NJ, NM,
        NY, NC, ND, OH, OK, OR, PA, RI, SC, SD, TN, TX, UT, VT, VA, WA,
        WV, WI, WY;
        infix fun to(v1 :US) = Graph.Edge.of(ordinal, v1.ordinal)
    }
    val usa = graphOf(US.entries.size.toUInt(), listOf(
        listOf(US.OR, US.ID).map { US.WA to it },
        listOf(US.ID, US.CA).map { US.OR to it },
        listOf(US.NV).map { US.CA to it },
        listOf(US.AZ, US.UT, US.ID).map { US.NV to it },
        listOf(US.MT, US.WY, US.UT).map { US.ID to it },
        listOf(US.WY, US.CO, US.AZ).map { US.UT to it },
        listOf(US.NM).map { US.AZ to it },
        listOf(US.TX, US.OK, US.CO).map { US.NM to it },
        listOf(US.OK, US.KS, US.NE, US.WY).map { US.CO to it },
        listOf(US.NE, US.SD, US.MT).map { US.WY to it },
        listOf(US.ND, US.SD).map { US.MT to it },
        listOf(US.SD, US.MN).map { US.ND to it },
        listOf(US.MN, US.IA, US.NE).map { US.SD to it },
        listOf(US.IA, US.MO, US.KS).map { US.NE to it },
        listOf(US.MO, US.OK).map { US.KS to it },
        listOf(US.MO, US.AR, US.TX).map { US.OK to it },
        listOf(US.AR, US.LA).map { US.TX to it },
        listOf(US.MS, US.AR).map { US.LA to it },
        listOf(US.MS, US.TN, US.MO).map { US.AR to it },
        listOf(US.TN, US.KY, US.IL, US.IA).map { US.MO to it },
        listOf(US.IL, US.WI, US.MN).map { US.IA to it },
        listOf(US.WI).map { US.MN to it },
        listOf(US.MI, US.IL).map { US.WI to it },
        listOf(US.IN, US.KY).map { US.IL to it },
        listOf(US.IN, US.OH, US.WV, US.VA, US.TN).map { US.KY to it },
        listOf(US.VA, US.NC, US.GA, US.AL, US.MS).map { US.TN to it },
        listOf(US.AL).map { US.MS to it },
        listOf(US.GA, US.FL).map { US.AL to it },
        listOf(US.GA).map { US.FL to it },
        listOf(US.SC, US.NC).map { US.GA to it },
        listOf(US.NC).map { US.SC to it },
        listOf(US.VA).map { US.NC to it },
        listOf(US.WV, US.DC, US.MD).map { US.VA to it },
        listOf(US.MD, US.PA).map { US.WV to it },
        listOf(US.DC, US.DE, US.PA).map { US.MD to it },
        listOf(US.DE, US.NJ, US.NY).map { US.PA to it },
        listOf(US.NJ).map { US.DE to it },
        listOf(US.NY).map { US.NJ to it },
        listOf(US.CT, US.MA, US.VT).map { US.NY to it },
        listOf(US.RI, US.MA).map { US.CT to it },
        listOf(US.MA).map { US.RI to it },
        listOf(US.VT, US.NH).map { US.MA to it },
        listOf(US.NH).map { US.VT to it },
        listOf(US.ME).map { US.NH to it },
    ).flatten(), "USA")

    @Tag("slow")
    @Test fun xxColorizeUSA() {
        val cs = usa.colorize()
        println("political map of USA: "+cs.mapIndexed { p, c -> Pair(US.entries[p].name, c) }.groupBy { it.second }
            .entries.joinToString("\n") { (c, ps) -> "$c: "+ps.joinToString { it.first } })
        assertEquals(US.entries.size, cs.size)
        for (p in US.entries)
            assertTrue(cs[p.ordinal] in 0..< 4)
        usa.getEdges().forEach { e ->
            assertNotEquals(cs[e.v0], cs[e.v1])
        }
//        val p :ZPoly = cpoly(usa)
//        println("number of colorizations of USA: 2: ${p(2)},  3: ${p(3)},  4: ${p(4)},  5: ${p(5)}, ...")
    }

    infix fun Int.to(v1 :Int) = Graph.Edge.of(this, v1)
}
