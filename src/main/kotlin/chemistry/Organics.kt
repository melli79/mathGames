package chemistry

data class OrganicMolecule(val clength :UByte, val type :BaseType =BaseType.Alkan, val moieties :List<PositionedMoiety> =emptyList()) {
    companion object {
        val names = listOf("hydrogene", "Methane", "Ethane", "Propane", "Butane", "Pentane",
            "Hexane", "Heptane", "Octane", "Nonane", "Decane")
    }

    override fun toString() :String {
        val prefix = moieties.joinToString("-") +if (moieties.isNotEmpty()) "-" else ""
        val coreName = if (clength.toInt() < names.size) names[clength.toInt()] else "$clength-alkane"
        return prefix+when (type) {
            BaseType.Alkan -> coreName
            BaseType.Acid -> coreName +BaseType.Acid.suffix
            else -> coreName.substring(0 until coreName.length-1) + type.suffix
        }
    }

    enum class BaseType(val suffix :String) {
        Alkan("e"),
        Alcohol("ol"), Alcaloid("al"), Acid(" acid"),
    }
}

data class PositionedMoiety(val moiety :Moiety, val positions :Array<UByte>) {
    constructor(moiety :Moiety, pos :UByte) :this(moiety, arrayOf(pos))

    override fun toString() = when (positions.size) {
        0 -> moiety.toString()
        1 -> "${positions[0]}-$moiety"
        else -> "(${positions.joinToString(","){ "$it-" }})-${count(positions.size.toUByte())}$moiety"
    }

    override fun equals(other :Any?) :Boolean {
        if (this === other) return true
        if (other !is PositionedMoiety) return false
        return (moiety==other.moiety) && positions.contentEquals(other.positions)
    }

    override fun hashCode() = 31* moiety.hashCode() +positions.contentHashCode()
}

sealed interface Moiety {
    data class Organic(val clength :UByte) :Moiety {
        override fun toString() :String {
            val name = OrganicMolecule.names[clength.toInt()]
            return name.substring(0 until name.length-3) +"yl"
        }
    }

    object Amine :Moiety {
        override fun toString() = "amine"
    }

    object Alcohol :Moiety {
        override fun toString() = "ol"
    }

    data class Inorganic1(val atom :Atom.Nonmetal) :Moiety {
        override fun toString() :String {
            val name = atom.toString()
            return name.substring(0 until name.length-3) +"o"
        }
    }

    class Inorganic2(element1 :Atom, element2 :Atom, amount1 :UByte, amount2 :UByte, type :Bond.Type) :Moiety,
        Bond2(element1, element2, amount1, amount2, type) {}

    class Inorganic3(element1 :Atom, element2 :Atom, element3 :Atom, amount1 :UByte, amount2 :UByte, amount3 :UByte) :Moiety,
            Bond3(element1, element2, element3, amount1, amount2, amount3) {}
}
