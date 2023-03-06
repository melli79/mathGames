package chemistry

import kotlin.test.*

@OptIn(ExperimentalUnsignedTypes::class)
class OrganicsTester {
    @Test fun alkanes() {
        for (n in 1..15) {
            val alkan = OrganicMolecule(n.toUByte(), OrganicMolecule.BaseType.Alkan)
            println("${alkan.getIupacName()}: $alkan")
        }
    }

    @Test fun Alcanoles() {
        for (n in 1..10) {
            val alkanol = createOrganicMolecule(n.toUByte(), OrganicMolecule.BaseType.Alcohol)
            println("${alkanol.getIupacName()}: $alkanol,")
            val alkanal = createOrganicMolecule(n.toUByte(), OrganicMolecule.BaseType.Alcaloid)
            println("  ${alkanal.getIupacName()}: $alkanal")
        }
    }

    @Test fun Acids() {
        for (n in 1..10) {
            val acid = createOrganicMolecule(n.toUByte(), OrganicMolecule.BaseType.Acid)
            println("${acid.getIupacName()}: $acid")
        }
    }

    @Test fun polyAlcohol() {
        val propantriol = OrganicMolecule(3u, moieties= listOf(
            PositionedMoiety(Moiety.Alcohol, ubyteArrayOf(1u, 2u, 3u) )))
        println("${propantriol.getIupacName()}: $propantriol")
    }

    @Test fun amines() {
        val diamineHexan = OrganicMolecule(6u,
            moieties= listOf(
                PositionedMoiety(Moiety.Amine, ubyteArrayOf(2u, 4u))
            ))
        println("${diamineHexan.getIupacName()}: $diamineHexan")
    }

    @Test fun halogeneAlkan() {
        val fckw = OrganicMolecule(1u, moieties= listOf(
            PositionedMoiety(Moiety.Inorganic1(Nonmetal.Chlorine), ubyteArrayOf(1u,1u,1u)),
            PositionedMoiety(Moiety.Inorganic1(Nonmetal.Fluorine))
        ))
        println(fckw.getIupacName()+": $fckw")
    }

    @Test fun polyalcane() {
        val methylEthylHexane = OrganicMolecule(6u, moieties= listOf(
            PositionedMoiety(Moiety.Alkyl(1u), 2u),
            PositionedMoiety(Moiety.Alkyl(2u), 4u),
        ))
        println(methylEthylHexane.getIupacName()+": $methylEthylHexane")
    }
}
