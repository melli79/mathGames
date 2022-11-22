package chemistry

import kotlin.test.*

class BondTester {
    @Test fun noBondsWithNobleGases() {
        val result = bind(Atom.Metal.Sodium, Atom.Noblegas.Helium)
        assertEquals(emptySet(), result)

        val result2 = bind(Atom.Nonmetal.Fluorine, Atom.Noblegas.Neon)
        assertEquals(emptySet(), result2)
    }

    @Test fun hydrogenOxides() {
        val result = bind(Atom.Nonmetal.Hydrogen, Atom.Nonmetal.Oxygen)
        assertEquals(2, result.size)
        val water = result.find { b -> b.amount1>1.toUByte() }
        assertNotNull(water)
        assertEquals(2.toUByte(), water.amount1)
        assertEquals(1.toUByte(), water.amount2)
        val peroxide = result.find { b -> b.amount1==1.toUByte() }
        assertNotNull(peroxide)
        assertEquals(1.toUByte(), peroxide.amount2)
    }

    @Test fun copperOxides() {
        val result = bind(Atom.Metal.Copper, Atom.Nonmetal.Oxygen)
        println(result)
        assertEquals(2, result.size)
        val copper1 = result.find { b -> b.amount1==2.toUByte() }
        assertNotNull(copper1)
        assertEquals(1.toUByte(), copper1.amount2)

        val copper2 = result.find { b -> b.amount1==1.toUByte() }
        assertNotNull(copper2)
        assertEquals(1.toUByte(), copper2.amount2)
    }

    @Test fun nitrogenOxides() {
        val result = bind(Atom.Nonmetal.Nitrogen, Atom.Nonmetal.Oxygen)
        println(result)
        val monoNitro = result.find { b -> b.amount1==1.toUByte() }
        assertNotNull(monoNitro)
        assertEquals(1.toUByte(), monoNitro.amount2)

        val diNitro = result.find { b -> b.amount1==2.toUByte() }
        assertNotNull(diNitro)
        assertEquals(1.toUByte(), diNitro.amount2)
    }

    @Test fun bondsOfMetals() {
        for (metal in Atom.Metal.values()) {
            print("$metal:  ")
            for (nonmetal in Atom.Nonmetal.values()) {
                print("${bind(metal, nonmetal)}, ")
            }
            println()
        }
    }

    @Test fun bondsOfNonmetals() {
        for (nonmetal in Atom.Nonmetal.values()) if (nonmetal!=Atom.Nonmetal.Oxygen) {
            print("$nonmetal: ")
            for (oxidizer in oxidizers) if (nonmetal!=oxidizer) {
                print("${bind(nonmetal, oxidizer)}, ")
            }
            println()
        }
    }
}
