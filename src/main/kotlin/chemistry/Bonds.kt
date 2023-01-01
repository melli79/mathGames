package chemistry

import common.math.lcm
import kotlin.math.abs

sealed interface Bond {
    val element1 :Atom
    val element2 :Atom
    val amount1 :UByte
    val amount2 :UByte
    val type :Type

    enum class Type {
        Ionic, Acid, Base, Polar, Nonpolar
    }
}

open class Bond2(override val element1 :Atom, override val element2 :Atom, override val amount1 :UByte, override val amount2 :UByte,
        override val type :Bond.Type) :Bond {

    override fun toString() :String {
        val part1 = if (element1 is Atom.Metal) {
            if (element1.valences.size<2)
                "$element1"
            else {
                val valence1 = abs(element2.valences[0].toInt()) *amount2.toInt() /amount1.toInt()
                "$element1-$valence1-"
            }
        } else if (element2==Atom.Nonmetal.Hydrogen || element1==Atom.Nonmetal.Hydrogen)
            "$element1"
        else
            "${count(amount1)}$element1${count(amount2)}"
        return "$part1${nameAnion(element2)}"
    }

    private fun nameAnion(element :Atom) = when (element) {
        Atom.Nonmetal.Fluorine -> "fluoride"
        Atom.Nonmetal.Oxygen -> "oxide"
        Atom.Nonmetal.Chlorine -> "chloride"
        Atom.Nonmetal.Sulfur -> "sulfide"
        Atom.Nonmetal.Selenium -> "selenide"
        Atom.Nonmetal.Nitrogen -> "nitride"
        Atom.Nonmetal.Phosphorus -> "phosphoride"
        Atom.Nonmetal.Bromine -> "bromide"
        Atom.Nonmetal.Carbon -> "carbide"
        Atom.Nonmetal.Iodine -> "iodide"
        else -> element.toString()
    }
}

internal fun count(amount :UByte) = when (amount.toUInt()) {
    0u -> "no"
    1u -> "mono"
    2u -> "di"
    3u -> "tri"
    4u -> "tretr"
    5u -> "pent"
    6u -> "hex"
    7u -> "hept"
    else -> "poly"
}

val oxidizers = setOf(Atom.Nonmetal.Oxygen, Atom.Nonmetal.Fluorine, Atom.Nonmetal.Chlorine, Atom.Nonmetal.Bromine)

fun bind(element1 :Atom, element2 :Atom) :Set<Bond2> {
    val result = mutableSetOf<Bond2>()
    if (element2==Atom.Nonmetal.Oxygen) when (element1) {
        Atom.Nonmetal.Hydrogen -> result.add(Bond2(element1, element2, 1u, 1u, Bond.Type.Polar))
        Atom.Nonmetal.Nitrogen -> {
            result.add(Bond2(element1, element2, 2u, 1u, Bond.Type.Polar))
            result.add(Bond2(element1, element2, 1u, 1u, Bond.Type.Polar))
            result.add(Bond2(element1, element2, 1u, 2u, Bond.Type.Polar))
        }
        else -> {}
    }
    for (valence1 in element1.valences) {
        for (valence2 in element2.valences) {
            if (valence1*valence2<0) {
                val type = when {
                    element1 is Atom.Metal || element2 is Atom.Metal -> Bond.Type.Ionic
                    element1==element2 || element1 in oxidizers && element2 in oxidizers -> Bond.Type.Nonpolar
                    element1==Atom.Nonmetal.Nitrogen && element2==Atom.Nonmetal.Hydrogen -> Bond.Type.Base
                    (element1 is Atom.Nonmetal && element2==Atom.Nonmetal.Hydrogen) ||
                            (element2 is Atom.Nonmetal && element1==Atom.Nonmetal.Hydrogen) -> Bond.Type.Acid
                    else -> Bond.Type.Polar
                }
                val m = lcm(abs(valence1.toLong()).toULong(), abs(valence2.toLong()).toULong()).toInt()
                result.add(Bond2(element1, element2,
                    (m/abs(valence1.toInt())).toUByte(), (m/abs(valence2.toInt())).toUByte(),
                    type))
            }
        }
    }
    return result
}
