package chemistry

sealed interface Atom {
    val name :String
    val valences :Array<Byte>
    val order :UByte
    val group :Group
    val symbol :String

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

    fun describe() = """${symbol}_$order^$group"""

    enum class Group(val order :Byte) {
        Alkali(1), Earths(2), Borons(3), Carbons(4),
        Nitrogenes(5), Oxygenes(6), Halogenes(7), Nobles(8),
        RareEarths(-3), IV(-4), V(-5), VI(-6), VII(-7),
        VIIIa(-8), VIIIb(-8), VIIIc(-8), I(-1), II(-2),
        Lathanides(-1), Actinides(-1)
    }
}
enum class Metal(override val valences :Array<Byte>, override val order :UByte, override val group :Atom.Group,
                 symb :String? =null) :Atom {
    Lithium(arrayOf(1), 3u, Atom.Group.Alkali), Sodium(arrayOf(1), 11u, Atom.Group.Alkali, "Na"), Potassium(arrayOf(1), 19u, Atom.Group.Alkali, "K"), Rubidium(arrayOf(1), 37u, Atom.Group.Alkali), Caesium(arrayOf(1), 55u, Atom.Group.Alkali, "Cs"), Franzium(arrayOf(1), 87u, Atom.Group.Alkali),
    Beryllium(arrayOf(2), 4u, Atom.Group.Earths), Magnesium(arrayOf(2), 12u, Atom.Group.Earths, "Mg"), Calcium(arrayOf(2), 20u, Atom.Group.Earths), Strontium(arrayOf(2), 38u, Atom.Group.Earths, "Sr"), Barium(arrayOf(2), 56u, Atom.Group.Earths), Radium(arrayOf(2), 88u, Atom.Group.Earths),
    Scandium(arrayOf(1), 21u, Atom.Group.RareEarths), Yttrium(arrayOf(1), 39u, Atom.Group.RareEarths), Thorium(arrayOf(1), 90u, Atom.Group.Actinides), Uranium(arrayOf(1, 4, 6), 92u, Atom.Group.Actinides, "U"), Neptunium(arrayOf(1,4,6), 93u, Atom.Group.Actinides, "Np"), Plutonium(arrayOf(1,4,6), 94u, Atom.Group.Actinides, "Pu"),
    Titanium(arrayOf(2,4), 22u, Atom.Group.IV), Zirconium(arrayOf(2,4), 40u, Atom.Group.IV, "Zr"), Hafnium(arrayOf(2,4), 72u, Atom.Group.IV, "Hf"),
    Vanadium(arrayOf(2,3,5), 23u, Atom.Group.V), Niobium(arrayOf(2,3,5), 41u, Atom.Group.V, "Nb"), Tantalum(arrayOf(2,3,5), 73u, Atom.Group.V),
    Chromium(arrayOf(2,3,4,5,6), 24u, Atom.Group.VI, "Cr"), Molybdenum(arrayOf(2,3,4,6), 42u, Atom.Group.VI), Wolfram(arrayOf(4,6), 74u, Atom.Group.VI, "W"),
    Mangan(arrayOf(2,3,4,6,7), 25u, Atom.Group.VII, "Mn"), Technetium(arrayOf(2,3,4,6,7), 43u, Atom.Group.VII, "Tc"), Rhenium(arrayOf(2,3,4,6,7), 75u, Atom.Group.VII, "Re"), Bohrium(arrayOf(2,3,4,6,7), 107u, Atom.Group.VII, "Bh"),
    Ferrum(arrayOf(2, 3), 26u, Atom.Group.VIIIa), Ruthenium(arrayOf(2, 3), 44u, Atom.Group.VIIIa), Osmium(arrayOf(2, 3), 76u, Atom.Group.VIIIa),
    Cobalt(arrayOf(2,3,4,5), 27u, Atom.Group.VIIIb), Rhodium(arrayOf(2,3,4,5), 45u, Atom.Group.VIIIb), Iridium(arrayOf(2,3,4,5), 77u, Atom.Group.VIIIb),
    Nickel(arrayOf(1,3,4), 28u, Atom.Group.VIIIc), Palladium(arrayOf(1,3,4), 46u, Atom.Group.VIIIc, "Pd"), Platinum(arrayOf(1,4), 78u, Atom.Group.VIIIc, "Pt"),
    Copper(arrayOf(1, 2), 29u, Atom.Group.I, "Cu"), Argentum(arrayOf(1), 47u, Atom.Group.I, "Ag"), Aurum(arrayOf(1, 2), 79u, Atom.Group.I),
    Zinc(arrayOf(2), 30u, Atom.Group.II), Cadmium(arrayOf(2), 48u, Atom.Group.II, "Cd"), Mercury(arrayOf(1, 2), 80u, Atom.Group.II, "Hg"),
    Aluminium(arrayOf(3), 13u, Atom.Group.Borons), Gallium(arrayOf(3), 31u, Atom.Group.Borons), Indium(arrayOf(3), 49u, Atom.Group.Borons), Thallium(arrayOf(3), 81u, Atom.Group.Borons, "Tl"),
    Stannium(arrayOf(2, 4), 50u, Atom.Group.Carbons, "Sn"), Plumbum(arrayOf(2, 4), 82u, Atom.Group.Carbons, "Pb"),
    Bismuth(arrayOf(3, 5), 83u, Atom.Group.Nitrogenes),
    Astatine(arrayOf(7, -1), 85u, Atom.Group.Halogenes, "At");

    override val symbol = symb ?: name.substring(0..1)
}

enum class Semimetal(override val valences :Array<Byte>, override val order :UByte, override val group :Atom.Group,
                     symb :String? =null) :Atom {
    Boron(arrayOf(3), 5u, Atom.Group.Borons, "B"),
    Silicon(arrayOf(4, -4), 14u, Atom.Group.Carbons), Germanium(arrayOf(4, -4), 32u, Atom.Group.Carbons),
    Arsen(arrayOf(-3, 5), 33u, Atom.Group.Nitrogenes, "As"), Antimony(arrayOf(-3, 5), 51u, Atom.Group.Nitrogenes, "Sb"),
    Tellurium(arrayOf(-2, 6), 52u, Atom.Group.Oxygenes), Polonium(arrayOf(-2, 6), 84u, Atom.Group.Oxygenes)
    ;

    override val symbol = symb ?: name.substring(0..1)
}

enum class Nonmetal(override val valences :Array<Byte>, override val order :UByte,
                    override val group :Atom.Group =Atom.Group.Halogenes, private val symb :String? =null) :Atom {
    Hydrogen(arrayOf(1), 1u, Atom.Group.Alkali),
    Fluorine(arrayOf(-1), 9u), Chlorine(arrayOf(-1, 1, 3, 5, 7), 17u, symb= "Cl"), Bromine(arrayOf(-1, 1, 3, 5, 7), 35u, symb= "Br"), Iodine(arrayOf(-1, 1, 3, 5, 7), 53u),
    Oxygen(arrayOf(-2), 8u, Atom.Group.Oxygenes), Sulfur(arrayOf(-2, 4, 6), 16u, Atom.Group.Oxygenes), Selenium(arrayOf(-2, 4, 6), 34u, Atom.Group.Oxygenes, "Se"),
    Nitrogen(arrayOf(-3, 5), 7u), Phosphorus(arrayOf(-3, 5), 15u),
    Carbon(arrayOf(4, 2, -4), 6u, Atom.Group.Carbons);

    override val symbol = symb ?: name.substring(0..0)
}

enum class Noblegas(override val valences :Array<Byte> =emptyArray(), override val order :UByte,
                    override val group :Atom.Group =Atom.Group.Nobles, private val symb :String? =null) :Atom {
    Helium(order= 2u), Neon(order= 10u), Argon(order= 18u), Crypton(order= 36u), Xenon(order= 54u), Radon(order= 86u, symb= "Rn");

    override val symbol = symb ?: name.substring(0..1)
}
