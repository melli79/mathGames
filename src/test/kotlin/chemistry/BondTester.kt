package chemistry

import kotlin.test.*

class BondTester {
    @Test fun noBondsWithNobleGases() {
        val result = bind(Nonmetal.Fluorine, Noblegas.Helium)
        println("${Noblegas.Helium}: $result")
        assertEquals(emptySet(), result)

        val result2 = bind(Nonmetal.Fluorine, Noblegas.Neon)
        println("${Noblegas.Neon}: $result2")
        assertEquals(emptySet(), result2)
        println("${Noblegas.Argon.name}: ${bind(Nonmetal.Fluorine, Noblegas.Argon)}")
        println("${Noblegas.Crypton.name}: ${bind(Nonmetal.Fluorine, Noblegas.Crypton)}")
        println("${Noblegas.Xenon.name}: ${bind(Nonmetal.Fluorine, Noblegas.Xenon)}")
        println("${Noblegas.Radon.name}: ${bind(Nonmetal.Fluorine, Noblegas.Radon)}")
    }

    @Test fun hydrogenOxides() {
        val result = bind(Nonmetal.Hydrogen, Nonmetal.Oxygen)
        println(result)
        assertEquals(2, result.size)
        val water = result.find { b -> b.amount2==1.toUByte() }
        assertNotNull(water)
        assertEquals(2.toUByte(), water.amount1)
        assertEquals(1.toUByte(), water.amount2)
        val peroxide = result.find { b -> b.amount2==2.toUByte() }
        assertNotNull(peroxide)
        assertEquals(2.toUByte(), peroxide.amount1)
    }

    @Test fun copperOxides() {
        val result = bind(Metal.Copper, Nonmetal.Oxygen)
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
        val result = bind(Nonmetal.Nitrogen, Nonmetal.Oxygen)
        println(result)
        val monoNitro = result.find { b -> b.amount1==1.toUByte() }
        assertNotNull(monoNitro)
        assertEquals(1.toUByte(), monoNitro.amount2)

        val diNitro = result.find { b -> b.amount1==2.toUByte() }
        assertNotNull(diNitro)
        assertEquals(1.toUByte(), diNitro.amount2)
    }

    @Test fun metalOxides() {
        for (metal in Metal.values()) {
            println("${metal.name}: ${bind(metal, Nonmetal.Oxygen)}")
        }
    }

    @Test fun salts() {
        for (metal in Metal.values()) {
            print("${metal.name}:  ")
            for (nonmetal in Nonmetal.values()) if (nonmetal!=Nonmetal.Oxygen) {
                print("${bind(metal, nonmetal)}, ")
            }
            println()
        }
    }

    val weakMetals = listOf(
        Metal.Sodium, Metal.Potassium, Metal.Rubidium, Metal.Franzium, Metal.Caesium,
        Metal.Calcium, Metal.Gallium
    )

    @Test fun bondsOfSemimetals() {
        for (element in Semimetal.values()) {
            print(element.getIupacName()+":  ")
            for (metal in weakMetals) {
                print("${bind(metal, element)}, ")
            }
            for (oxidizer in oxidizers) {
                print("${bind(element, oxidizer)}, ")
            }
            println()
        }
    }

    @Test fun bondsOfNonmetals() {
        for (nonmetal in Nonmetal.values()) if (nonmetal!=Nonmetal.Oxygen) {
            print("${nonmetal.name}: ")
            for (oxidizer in oxidizers) if (nonmetal!=oxidizer) {
                print("${bind(nonmetal, oxidizer)}, ")
            }
            println()
        }
    }
}
