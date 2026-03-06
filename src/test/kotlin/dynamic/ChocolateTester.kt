package dynamic

import kotlin.test.*

class ChocolateTester {
    @Test fun empty() {
        val pieces = emptyList<Piece>()
        val bar = Bar(5u,3u)
        val result = bar.partition(pieces)
        assertNotNull(result)
        assertTrue( result.isEmpty())
    }

    @Test fun onePieceTooBig() {
        val pieces = listOf(Piece(6u,3u))
        val bar = Bar(5u,3u)
        val result = bar.partition(pieces)
        assertNull( result)
    }

    @Test fun onePieceFits() {
        val pieces = listOf(Piece(2u,3u))
        val bar = Bar(5u,3u)
        val result = bar.partition(pieces)
        assertNotNull(result)
        assertEquals(Piece(2u,3u), result.getOrNull(0))
        assertEquals(1, result.size)
    }

    @Test fun onePieceFitsTurned() {
        val pieces = listOf(Piece(2u,3u))
        val bar = Bar(3u,2u)
        val result = bar.partition(pieces)
        assertNotNull(result)
        assertEquals(Piece(2u,3u), result.getOrNull(0))
        assertEquals(1, result.size)
    }

    @Test fun twoPiecesTooBig() {
        val pieces = listOf(Piece(3u,3u), Piece(3u,3u))
        val bar = Bar(5u,3u)
        val result = bar.partition(pieces)
        assertNull( result)
    }

    @Test fun twoPiecesFit() {
        val pieces = listOf(Piece(2u,3u), Piece(3u,3u))
        val bar = Bar(5u,3u)
        val result = bar.partition(pieces)
        assertNotNull(result)
        assertEquals(Piece(2u,3u), result.getOrNull(0))
        assertEquals(Piece(3u,3u), result.getOrNull(1))
        assertEquals(2, result.size)
    }

    @Test fun twoPiecesFitTurned() {
        val pieces = listOf(Piece(3u,2u), Piece(3u,3u))
        val bar = Bar(5u,3u)
        val result = bar.partition(pieces)
        assertNotNull(result)
        assertEquals(Piece(3u,2u), result.getOrNull(0))
        assertEquals(Piece(3u,3u), result.getOrNull(1))
        assertEquals(2, result.size)
    }
}
