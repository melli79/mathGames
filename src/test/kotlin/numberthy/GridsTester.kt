package numberthy

import common.math.ipow
import kotlin.test.*

class GridsTester {
    val dim = 3.toUByte()

    @Test fun addingToZeroGrid() {
        (1u..dim*dim).forEach { pos ->
            assertEquals(10uL.ipow((pos-1u).toUByte()), 0uL.add(pos.toUByte(), 1.toUByte()))
        }
    }

    @Test fun addingWithOverflow() {
        (1u..dim*dim).forEach { pos ->
            assertEquals(2u*10uL.ipow((pos-1u).toUByte()), (6u*10uL.ipow((pos-1u).toUByte())).add(pos.toUByte(), 6.toUByte()))
        }
    }

    @Test fun topLeft() {
        assertEquals(1012uL, mutate(1uL, dim, 1u.toUByte()))
    }

    @Test fun topRight() {
        assertEquals(100210uL, mutate(100uL, dim, dim))
    }

    @Test fun topMid() {
        assertEquals(10121uL, mutate(10uL, dim, 2.toUByte()))
    }

    @Test fun middleLeft() {
        assertEquals(1_012_001uL, mutate(1_000uL, dim, (dim+1uL).toUByte()))
    }

    @Test fun middleRight() {
        assertEquals(100_210_100uL, mutate(100_000uL, dim, (2u*dim).toUByte()))
    }

    @Test fun middle() {
        assertEquals(10_121_010uL, mutate(10_000uL, dim, (2u*dim-1u).toUByte()))
    }

    @Test fun bottomLeft() {
        assertEquals(12_001_000uL, mutate(1_000_000uL, dim, (dim*(dim-1u)+1uL).toUByte()))
    }

    @Test fun bottomRight() {
        assertEquals(210_100_000uL, mutate(100_000_000uL, dim, (dim*dim).toUByte()))
    }

    @Test fun bottomMid() {
        assertEquals(121_010_000uL, mutate(10_000_000uL, dim, (dim*dim-1u).toUByte()))
    }
}
