package dynamic

import dynamic.TowerOfHanoi.computeNumMoves
import dynamic.TowerOfHanoi.computeSplit
import dynamic.TowerOfHanoi.shuffle
import java.util.Stack
import kotlin.test.*

class HanoiTester {
    @Test fun zero() {
        val split = computeSplit(0u, 3u)
        assertEquals(0u, split)
        assertEquals(0uL, computeNumMoves(0u, 2u))
    }

    @Test fun one() {
        val split = computeSplit(1u, 2u)
        assertEquals(0u, split)
        assertEquals(1uL, computeNumMoves(1u, 2u))
    }

    @Test fun two2t() {
        assertEquals(ULong.MAX_VALUE, computeNumMoves(2u, 2u))
    }

    @Test fun two3t() {
        val split = computeSplit(2u, 3u)
        assertEquals(1u, split)
        assertEquals(3uL, computeNumMoves(2u, 3u))
        val src = Stack<UInt>();  val tar = Stack<UInt>();  val aux = Stack<UInt>()
        src.push(2u);  src.push(1u)
        shuffle(src, 2u, tar, listOf(aux))
        assertTrue(src.empty())
        assertTrue(aux.empty())
        assertContentEquals(listOf(2u,1u), tar)
    }

    @Test fun three3t() {
        val split = computeSplit(3u, 3u)
        assertEquals(1u, split)
        assertEquals(7uL, computeNumMoves(3u, 3u))
        val src = Stack<UInt>();  val tar = Stack<UInt>();  val aux = Stack<UInt>()
        src.push(3u);  src.push(2u);  src.push(1u)
        shuffle(src, 3u, tar, listOf(aux))
        assertTrue(src.empty())
        assertTrue(aux.empty())
        assertContentEquals(listOf(3u, 2u,1u), tar)
    }

    @Test fun three4t() {
        val split = computeSplit(3u, 4u)
        assertTrue(split in listOf(1u, 2u))
        assertEquals(5uL, computeNumMoves(3u, 4u))
        val src = Stack<UInt>();  val tar = Stack<UInt>();  val aux = Stack<UInt>();  val aux2 = Stack<UInt>()
        src.push(3u);  src.push(2u);  src.push(1u)
        shuffle(src, 3u, tar, listOf(aux, aux2))
        assertTrue(src.empty())
        assertTrue(aux.empty())
        assertTrue(aux2.empty())
        assertContentEquals(listOf(3u, 2u,1u), tar)
    }

    @Test fun four3t() {
        val split = computeSplit(4u, 3u)
        assertEquals(1u, split)
        assertEquals(15uL, computeNumMoves(4u, 3u))
        val src = Stack<UInt>();  val tar = Stack<UInt>();  val aux = Stack<UInt>()
        src.push(4u);  src.push(3u);  src.push(2u);  src.push(1u)
        shuffle(src, 4u, tar, listOf(aux))
        assertTrue(src.empty())
        assertTrue(aux.empty())
        assertContentEquals(listOf(4u, 3u, 2u,1u), tar)
    }

    @Test fun fourT64() {
        val count = computeNumMoves(64u, 4u)
        println("Shuffling ${64} disks via ${4} towers takes $count moves.")
        assertTrue(count<19_000uL)
    }
}
