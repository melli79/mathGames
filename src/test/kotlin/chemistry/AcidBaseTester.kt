package chemistry

import kotlin.test.*

class AcidBaseTester {
    @Test fun acidTester() {
        for (nonmetal in Nonmetal.values()) if (nonmetal!=Nonmetal.Oxygen) {
            val acids = acidBase(nonmetal)
            println(acids)
            acids.forEach { a -> if (a !is Bond2 || a.element1!=Nonmetal.Nitrogen)
                assertEquals(Bond.Type.Acid, a.type)
                else assertEquals(Bond.Type.Base, a.type)
            }
        }
    }

    @Test fun baseTester() {
        for (metal in Metal.values()) {
            val bases = acidBase(metal)
            println(bases)
            bases.forEach { b -> assertEquals(Bond.Type.Base, b.type) }
        }
        for (semimetal in Semimetal.values()) {
            println(acidBase(semimetal))
        }
    }
}