package chemistry

import kotlin.test.*

class OrganicsTester {
    @Test fun alkanes() {
        for (n in 1..15) {
            println(OrganicMolecule(n.toUByte(), OrganicMolecule.BaseType.Alkan))
        }
    }

    @Test fun Alcanoles() {
        for (n in 1..10) {
            print(OrganicMolecule(n.toUByte(), OrganicMolecule.BaseType.Alcohol))
            println(", "+ OrganicMolecule(n.toUByte(), OrganicMolecule.BaseType.Alcaloid))
        }
    }

    @Test fun Acids() {
        for (n in 1..10) {
            println(OrganicMolecule(n.toUByte(), OrganicMolecule.BaseType.Acid))
        }
    }

    @Test fun polyAlcohol() {
        val propantriol = OrganicMolecule(3u, moieties= listOf(
            PositionedMoiety(Moiety.Alcohol, arrayOf(1u, 2u, 3u) )))
        println(propantriol)
    }

    @Test fun amines() {
        val diamineHexan = OrganicMolecule(6u,
            moieties= listOf(
                PositionedMoiety(Moiety.Amine, arrayOf(2u, 4u))
            ))
        println(diamineHexan)
    }

    @Test fun halogeneAlkan() {
        val fckw = OrganicMolecule(1u, moieties= listOf(
            PositionedMoiety(Moiety.Inorganic1(Atom.Nonmetal.Chlorine), arrayOf(2u,3u,4u)),
            PositionedMoiety(Moiety.Inorganic1(Atom.Nonmetal.Fluorine), 1u)
        ))
        println(fckw)
    }

    @Test fun polyalcane() {
        val methylEthylHexane = OrganicMolecule(6u, moieties= listOf(
            PositionedMoiety(Moiety.Organic(1u), 2u),
            PositionedMoiety(Moiety.Organic(2u), 4u),
        ))
        println(methylEthylHexane)
    }
}
