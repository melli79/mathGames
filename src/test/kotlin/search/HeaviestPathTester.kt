package search

import kotlin.test.*

@OptIn(ExperimentalUnsignedTypes::class)
class HeaviestPathTester {

    val findHeaviestPath = ::findHeaviestPathReversed

    @Test fun emptyGrid() {
        val result = findHeaviestPath(emptyList())
        assertEquals(0u, result.first)
        val paths = result.second
        assertEquals(1, paths.size)
        assertEquals(listOf(), paths.first().toList())
    }

    @Test fun uniqueStep() {
        val result = findHeaviestPath(listOf(uintArrayOf(1u)))
        println("${result.first}: "+ result.second.joinToString { it.toList().toString() })
        assertEquals(1u, result.first)
        val paths = result.second
        assertEquals(1, paths.size)
        assertEquals(listOf(0u), paths.first().toList())
    }

    @Test fun singleStep() {
        val result = findHeaviestPath(listOf(uintArrayOf(1u, 2u)))
        println("${result.first}: "+ result.second.joinToString { it.toList().toString() })
        assertEquals(2u, result.first)
        val paths = result.second
        assertEquals(1, paths.size)
        assertEquals(listOf(1u), paths.first().toList())
    }

    @Test fun ambiguousStep() {
        val result = findHeaviestPath(listOf(uintArrayOf(1u, 1u)))
        println("${result.first}: "+ result.second.joinToString { it.toList().toString() })
        assertEquals(1u, result.first)
        val paths = result.second.toList()
        assertEquals(2, paths.size)
        val first = paths.first().toList()
        val second = paths[1].toList()
        val expect0 = listOf(0u)
        val expect1 = listOf(1u)
        assertTrue(expect0==first || expect0==second)
        assertTrue(expect1==first || expect1==second)
    }

    @Test fun easyQuestion() {
        val grid = readGrid("sampleGrid.data")
        val result = findHeaviestPath(grid)
        println("${result.first}: "+ result.second.joinToString { it.toList().toString() })
        assertTrue(result.first>75u)
        assertFalse(result.second.isEmpty())
    }
}
