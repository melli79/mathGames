package chemistry

fun acidBase(element :Atom) :List<Bond> = when (element) {
    is Atom.Metal, is Atom.Semimetal -> element.valences.filter { v -> v>0 } .map { v ->
        Bond3(element, Atom.Nonmetal.Oxygen, Atom.Nonmetal.Hydrogen,
            1u, v.toUByte(), v.toUByte())
    }

    Atom.Nonmetal.Fluorine -> bind(Atom.Nonmetal.Hydrogen, element).toList()
    in setOf(Atom.Nonmetal.Chlorine, Atom.Nonmetal.Bromine, Atom.Nonmetal.Iodine) ->
        bind(element, Atom.Nonmetal.Hydrogen).toList() +
                element.valences.filter { v -> v>0 }.map { v ->
                    Bond3(Atom.Nonmetal.Hydrogen, element, Atom.Nonmetal.Oxygen,
                        1u, 1u, (v/2).toUByte())
                }

    in setOf(Atom.Nonmetal.Sulfur, Atom.Nonmetal.Selenium) ->
        bind(element, Atom.Nonmetal.Hydrogen).toList() +
                element.valences.filter { v -> v>0 }.map { v ->
                    Bond3(Atom.Nonmetal.Hydrogen, element, Atom.Nonmetal.Oxygen,
                        2u, 1u, (v/2 +1).toUByte())
                }

    in setOf(Atom.Nonmetal.Nitrogen, Atom.Nonmetal.Phosphorus, Atom.Nonmetal.Arsen) ->
        bind(element, Atom.Nonmetal.Hydrogen).toList() +
                element.valences.filter { v -> v>0 }.map { v ->
                    Bond3(Atom.Nonmetal.Hydrogen, element, Atom.Nonmetal.Oxygen,
                        1u, 1u, (v/2 +1).toUByte())
                }

    Atom.Nonmetal.Carbon -> listOf(
        Bond3(Atom.Nonmetal.Hydrogen, Atom.Nonmetal.Carbon, Atom.Nonmetal.Oxygen,
            2u, 1u, 3u)
    )

    else -> emptyList()
}
