package graph

import kotlin.test.*

class SimplicesTester {
   val g = graphOf(6u, listOf(0 to 1, 1 to 2, 2 to 0, 2 to 3, 3 to 4, 4 to 5, 5 to 3))

   @Test fun l0() {
      val l0 = computeL0(g)
      println("L0($g) = $l0")
      assertEquals((0..5).toSet(), l0)
   }

   @Test fun l1() {
      val l1 = computeL1(g)
      println("L1($g) = $l1")
      assertEquals(g.getEdges().toSet(), l1)
   }

   @Test fun l2() {
      val l2 = g.computeLk(2u)
      println("L2($g) = $l2")
      assertEquals(setOf(setOf(0,1,2), setOf(3,4,5)), l2)
   }

   infix fun Int.to(w :Int) = Graph.Edge.of(this, w)
}

