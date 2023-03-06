package chemistry

import java.util.*

open class Bond3(
        override val element1 :Atom, override val element2 :Atom, val element3 :Atom,
        override val amount1 :UByte, override val amount2 :UByte, val amount3 :UByte) :Bond {
    override fun toString() = if (amount2==amount3 && amount2>1u)
            """$element1${amount(amount1)}($element2$element3)${amount(amount2)}"""
        else
            """$element1${amount(amount1)}$element2${amount(amount2)}$element3${amount(amount3)}"""

    override fun getIupacName() = when {
        amount2==amount3 && element2== Nonmetal.Oxygen &&element3== Nonmetal.Hydrogen ->
            if (element1 is Metal) "$element1${valence1()}hydroxide"
            else "$element1 hydroxide"
        element3== Nonmetal.Oxygen -> if (element1 is Metal)
                "$element1${valence1()}${oxideName(element2, amount3)}"
            else "${count(amount1)}$element1 ${oxideName(element2, amount3)}"
        else -> "${count(amount1)}$element1$element2${count(amount2)}$element3${count(amount3)}"
    } +" ($type)"

    private fun valence1() = if (element1.valences.size==1) " " else "-$amount2-"
}

internal fun oxideName(element :Atom, oxygenAmount :UByte) = when (element) {
    Nonmetal.Fluorine -> "fluorite"
    Nonmetal.Chlorine, Nonmetal.Bromine, Nonmetal.Iodine, Semimetal.Astatine,
    Metal.Chromium -> when (oxygenAmount.toInt()) {
        1 -> "hypo${stem(element)}ic"
        2 -> "${stem(element)}ic"
        3 -> "${stem(element)}ate"
        else -> "per${stem(element)}ate"
    }
    Metal.Mangan -> when (oxygenAmount.toInt()) {
        1 -> "hypomanganite"
        2 -> "manganite"
        3 -> "manganate"
        else -> "permanganate"
    }
    Nonmetal.Sulfur -> if (oxygenAmount<=3u) "sulfite" else "sulfate"
    Nonmetal.Selenium -> if (oxygenAmount<=3u) "selenite" else "selenate"
    Nonmetal.Nitrogen -> if (oxygenAmount<=2u) "nitrite" else "nitrate"
    Nonmetal.Phosphorus -> if (oxygenAmount<=3u) "phosphite" else "phosphate"
    Nonmetal.Carbon -> "carbonate"
    Semimetal.Silicon -> "silicate"
    Semimetal.Germanium -> "germanate"
    Metal.Plumbum -> if (oxygenAmount<=1u) "plumbite" else "plumbate"
    Semimetal.Boron -> "borate"
    Metal.Aluminium -> "aluminate"
    Metal.Ferrum -> if (oxygenAmount<=2u) "ferrite"  else "ferrate"
    Metal.Copper -> "cuprate"
    Metal.Argentum -> "argentate"
    Metal.Mercury -> "amalgam"
    else -> "${element}ate"
}

private fun stem(element :Atom) :String {
    val name = element.toString().lowercase(Locale.ENGLISH)
    return name.substring(0 until name.length-3)
}
