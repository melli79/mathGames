package numberthy

import common.math.numthy.Matp
import common.math.numthy.Vecp
import kotlin.test.*

@OptIn(ExperimentalUnsignedTypes::class)
class MatpTester {
    val p = 97u

    @Test
    fun matVecId() {
        val m = Matp(p, arrayOf(uintArrayOf(1u,0u,0u), uintArrayOf(0u,1u,0u), uintArrayOf(0u,0u,1u)))
        val v = Vecp(p, uintArrayOf(1u,2u,3u))
        assertEquals(v, m*v)
    }

    @Test
    fun matVec() {
        val m = Matp(p, arrayOf(uintArrayOf(1u,0u,0u,1u), uintArrayOf(0u,1u,0u,0u), uintArrayOf(0u,0u,1u,0u), uintArrayOf(0u,0u,0u,1u)))
        val v = Vecp(p, uintArrayOf(1u,0u,0u,2u))
        assertEquals(Vecp(p, uintArrayOf(3u,0u,0u,2u)), m*v)
    }
}
