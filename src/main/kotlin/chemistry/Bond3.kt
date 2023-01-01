package chemistry

import java.util.*

open class Bond3(
        override val element1 :Atom, override val element2 :Atom, val element3 :Atom,
        override val amount1 :UByte, override val amount2 :UByte, val amount3 :UByte) :Bond {
    override fun toString() = when {
        amount2==amount3 && element2== Atom.Nonmetal.Oxygen &&element3== Atom.Nonmetal.Hydrogen ->
            if (element1 is Atom.Metal) "$element1${valence1()}hydroxide"
            else "$element1 hydroxide"
        element3== Atom.Nonmetal.Oxygen -> if (element1 is Atom.Metal)
                "$element1${valence1()}${oxideName(element2, amount3)}"
            else "${count(amount1)}$element1 ${oxideName(element2, amount3)}"
        else -> "${count(amount1)}$element1$element2${count(amount2)}$element3${count(amount3)}"
    } +" ($type)"

    private fun valence1() = if (element1.valences.size==1) " " else "-$amount2-"

    override val type :Bond.Type
        get() = when {
        element1==Atom.Nonmetal.Hydrogen -> Bond.Type.Acid
        element1 is Atom.Metal && element2==Atom.Nonmetal.Oxygen && element3==Atom.Nonmetal.Hydrogen
                -> Bond.Type.Base
        element1 is Atom.Semimetal && element2==Atom.Nonmetal.Oxygen && element3==Atom.Nonmetal.Hydrogen
                -> Bond.Type.Acid
        element1 is Atom.Metal || element2 is Atom.Metal -> Bond.Type.Polar
        else -> Bond.Type.Nonpolar
    }
}

internal fun oxideName(element :Atom, oxygenAmount :UByte) = when (element) {
    Atom.Nonmetal.Fluorine -> "fluorite"
    Atom.Nonmetal.Chlorine, Atom.Nonmetal.Bromine, Atom.Nonmetal.Iodine, Atom.Metal.Astatine,
    Atom.Metal.Chromium -> when (oxygenAmount.toInt()) {
        1 -> "hypo${stem(element)}ic"
        2 -> "${stem(element)}ic"
        3 -> "${stem(element)}ate"
        else -> "per${stem(element)}ate"
    }
    Atom.Metal.Mangan -> when (oxygenAmount.toInt()) {
        1 -> "hypomanganite"
        2 -> "manganite"
        3 -> "manganate"
        else -> "permanganate"
    }
    Atom.Nonmetal.Sulfur -> if (oxygenAmount<=3u) "sulfite" else "sulfate"
    Atom.Nonmetal.Selenium -> if (oxygenAmount<=3u) "selenite" else "selenate"
    Atom.Nonmetal.Nitrogen -> if (oxygenAmount<=2u) "nitrite" else "nitrate"
    Atom.Nonmetal.Phosphorus -> if (oxygenAmount<=3u) "phosphite" else "phosphate"
    Atom.Nonmetal.Carbon -> "carbonate"
    Atom.Semimetal.Silicon -> "silicate"
    Atom.Semimetal.Germanium -> "germanate"
    Atom.Metal.Plumbum -> if (oxygenAmount<=1u) "plumbite" else "plumbate"
    Atom.Semimetal.Boron -> "borate"
    Atom.Metal.Aluminium -> "aluminate"
    Atom.Metal.Ferrum -> if (oxygenAmount<=2u) "ferrite"  else "ferrate"
    Atom.Metal.Copper -> "cuprate"
    Atom.Metal.Argentum -> "argentate"
    Atom.Metal.Mercury -> "amalgam"
    else -> "${element}ate"
}

private fun stem(element :Atom) :String {
    val name = element.toString().lowercase(Locale.ENGLISH)
    return name.substring(0 until name.length-3)
}
