package diophantine

import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.test.*

class QuadraticPrimitiveRootTester {
    @Test fun three() {
        val result = findPrimitive(3u)
        println("Primitive root in Q[√3] = ${result?.first}±${result?.second}√3")
        assertEquals(Pair(2u,1u), result)
    }
    @Test fun thirteen() {
        val result = findPrimitive(13u)
        println("Primitive root in Q[√13] = ${result?.first}±${result?.second}√13")
        assertEquals(Pair(649u, 180u), result)
    }

    @Test fun smallNumbers() {
        for (d in 2u..99u) {
            val result = findPrimitive(d)
            if (result!=null) {
                println("Primitive root in Q[√$d)] = ${result.first} ±${result.second}√$d)")
                assertEquals(sqr(result.first.toInt()), d*sqr(result.second.toInt())+1uL)
            } else if (d.toULong() != sqr(sqrt(d.toDouble()).roundToInt()))
                println("Failed for Q[√$d)]")
        }
    }
}
