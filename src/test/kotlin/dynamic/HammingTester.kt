package dynamic

import kotlin.test.*

class HammingTester {
    @Test fun trivial() {
        assertEquals(0u, computeHammingDistance("", ""))
    }

    @Test fun delete1() {
        assertEquals("ac", patch("abc", listOf(Edit.Delete(1u))))
    }

    @Test fun add1() {
        assertEquals("aab", patch("ab", listOf(Edit.Insert(1u, 'a'))))
    }

    @Test fun replace1() {
        assertEquals("adc", patch("abc", listOf(Edit.Replace(1u, 'd'))))
    }

    @Test fun find1toAdd() {
        assertEquals(listOf(Edit.Insert(0u, 'a')), computeEditSteps("", "a"))
        assertEquals(1u, computeHammingDistance("", "a"))
    }

    @Test fun find1toDelete() {
        assertEquals(listOf(Edit.Delete(0u)), computeEditSteps("a", ""))
        assertEquals(1u, computeHammingDistance("a", ""))
    }

    @Test fun find1toReplace() {
        assertEquals(listOf(Edit.Replace(0u, 'b')), computeEditSteps("a", "b"))
        assertEquals(1u, computeHammingDistance("a", "b"))
    }

    @Test fun hallo2hell() {
        val result = computeEditSteps("Hallo", "Hell")
        println("'Hallo': ${result} -> 'Hell'")
        assertEquals("Hell", patch("Hallo", result))
        assertEquals(2u, computeHammingDistance("Hallo", "Hell"))
    }
}
