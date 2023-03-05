package chemistry

import kotlin.test.*

class PeriodicTableTester {
    val elements = (Metal.values().toList<Atom>()+Semimetal.values()+Nonmetal.values()+Noblegas.values()).sortedBy { it.order }

    @Test fun periods() {
        var period = 0.toUByte()
        for (element in elements) {
            if (period<element.period) {
                period = element.period
                print("\n$period:  ")
            }
            print(element.describe()+", ")
        }
    }
}
