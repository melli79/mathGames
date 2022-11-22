package chemistry

sealed interface Atom {
    val valences :Array<Byte>

    enum class Metal(override val valences :Array<Byte>) :Atom {
        Lithium(arrayOf(1)), Sodium(arrayOf(1)), Potassium(arrayOf(1)), Rubidium(arrayOf(1)), Caesium(arrayOf(1)), Franzium(arrayOf(1)),
        Beryllium(arrayOf(2)), Magnesium(arrayOf(2)), Calcium(arrayOf(2)), Strontium(arrayOf(2)), Barium(arrayOf(2)), Radium(arrayOf(2)),
        Scandium(arrayOf(1)), Yttrium(arrayOf(1)), Thorium(arrayOf(1)), Uranium(arrayOf(1, 4, 6)), Neptunium(arrayOf(1,4,6)), Plutonium(arrayOf(1,4,6)),
        Titanium(arrayOf(2,4)), Zirconium(arrayOf(2,4)), Hafnium(arrayOf(2,4)),
        Vanadium(arrayOf(2,3,5)), Niobium(arrayOf(2,3,5)), Tantalum(arrayOf(2,3,5)),
        Chromium(arrayOf(2,3,4,5,6)), Molybdenum(arrayOf(2,3,4,6)), Wolfram(arrayOf(4,6)),
        Mangan(arrayOf(2,3,4,6,7)), Technetium(arrayOf(2,3,4,6,7)), Rhenium(arrayOf(2,3,4,6,7)), Bohrium(arrayOf(2,3,4,6,7)),
        Ferrum(arrayOf(2, 3)), Ruthenium(arrayOf(2, 3)), Osmium(arrayOf(2, 3)),
        Cobalt(arrayOf(2,3,4,5)), Rhodium(arrayOf(2,3,4,5)), Iridium(arrayOf(2,3,4,5)),
        Nickel(arrayOf(1,3,4)), Palladium(arrayOf(1,3,4)), Platinum(arrayOf(1,4)),
        Copper(arrayOf(1, 2)), Argentum(arrayOf(1)), Aurum(arrayOf(1, 2)),
        Zinc(arrayOf(2)), Cadmium(arrayOf(2)), Mercury(arrayOf(1, 2)),
        Bor(arrayOf(3)), Aluminium(arrayOf(3)), Gallium(arrayOf(3)), Indium(arrayOf(3)), Thallium(arrayOf(3)),
        Stannium(arrayOf(2, 4)), Plumbum(arrayOf(2, 4)),
        Bismuth(arrayOf(3, 5)),
        Polonium(arrayOf(6, 2, 4)),
        Astatine(arrayOf(7, -1)),
    }

    enum class Semimetal(override val valences :Array<Byte>) :Atom {
        Silicon(arrayOf(4, -4)), Germanium(arrayOf(4, -4)),
        Arsen(arrayOf(-3, 5)), Antimon(arrayOf(-3, 5)),
        Tellur(arrayOf(-2, 6))
    }

    enum class Nonmetal(override val valences :Array<Byte>) :Atom {
        Hydrogen(arrayOf(1)),
        Fluorine(arrayOf(-1)), Chlorine(arrayOf(-1, 1, 3, 5, 7)), Bromine(arrayOf(-1, 1, 3, 5, 7)), Iodine(arrayOf(-1, 1, 3, 5, 7)),
        Oxygen(arrayOf(-2)), Sulfur(arrayOf(-2, 4, 6)), Selenium(arrayOf(-2, 4, 6)),
        Nitrogen(arrayOf(-3, 5)), Phosphor(arrayOf(-3, 5)),
        Carbon(arrayOf(4, 2, -4))
    }

    enum class Noblegas(override val valences :Array<Byte> =emptyArray()) :Atom {
        Helium, Neon, Argon, Crypton, Xenon, Radon
    }
}
