package numberthy

import common.math.numthy.Matp
import common.math.numthy.inv
import common.math.numthy.matp
import kotlin.test.*

@OptIn(ExperimentalUnsignedTypes::class)
class LinSolvepTester {
    val p = 5u

    @Test
    fun id() {
        val m = Matp.one(p, 3)
        assertEquals(Matp.one(p, 3), m)
        val m1 = m.inv()
        assertEquals(m, m1)
    }

    @Test
    fun inv() {
        val m = Matp(p, arrayOf(uintArrayOf(1u,0u,1u), uintArrayOf(0u,1u,0u), uintArrayOf(0u,0u,1u)))
        assertNotEquals(Matp.one(p, 3), m)
        val m1 = m.inv()
        assertEquals(Matp.one(p, 3), m*m1)
        assertEquals(Matp.one(p, 3), m1*m)
    }

    @Test fun invRandom() {
        while (true) {
            val m = random.matp(p, 4, 4)
            val m1 :Matp
            try {
                m1 = m.inv()
            } catch (e :AssertionError) {
                print(".");  System.out.flush()
                continue
            }
            assertEquals(Matp.one(p, 4), m*m1)
            assertEquals(Matp.one(p, 4), m1*m)
            break
        }
    }
}
