package probability

import kotlin.test.*

class BirthdayParadoxTester {
    @Test fun classical() {
        val n = 365u
        val result = findThreshold(n)
        assertEquals(1, result.size)
        println("P_$n(k)=50% at k ≈ ${result.first()}")
        assertEquals(22.8, result.first(), 0.1)
    }

    @Test fun smallN() {
        for (n in listOf(1,2,3,4,5,10,20,30,40,50,60,70,80,90,100,200,300,400,500,600,700,800,900,1000,2000)) {
            print("$n:%.1f,  ".format(findThreshold(n.toUInt()).first()))
        }
        println()
    }
}
