package chemistry

import kotlin.test.*

class AcidBaseTester {
    @Test fun acidTester() {
        for (nonmetal in Atom.Nonmetal.values()) if (nonmetal!=Atom.Nonmetal.Oxygen) {
            val acids = acidBase(nonmetal)
            println(acids)
            acids.forEach { a -> if (a !is Bond2 || a.element1!=Atom.Nonmetal.Nitrogen)
                assertEquals(Bond.Type.Acid, a.type)
                else assertEquals(Bond.Type.Base, a.type)
            }
        }
    }

    @Test fun baseTester() {
        for (metal in Atom.Metal.values()) {
            val bases = acidBase(metal)
            println(bases)
            bases.forEach { b -> assertEquals(Bond.Type.Base, b.type) }
        }
        for (semimetal in Atom.Semimetal.values()) {
            println(acidBase(semimetal))
        }
    }
}