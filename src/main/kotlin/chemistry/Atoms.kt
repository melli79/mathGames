package chemistry

import kotlin.math.pow

sealed interface Atom {
    val name :String
    val valences :ByteArray
    val order :UByte
    val group :Group
    val symbol :String
    val masses :Map<Double, Double>

    fun getIupacName() = name

    val period :UByte
        get() = when(order.toInt()) {
            in 1..2 -> 1u
            in 3..10 -> 2u
            in 11..18 -> 3u
            in 19..36 -> 4u
            in 37..54 -> 5u
            in 55..86 -> 6u
            in 87..118 -> 7u
            else -> 8u
        }

    val mass :Double
        get() = if (masses.isEmpty()) order.toDouble() + when {
            order<=28u -> 1.15*order.toDouble()
            else -> order.toDouble().pow(1.1125) -7
        } else masses.entries.sumOf { (p, m) -> p*m }

    fun describe() = """^%.0f ${symbol}_$order^$group""".format(mass)

    enum class Group(val nr :Byte) {
        Alkali(1), Earths(2),
        RareEarths(-3), IV(-4), V(-5), VI(-6), VII(-7),
        VIIIa(-8), VIIIb(-8), VIIIc(-8), I(-1), II(-2),
        Lanthanides(-3), Actinides(-3),
        Borons(3), Carbons(4), Nitrogenes(5), Oxygenes(6), Halogen(7),
        Nobles(8)
    }
}

enum class Metal(override val valences :ByteArray, override val order :UByte, override val group :Atom.Group,
                 symb :String? =null, override val masses :Map<Double, Double> =emptyMap()
) :Atom {
    Lithium(byteArrayOf(1), 3u, Atom.Group.Alkali), Sodium(byteArrayOf(1), 11u, Atom.Group.Alkali, "Na"),
        Potassium(byteArrayOf(1), 19u, Atom.Group.Alkali, "K"), Rubidium(byteArrayOf(1), 37u, Atom.Group.Alkali, "Rb"),
        Caesium(byteArrayOf(1), 55u, Atom.Group.Alkali, "Cs"), Franzium(byteArrayOf(1), 87u, Atom.Group.Alkali),
    Beryllium(byteArrayOf(2), 4u, Atom.Group.Earths), Magnesium(byteArrayOf(2), 12u, Atom.Group.Earths, "Mg"),
        Calcium(byteArrayOf(2), 20u, Atom.Group.Earths), Strontium(byteArrayOf(2), 38u, Atom.Group.Earths, "Sr"),
        Barium(byteArrayOf(2), 56u, Atom.Group.Earths), Radium(byteArrayOf(2), 88u, Atom.Group.Earths),
    Scandium(byteArrayOf(1), 21u, Atom.Group.RareEarths), Yttrium(byteArrayOf(1), 39u, Atom.Group.RareEarths),
        Lathanium(byteArrayOf(1), 57u, Atom.Group.Lanthanides), Cerium(byteArrayOf(1), 58u, Atom.Group.Lanthanides),
        Neodym(byteArrayOf(1), 60u, Atom.Group.Lanthanides, "Nd"),
        Actinium(byteArrayOf(1), 89u, Atom.Group.Actinides), Thorium(byteArrayOf(1), 90u, Atom.Group.Actinides),
        Uranium(byteArrayOf(1, 4, 6), 92u, Atom.Group.Actinides, "U"), Neptunium(byteArrayOf(1,4,6), 93u, Atom.Group.Actinides, "Np"),
        Plutonium(byteArrayOf(1,4,6), 94u, Atom.Group.Actinides, "Pu"),
    Titanium(byteArrayOf(2,4), 22u, Atom.Group.IV), Zirconium(byteArrayOf(2,4), 40u, Atom.Group.IV, "Zr"),
        Hafnium(byteArrayOf(2,4), 72u, Atom.Group.IV, "Hf"),
    Vanadium(byteArrayOf(2,3,5), 23u, Atom.Group.V), Niobium(byteArrayOf(2,3,5), 41u, Atom.Group.V, "Nb"),
        Tantalum(byteArrayOf(2,3,5), 73u, Atom.Group.V),
    Chromium(byteArrayOf(2,3,4,5,6), 24u, Atom.Group.VI, "Cr"), Molybdenum(byteArrayOf(2,3,4,6), 42u, Atom.Group.VI),
        Wolfram(byteArrayOf(4,6), 74u, Atom.Group.VI, "W"),
    Mangan(byteArrayOf(2,3,4,6,7), 25u, Atom.Group.VII, "Mn"), Technetium(byteArrayOf(2,3,4,6,7), 43u, Atom.Group.VII, "Tc"),
        Rhenium(byteArrayOf(2,3,4,6,7), 75u, Atom.Group.VII, "Re"), Bohrium(byteArrayOf(2,3,4,6,7), 107u, Atom.Group.VII, "Bh"),
    Ferrum(byteArrayOf(2, 3), 26u, Atom.Group.VIIIa), Ruthenium(byteArrayOf(2, 3), 44u, Atom.Group.VIIIa),
        Osmium(byteArrayOf(2, 3), 76u, Atom.Group.VIIIa),
    Cobalt(byteArrayOf(2,3,4,5), 27u, Atom.Group.VIIIb), Rhodium(byteArrayOf(2,3,4,5), 45u, Atom.Group.VIIIb),
        Iridium(byteArrayOf(2,3,4,5), 77u, Atom.Group.VIIIb),
    Nickel(byteArrayOf(1,3,4), 28u, Atom.Group.VIIIc), Palladium(byteArrayOf(1,3,4), 46u, Atom.Group.VIIIc, "Pd"),
        Platinum(byteArrayOf(1,4), 78u, Atom.Group.VIIIc, "Pt"),
    Copper(byteArrayOf(1, 2), 29u, Atom.Group.I, "Cu"), Argentum(byteArrayOf(1), 47u, Atom.Group.I, "Ag"),
        Aurum(byteArrayOf(1, 2), 79u, Atom.Group.I),
    Zinc(byteArrayOf(2), 30u, Atom.Group.II), Cadmium(byteArrayOf(2), 48u, Atom.Group.II, "Cd"),
        Mercury(byteArrayOf(1, 2), 80u, Atom.Group.II, "Hg"),
    Aluminium(byteArrayOf(3), 13u, Atom.Group.Borons), Gallium(byteArrayOf(3), 31u, Atom.Group.Borons),
        Indium(byteArrayOf(3), 49u, Atom.Group.Borons), Thallium(byteArrayOf(3), 81u, Atom.Group.Borons, "Tl"),
    Stannium(byteArrayOf(2, 4), 50u, Atom.Group.Carbons, "Sn"), Plumbum(byteArrayOf(2, 4), 82u, Atom.Group.Carbons, "Pb"),
    Bismuth(byteArrayOf(3, 5), 83u, Atom.Group.Nitrogenes);

    override val symbol = symb ?: name.substring(0..1)
    override fun toString() = symbol
}

enum class Semimetal(override val valences :ByteArray, override val order :UByte, override val group :Atom.Group,
                     symb :String? =null, override val masses :Map<Double, Double> =emptyMap()
) :Atom {
    Boron(byteArrayOf(3), 5u, Atom.Group.Borons, "B"),
    Silicon(byteArrayOf(4, -4), 14u, Atom.Group.Carbons), Germanium(byteArrayOf(4, -4), 32u, Atom.Group.Carbons),
    Arsenic(byteArrayOf(-3, 5), 33u, Atom.Group.Nitrogenes, "As"), Antimony(byteArrayOf(-3, 5), 51u, Atom.Group.Nitrogenes, "Sb"),
    Tellurium(byteArrayOf(-2, 6), 52u, Atom.Group.Oxygenes), Polonium(byteArrayOf(-2, 6), 84u, Atom.Group.Oxygenes),
    Astatine(byteArrayOf(7, -1), 85u, Atom.Group.Halogen, "At");

    override val symbol = symb ?: name.substring(0..1)
    override fun toString() = symbol
}

enum class Nonmetal(override val valences :ByteArray, override val order :UByte,
                    override val group :Atom.Group =Atom.Group.Halogen, symb :String? =null,
                    override val masses :Map<Double, Double> =emptyMap()
) :Atom {
    Hydrogen(byteArrayOf(1), 1u, Atom.Group.Alkali),
    Fluorine(byteArrayOf(-1), 9u), Chlorine(byteArrayOf(-1, 1, 3, 5, 7), 17u, symb= "Cl"), Bromine(byteArrayOf(-1, 1, 3, 5, 7), 35u, symb= "Br"),
        Iodine(byteArrayOf(-1, 1, 3, 5, 7), 53u),
    Oxygen(byteArrayOf(-2), 8u, Atom.Group.Oxygenes), Sulfur(byteArrayOf(-2, 4, 6), 16u, Atom.Group.Oxygenes),
        Selenium(byteArrayOf(-2, 4, 6), 34u, Atom.Group.Oxygenes, "Se"),
    Nitrogen(byteArrayOf(-3, 5), 7u), Phosphorus(byteArrayOf(-3, 5), 15u),
    Carbon(byteArrayOf(4, 2, -4), 6u, Atom.Group.Carbons);

    override val symbol = symb ?: name.substring(0..0)
    override fun toString() = symbol
}

enum class Noblegas(override val order :UByte, symb :String? =null,
                    override val group :Atom.Group =Atom.Group.Nobles,
                    override val valences :ByteArray =byteArrayOf(),
                    override val masses :Map<Double, Double> =emptyMap()
) :Atom {
    Helium(2u), Neon(10u), Argon(18u), Crypton(36u, symb= "Kr"), Xenon(54u),
        Radon(86u, symb= "Rn");

    override val symbol = symb ?: name.substring(0..1)
    override fun toString() = symbol
}
