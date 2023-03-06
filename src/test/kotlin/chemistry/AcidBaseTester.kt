package chemistry

import kotlin.test.*

class AcidBaseTester {
    @Test fun acidTester() {
        for (nonmetal in Nonmetal.values()) if (nonmetal!=Nonmetal.Oxygen && nonmetal!=Nonmetal.Hydrogen) {
            val acids = hydrate(nonmetal)
            println(acids)
            acids.forEach { a -> if (a !is Bond2 || a.element1!=Nonmetal.Nitrogen)
                assertEquals(Bond.Type.Acid, a.type, "$nonmetal: $a")
                else assertEquals(Bond.Type.Base, a.type, "$nonmetal: $a")
            }
        }
    }

    @Test fun baseTester() {
        for (metal in Metal.values()) {
            val bases = hydrate(metal)
            println(bases)
            bases.forEach { b -> assertEquals(Bond.Type.Base, b.type) }
        }
    }

    @Test fun semiMetalTester() {
        for (semimetal in Semimetal.values()) {
            val acids = hydrate(semimetal)
            println(acids)
            acids.forEach { a -> assertEquals(Bond.Type.Acid, a.type) }
        }
    }
}
