package chemistry

import common.math.lcm
import kotlin.math.abs

sealed interface Bond {
    val element1 :Atom
    val element2 :Atom
    val amount1 :UByte
    val amount2 :UByte

    val type :Type
        get() = when {
            element1==Nonmetal.Hydrogen -> Type.Acid
            element1 is Metal && element2==Nonmetal.Oxygen && this is Bond3 && element3==Nonmetal.Hydrogen
                -> Type.Base
            element1 is Semimetal && element2==Nonmetal.Oxygen && this is Bond3 && element3==Nonmetal.Hydrogen
                -> Type.Acid
            element1==Nonmetal.Nitrogen && element2==Nonmetal.Hydrogen -> Type.Base

            element1 is Metal && element2 in oxidizers -> Type.Ionic
            element1 in setOf(Nonmetal.Carbon, Nonmetal.Phosphorus, Nonmetal.Selenium) &&
                    element2 in setOf(Nonmetal.Fluorine, Nonmetal.Oxygen, Nonmetal.Chlorine) -> Type.Polar
            element1 is Metal || element2 is Metal || element1==Nonmetal.Phosphorus -> Type.Polar
            else -> Type.Nonpolar
        }

    enum class Type {
        Ionic, Acid, Base, Polar, Nonpolar
    }

    fun getIupacName() :String
}

open class Bond2(override val element1 :Atom, override val element2 :Atom,
                 override val amount1 :UByte, override val amount2 :UByte) :Bond {

    override fun toString() = """$element1${amount(amount1)}$element2${amount(amount2)}"""

    override fun getIupacName() :String {
        val part1 = if (element1 is Metal) {
            if (element1.valences.size<2)
                "$element1"
            else {
                val valence1 = abs(element2.valences[0].toInt()) *amount2.toInt() /amount1.toInt()
                "$element1-$valence1-"
            }
        } else if (element2==Nonmetal.Hydrogen || element1==Nonmetal.Hydrogen)
            "$element1"
        else
            "${count(amount1)}$element1${count(amount2)}"
        return "$part1${nameAnion(element2)}"
    }

    private fun nameAnion(element :Atom) = when (element) {
        Nonmetal.Fluorine -> "fluoride"
        Nonmetal.Oxygen -> "oxide"
        Nonmetal.Chlorine -> "chloride"
        Nonmetal.Sulfur -> "sulfide"
        Nonmetal.Selenium -> "selenide"
        Nonmetal.Nitrogen -> "nitride"
        Nonmetal.Phosphorus -> "phosphoride"
        Nonmetal.Bromine -> "bromide"
        Nonmetal.Carbon -> "carbide"
        Nonmetal.Iodine -> "iodide"
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

val oxidizers = setOf(Nonmetal.Fluorine, Nonmetal.Oxygen, Nonmetal.Chlorine, Nonmetal.Nitrogen, Nonmetal.Bromine, Nonmetal.Iodine)

fun bind(element1 :Atom, element2 :Atom) :Set<Bond2> {
    val result = mutableSetOf<Bond2>()
    if (element2==Nonmetal.Oxygen) when (element1) {
        Nonmetal.Hydrogen -> result.add(Bond2(element1, element2, 2u, 2u))
        Nonmetal.Nitrogen -> {
            result.add(Bond2(element1, element2, 2u, 1u))
            result.add(Bond2(element1, element2, 1u, 1u))
            result.add(Bond2(element1, element2, 1u, 2u))
        }
        Metal.Ferrum -> result.add(Bond2(element1, element2, 3u, 4u))
        else -> {/* ignore */}
    }
    for (valence1 in element1.valences) {
        for (valence2 in element2.valences) {
            if (valence1*valence2<0) {
                val m = lcm(abs(valence1.toLong()).toULong(), abs(valence2.toLong()).toULong()).toInt()
                result.add(Bond2(element1, element2,
                    (m/abs(valence1.toInt())).toUByte(), (m/abs(valence2.toInt())).toUByte())
                )
            }
        }
    }
    return result
}

internal fun amount(n :UByte) = when (n.toInt()) {
    1 -> ""
    else -> "_$n "
}
