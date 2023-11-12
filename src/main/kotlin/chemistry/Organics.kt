package chemistry

import kotlin.math.max

@OptIn(ExperimentalUnsignedTypes::class)
data class OrganicMolecule(val clength :UByte, val type :BaseType =BaseType.Alkan, val moieties :List<PositionedMoiety> =emptyList()) {
    companion object {
        val names = listOf("hydrogen", "Methane", "Ethane", "Propane", "Butane", "Pentane",
            "Hexane", "Heptane", "Octane", "Nonane", "Decane")
    }

    override fun toString() :String {
        if (clength==1.toUByte())
            return methylToString()
        val moieties = getOrderedMoieties()

        return (1u..max(clength.toUInt(), moieties.keys.maxOfOrNull { p :UByte -> p.toUInt() } ?: 0u))
            .joinToString("- ") { n -> moieties.moietiesOfLengthToString(n) }
    }

    private fun Map<UByte, List<Moiety>>.moietiesOfLengthToString(n :UInt) :String {
        val countedMoieties :Map<Moiety, Int> = getOrDefault(n.toUByte(), emptyList()).groupBy { it }
            .map { entry -> Pair(entry.key, entry.value.size) }.toMap()
        val covalence = countedMoieties.entries.sumOf { it.value * if (it.key == Moiety.Oxygen) 2 else 1 }
        val numH = if (n==1u || n==clength.toUInt()) 3 -covalence
            else if (n<clength.toUInt()) 2 -covalence  else 0
        return (if (n <= clength.toUInt()) Nonmetal.Carbon.symbol else "") +
                (if (numH > 0) Nonmetal.Hydrogen.symbol + amount(numH.toUByte()) else "") +
                countedMoieties.entries.joinToString("") { en ->
                    en.key.toString() + amount(en.value.toUByte())
                }
    }

    private fun getOrderedMoieties() :Map<UByte, List<Moiety>> = moieties.flatMap { pm ->
        if (pm.positions.isEmpty()) listOf(Pair(1.toUByte(), pm.moiety))
        else pm.positions.map { p: UByte -> Pair(p, pm.moiety) }
    }.groupBy { p -> p.first }.entries.associate { (k: UByte, ps) -> Pair(k, ps.map { it.second }) }

    private fun methylToString(): String {
        val numH = (4 - moieties.sumOf { pm ->
            val valence = if (pm.moiety == Moiety.Oxygen) 2 else 1
            val num = pm.positions.size
            if (num < 1) valence else num * valence
        }).toUByte()
        return Nonmetal.Carbon.symbol + (if (numH > 0u) Nonmetal.Hydrogen.symbol + amount(numH) else "") +
                moieties.joinToString("")
    }

    fun getIupacName() :String {
        val prefix = moieties.joinToString("-"){ it.getIupacName() } +if (moieties.isNotEmpty()) "-" else ""
        val coreName = if (clength.toInt() < names.size) names[clength.toInt()] else "$clength-alkane"
        return when (type) {
            BaseType.Alkan -> prefix+coreName
            BaseType.Acid -> coreName +BaseType.Acid.suffix
            else -> coreName.substring(0 until coreName.length-1) + type.suffix
        }
    }

    enum class BaseType(val suffix :String) {
        Alkan("e"),
        Alcohol("ol"), Alcaloid("al"), Acid(" acid"),
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
data class PositionedMoiety(val moiety :Moiety, val positions :UByteArray =ubyteArrayOf()) {
    constructor(moiety :Moiety, pos :UByte) :this(moiety, ubyteArrayOf(pos))

    override fun toString() = when (positions.size) {
        0, 1 -> moiety.toString()
        else -> "(${moiety})_${positions.size} "
    }

    fun getIupacName() = when (positions.size) {
        0 -> moiety.getIupacName()
        1 -> "${positions[0]}-${moiety.getIupacName()}"
        else -> "(${positions.joinToString(","){ "$it-" }})-${count(positions.size.toUByte())}${moiety.getIupacName()}"
    }

    override fun equals(other :Any?) :Boolean {
        if (this === other) return true
        if (other !is PositionedMoiety) return false
        return (moiety==other.moiety) && positions.contentEquals(other.positions)
    }

    override fun hashCode() = 31* moiety.hashCode() +positions.contentHashCode()
}

fun createOrganicMolecule(clength :UByte, type :OrganicMolecule.BaseType) = when(type) {
    OrganicMolecule.BaseType.Alkan -> OrganicMolecule(clength, type)
    OrganicMolecule.BaseType.Alcohol -> OrganicMolecule(clength, type, listOf(PositionedMoiety(Moiety.Alcohol, clength)))
    OrganicMolecule.BaseType.Alcaloid -> OrganicMolecule(clength, type, listOf(PositionedMoiety(Moiety.Oxygen, clength)))
    OrganicMolecule.BaseType.Acid -> OrganicMolecule(clength, type, listOf(PositionedMoiety(Moiety.Oxygen, clength), PositionedMoiety(Moiety.Alcohol, clength)))
}

sealed interface Moiety {
    fun getIupacName() :String

    data class Alkyl(val clength :UByte) :Moiety {
        override fun getIupacName() :String {
            val name = OrganicMolecule.names[clength.toInt()]
            return name.substring(0 until name.length-3) +"yl"
        }

        override fun toString() = when (clength.toInt()) {
            0 -> ""
            1 -> Nonmetal.Carbon.symbol + Nonmetal.Hydrogen.symbol +"_3 "
            else -> "("+ (2u..clength.toUInt()).joinToString("-") {
                        Nonmetal.Carbon.symbol + Nonmetal.Hydrogen.symbol +"_2"
                    } +"-"+ Nonmetal.Carbon.symbol + Nonmetal.Hydrogen.symbol +"_3" +")"
        }
    }

    object Amine :Moiety {
        override fun getIupacName() = "amine"

        override fun toString() = Nonmetal.Nitrogen.symbol + Nonmetal.Hydrogen.symbol +"_2 "
    }

    object Alcohol :Moiety {
        override fun getIupacName() = "ol"

        override fun toString() = Nonmetal.Oxygen.symbol + Nonmetal.Hydrogen.symbol
    }

    object Oxygen :Moiety {
        override fun getIupacName() = "al"

        override fun toString() = Nonmetal.Oxygen.symbol
    }

    data class Inorganic1(val atom :Nonmetal) :Moiety {
        override fun getIupacName() :String {
            val name = atom.name
            return name.substring(0 until name.length-3) +"o"
        }

        override fun toString() = atom.symbol
    }

    class Inorganic2(element1 :Atom, element2 :Atom, amount1 :UByte, amount2 :UByte) :Moiety,
        Bond2(element1, element2, amount1, amount2) {}

    companion object {
        val methyl = Alkyl(1u)
        val cyanide = Inorganic2(Nonmetal.Carbon, Nonmetal.Nitrogen, 1u, 1u)
        val sulfhydril = Inorganic2(Nonmetal.Nitrogen, Nonmetal.Hydrogen, 1u,1u)
    }

    class Inorganic3(element1 :Atom, element2 :Atom, element3 :Atom, amount1 :UByte, amount2 :UByte, amount3 :UByte) :Moiety,
            Bond3(element1, element2, element3, amount1, amount2, amount3) {}
}
