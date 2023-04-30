package probability

import kotlin.test.*

class MultiBirthdayTester {
    @Test fun withinDigits() {
        val n0 = 10u
        for (n in 3u..(2u*n0+1u)) {
            val p = estimateMultiBirthdayP(n0, n.toUShort(), 3u)
            val pManyReps = estimateHighMutliRepetitionsP(n0, n.toUShort())
            println("$n: p= %.2f%%, <Rep> = %.1f%%".format(p*100, pManyReps*100))
        }
    }
}
