package chemistry

fun acidBase(element :Atom) :List<Bond> = when (element) {
    is Metal, is Semimetal -> element.valences.filter { v -> v>0 } .map { v ->
        Bond3(element, Nonmetal.Oxygen, Nonmetal.Hydrogen,
            1u, v.toUByte(), v.toUByte())
    }

    Nonmetal.Fluorine -> bind(Nonmetal.Hydrogen, element).toList()
    in setOf(Nonmetal.Chlorine, Nonmetal.Bromine, Nonmetal.Iodine) ->
        bind(element, Nonmetal.Hydrogen).toList() +
                element.valences.filter { v -> v>0 }.map { v ->
                    Bond3(Nonmetal.Hydrogen, element, Nonmetal.Oxygen,
                        1u, 1u, (v/2).toUByte())
                }

    in setOf(Nonmetal.Sulfur, Nonmetal.Selenium) ->
        bind(element, Nonmetal.Hydrogen).toList() +
                element.valences.filter { v -> v>0 }.map { v ->
                    Bond3(Nonmetal.Hydrogen, element, Nonmetal.Oxygen,
                        2u, 1u, (v/2 +1).toUByte())
                }

    in setOf(Nonmetal.Nitrogen, Nonmetal.Phosphorus, Semimetal.Arsen) ->
        bind(element, Nonmetal.Hydrogen).toList() +
                element.valences.filter { v -> v>0 }.map { v ->
                    Bond3(Nonmetal.Hydrogen, element, Nonmetal.Oxygen,
                        1u, 1u, (v/2 +1).toUByte())
                }

    Nonmetal.Carbon -> listOf(
        Bond3(Nonmetal.Hydrogen, Nonmetal.Carbon, Nonmetal.Oxygen,
            2u, 1u, 3u)
    )

    else -> emptyList()
}
