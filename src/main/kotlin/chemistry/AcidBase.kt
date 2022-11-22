package chemistry

fun acidBase(element :Atom) :List<Bond> = when {
    element is Atom.Metal || element is Atom.Semimetal -> element.valences.filter { v -> v>0 } .map { v ->
        Bond3(element, Atom.Nonmetal.Oxygen, Atom.Nonmetal.Hydrogen, 1u, v.toUByte(), v.toUByte())
    }
    element== Atom.Nonmetal.Fluorine -> bind(Atom.Nonmetal.Hydrogen, element).toList()
    element in setOf(Atom.Nonmetal.Chlorine, Atom.Nonmetal.Bromine, Atom.Nonmetal.Iodine) ->
        bind(element, Atom.Nonmetal.Hydrogen).toList() +
        element.valences.filter { v -> v>0 }.map { v ->
            Bond3(
                Atom.Nonmetal.Hydrogen, element, Atom.Nonmetal.Oxygen,
                1u, 1u, (v / 2).toUByte()
            )
        }
    element in setOf(Atom.Nonmetal.Sulfur, Atom.Nonmetal.Selenium) ->
        bind(element, Atom.Nonmetal.Hydrogen).toList() +
        element.valences.filter { v -> v>0 }.map { v ->
            Bond3(
                Atom.Nonmetal.Hydrogen, element, Atom.Nonmetal.Oxygen,
                2u, 1u, (v / 2 + 1).toUByte()
            )
        }
    element in setOf(Atom.Nonmetal.Nitrogen) ->
        bind(element, Atom.Nonmetal.Hydrogen).toList() +
        element.valences.filter { v -> v>0 }.map { v ->
            Bond3(
                Atom.Nonmetal.Hydrogen, element, Atom.Nonmetal.Oxygen,
                1u, 1u, (v / 2 + 1).toUByte()
            )
        }
    element== Atom.Nonmetal.Carbon -> listOf(
        Bond3(
            Atom.Nonmetal.Hydrogen, Atom.Nonmetal.Carbon, Atom.Nonmetal.Oxygen,
            2u, 1u, 3u
        )
    )
    else -> emptyList()
}