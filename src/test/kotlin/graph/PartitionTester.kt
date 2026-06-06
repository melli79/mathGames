package graph

import org.junit.jupiter.api.RepeatedTest
import kotlin.test.*

class PartitionTester {
    val n = 10

    @Test fun unconstrained() {
        val result = partition(n.toUShort(), emptySet())
        assertEquals(1, result.size)
        assertEquals((0..< n).toSet(), result.first())
    }

    @Test fun singleConstraint() {
        val result = partition(10u, setOf(Graph.Edge.of(0,1)))
        assertEquals(2, result.size)
        val (s1, s2) = result.toList()
        assertEquals((0..< n).toSet(), s1+s2)
        assertTrue(0 in s1 && 1 in s2 || 0 in s2 && 1 in s1)
    }

    @Test fun connectedConstraints() {
        var m = 10
        val input = setOf(Graph.Edge.of(0, 1), Graph.Edge.of(0, 2))
        var result = partition(n.toUShort(), input)
        repeat(10) {
            val candidate = partition(n.toUShort(), input)
            if (candidate.size < m) {
                m = candidate.size
                result = candidate
            }
        }
        assertEquals(2, m)
        val (s1, s2) = result.toList()
        assertEquals((0..< n).toSet(), s1+s2)
        assertTrue(0 in s1 && 1 in s2 || 0 in s2 && 1 in s1)
        assertTrue(0 in s1 && 2 in s2 || 0 in s2 && 2 in s1)
    }

    companion object {
        val mm = mutableSetOf(1)
    }

    @RepeatedTest(1000) fun try20constraints() {
        val constraints = (1..20).map {
            random.edge(n)
        }.toMutableSet()
        while (constraints.size<20) {
            constraints.add(random.edge(n))
        }
        var m = 10
        var result = partition(n.toUShort(), constraints)
        repeat(50) {
            val candidate = partition(n.toUShort(), constraints)
            if (candidate.size < m) {
                m = candidate.size
                result = candidate
            }
        }
        if (m>mm.max()) {
            mm.add(m)
            println("20 constraints into $m")
        }
    }

    @Test fun pussi1() {
        val rounds = pussi(1u)
        assertEquals(5, rounds.size)
        rounds.forEachIndexed { r, round ->
            assertEquals(4, round.size, "round $r: expected 4 games, but found: "+round.joinToString())
            round.forEach { assertEquals(4, it.size) }
            val players = round.flatten().sorted()
            assertEquals((0..15).toList(), players, "round $r: expected 16 players, but found: $players")
        }
    }

    @Test fun pussi2() {
        pussi(2u)
    }

    fun pussi(n :UInt) :List<Set<Set<Int>>> {
        val k = 4u
        val N = k*(1u+n*(k-1u))
        println("Game setup: $N players in groups of $k are playing ${(N-1u)/(k-1u)} rounds:")
        val rounds = partition(k, n)
        println("\n"+ rounds.mapIndexed { r, round -> Pair(r, round) }.joinToString("\n") { (r, round) ->
            "round $r: "+round.joinToString() })
        return rounds
    }

    @Test fun girls1() {
        val days = girls(1u)
        assertEquals(4, days.size)
        days.take(2).forEachIndexed { d, day ->
            assertEquals(3, day.size, "day $d: expected 3 rows, but found: "+day.joinToString())
            day.forEach { assertEquals(3, it.size, "expected 3 players in row, but found: $it") }
            val players = day.flatten().toSet()
            assertEquals((0..< 9).toSet(), players, "day $d: expected 9 players, but found: $players")
        }
    }

    @Test fun girl2() {
        girls(2u)
    }

    fun girls(n :UInt) :List<Set<Set<Int>>> {
        val k = 3u
        val N = k*(1u+n*(k-1u))
        println("Game setup: $N girls in groups of $k are walking ${(N-1u)/(k-1u)} days:")
        val days = partition(k, n)
        println("\n"+ days.mapIndexed { d, day -> Pair(d, day) }.joinToString("\n") { (d, day) ->
            "day $d: "+day.joinToString() })
        return days
    }

    @Test fun pentas1() {
        pentas(1u)
    }

    fun pentas(n :UInt) {
        val k = 5u
        val N = k*(1u+n*(k-1u))
        println("Dance setup: $N dancers in groups of $k are dancing ${(N-1u)/(k-1u)} rounds:")
        val rounds = partition(k, n)
        println("\n"+ rounds.mapIndexed { r, round -> Pair(r, round) }.joinToString("\n") { (r, round) ->
            "round $r: "+round.joinToString() })
    }

    @Test fun hexa1() {
        hexas(1u)
    }

    fun Int.add6(s :Int) = 6*(this/6) +(this+s)%6

    fun hexas(n :UInt) {
        val k = 6u
        val N = k*(1u+n*(k-1u))
        println("Dance setup: $N dancers in groups of $k are dancing ${(N-1u)/(k-1u)} rounds:")
        val s01 = (0..5).toSet()
        val s10 = s01.map{it*6}.toSet()
        val rounds :List<MutableSet<Set<Int>>> = listOf(
            mutableSetOf(s01, s01.map{it+6}.toSet(), s01.map{it+12}.toSet(), s01.map{it+18}.toSet(), s01.map{it+24}.toSet(), s01.map{it+30}.toSet()), // (0,1)
            mutableSetOf(s10, s10.map{it+1}.toSet(), s10.map{it+2}.toSet(), s10.map{it+3}.toSet(), s10.map{it+4}.toSet(), s10.map{it+5}.toSet()), // (1,0)
            mutableSetOf(setOf(0,7)),
            mutableSetOf(setOf(0,8)),
            mutableSetOf(),
            mutableSetOf(),
            mutableSetOf(),
        )
        val remainings = (12..35).toMutableList()
        rounds.hexa1(remainings, 2) { rem3, r3 ->
            println("found 2nd permutation: ${rounds[2].first().joinToString()}")
            this.hexa1(rem3, r3) { rem4, r4 ->
                //      this.hexa1(rem4, r4) { rem4, r5 ->
                // val r5 = rounds[5]

                // val r6 = rounds[6]

                println("\n"+ this.mapIndexed { r, round -> Pair(r, round) }.joinToString("\n") { (r, round) ->
                    "round $r: "+round.joinToString() })
                true
                //      }
            }
        }
    }

    private fun List<MutableSet<Set<Int>>>.hexa1(remainings :MutableList<Int>, round :Int,
             descend:List<MutableSet<Set<Int>>>.(MutableList<Int>, Int)->Boolean) :Boolean {
        val r2 = this[round]
        val h2 = r2.first().toMutableSet()
        r2.remove(r2.first())
        var abort = false
        var it2 = 0
        while (!abort && it2 < remainings.size) {
            val p22 = remainings[it2]
            if (this.can(h2, p22)) {
                remainings.removeAt(it2);  h2.add(p22)
                println("p22=$p22")
                var it23 = it2
                while (!abort && it23 < remainings.size) {
                    val p23 = remainings[it23]
                    if (this.can(h2, p23)) {
                        remainings.removeAt(it23);  h2.add(p23)
                        println("p23=$p23")
                        var it24 = it23
                        while(!abort && it24 < remainings.size) {
                            val p24 = remainings[it24]
                            if (this.can(h2, p24)) {
                                remainings.removeAt(it24);  h2.add(p24)
                                println("p24=$p24")
                                var it25 = it24
                                while (!abort && it25 < remainings.size) {
                                    val p25 = remainings[it25]
                                    if (this.can(h2, p25)) {
                                        remainings.removeAt(it25);  h2.add(p25)
                                        r2.add(h2)
                                        (1..5).forEach { sh -> r2.add(h2.map{it.add6(sh)}.toSet()) }
                                        abort = descend(remainings, round+1)
                                        r2.clear()
                                        h2.remove(p25);  remainings.add(it25, p25)
                                    }
                                    it25++
                                }
                                h2.remove(p24);  remainings.add(it24, p24)
                            }
                            it24++
                        }
                        h2.remove(p23);  remainings.add(it23, p23)
                    }
                    it23++
                }
                h2.remove(p22);  remainings.add(it2, p22)
                println()
            }
            it2++
        }
        r2.add(h2)
        return abort
    }
}
