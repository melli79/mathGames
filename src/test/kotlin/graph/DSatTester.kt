package graph

import kotlin.test.*

class DSatTester {
   @Test fun empty() {
     val g = graphOf(0u, emptyList(), "/O")
     val result = g.colorizeDS()
     println("Colorization of empty graph $g: $result")
     assertEquals(emptyList(), result)
   }

   @Test fun singleton() {
     val g = graphOf(1u, emptyList(), "K1")
     val result = g.colorizeDS()
     println("Colorization of $g: $result")
     assertEquals(listOf(0), result)
   }

   @Test fun twoSingletons() {
     val g = graphOf(2u, emptyList())
     val result = g.colorizeDS()
     println("Colorization of $g: $result")
     assertEquals(listOf(0,0), result)
   }

   @Test fun line() {
     val g = graphOf(2u, listOf(0 to 1), "K2")
     val result = g.colorizeDS()
     println("Colorization of $g: $result")
     assertEquals(listOf(0,1), result)
   }

   @Test fun line3() {
     val g = graphOf(3u, listOf(0 to 1, 1 to 2), "L3")
     val result = g.colorizeDS()
     println("Colorization of $g: $result")
     assertEquals(listOf(1,0,1), result)
   }

  @Test fun colorizeGermany() {
    val result = germany.colorizeDS()
    println("political map of Germany: "+ result.mapIndexed { pr, c -> Pair(Germany.entries[pr], c) }
      .groupBy { it.second }.entries.joinToString<Map.Entry<Int, List<Pair<Germany, Int>>>>("\n") { (c, prs) -> "$c: "+ prs.joinToString { it.first.name } })
    for (pr in Germany.entries)
      assertTrue(result[pr.ordinal] in 0..3, "Greedy colorization of $germany failed at $pr: ${result[pr.ordinal]}")
    germany.getEdges().forEach { e ->
        assertNotEquals(result[e.v0], result[e.v1])
    }
  }

  @Test fun colorizeUs() {
    val polit = usa.colorizeDS()
    println("political map of USA: "+ polit.mapIndexed { pr, c -> Pair(US.entries[pr], c) }
      .groupBy { it.second }.entries.joinToString<Map.Entry<Int, List<Pair<US, Int>>>>("\n") { (c, prs) -> "$c: "+ prs.joinToString { it.first.name } })
    for (pr in US.entries)
      assertTrue(polit[pr.ordinal] in 0..3, "Greedy colorization of $usa failed at $pr: ${polit[pr.ordinal]}")
    usa.getEdges().forEach { e ->
        assertNotEquals(polit[e.v0], polit[e.v1])
    }
  }

  @Test fun colorizeChina() {
    val polit = china.colorizeDS()
    println("political map of China: "+ polit.mapIndexed { pr, c -> Pair(China.entries[pr], c) }
      .groupBy { it.second }.entries.joinToString<Map.Entry<Int, List<Pair<China, Int>>>>("\n") { (c, prs) -> "$c: "+ prs.joinToString { it.first.name } })
    china.getEdges().forEach { e ->
        assertNotEquals(polit[e.v0], polit[e.v1])
    }
    for (pr in China.entries)
      if (polit[pr.ordinal] !in 0..3)
        println("Greedy colorization of $china failed at $pr: ${polit[pr.ordinal]}")
    val pol2 :List<Int> = china.colorizeDSbt()
    println("tight colorization of China with Backtracking: "+ pol2.mapIndexed { pr, c -> Pair(China.entries[pr], c) }.groupBy { it.second }.entries.joinToString("\n") { (c, prs) -> "$c: "+ prs.joinToString { it.first.name } })
    china.getEdges().forEach { e ->
        assertNotEquals(pol2[e.v0], pol2[e.v1])
    }
    for (pr in China.entries)
      assertTrue(pol2[pr.ordinal] in 0..3, "Colorization of $china with backtracking failed at $pr: ${pol2[pr.ordinal]}")
  }

  infix fun Int.to(w :Int) = Graph.Edge.of(this, w)
}
