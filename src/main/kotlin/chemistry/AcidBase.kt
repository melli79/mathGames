package chemistry

fun hydrate(element :Atom) :List<Bond> = when (element) {
    is Metal, is Semimetal -> element.valences.filter { v -> v>0 } .map { v ->
        Bond3(element, Nonmetal.Oxygen, Nonmetal.Hydrogen,
            1u, v.toUByte(), v.toUByte())
    }

    Nonmetal.Fluorine, Nonmetal.Oxygen -> bind(Nonmetal.Hydrogen, element).toList()

    Nonmetal.Hydrogen -> bind(element, Nonmetal.Oxygen).toList()

    in setOf(Nonmetal.Chlorine, Nonmetal.Bromine, Nonmetal.Iodine) ->
        bind(Nonmetal.Hydrogen, element).toList() +
                element.valences.filter { v -> v>0 }.map { v ->
                    Bond3(Nonmetal.Hydrogen, element, Nonmetal.Oxygen,
                        1u, 1u, (v/2).toUByte())
                }

    in setOf(Nonmetal.Sulfur, Nonmetal.Selenium) ->
        bind(Nonmetal.Hydrogen, element).toList() +
                element.valences.filter { v -> v>0 }.map { v ->
                    Bond3(Nonmetal.Hydrogen, element, Nonmetal.Oxygen,
                        2u, 1u, (v/2 +1).toUByte())
                }

    Nonmetal.Nitrogen ->
        bind(element, Nonmetal.Hydrogen).toList() + listOf(
                    Bond3(Nonmetal.Hydrogen, element, Nonmetal.Oxygen,
                        1u, 1u, 2u),
                    Bond3(Nonmetal.Hydrogen, element, Nonmetal.Oxygen,
                        1u, 1u, 3u)
        )

    Nonmetal.Phosphorus -> listOf(
            Bond3(Nonmetal.Hydrogen, element, Nonmetal.Oxygen,
                3u, 1u, 2u),
            Bond3(Nonmetal.Hydrogen, element, Nonmetal.Oxygen,
                3u, 1u, 3u),
            Bond3(Nonmetal.Hydrogen, element, Nonmetal.Oxygen,
                3u, 1u, 4u)
        )

    Nonmetal.Carbon -> listOf(
        Bond3(Nonmetal.Hydrogen, Nonmetal.Carbon, Nonmetal.Oxygen,
            2u, 1u, 3u)
    )

    else -> emptyList()
}

fun abs(x :Byte) = when {
    x >= 0 -> x.toUByte()
    else -> (-x).toUByte()
}
