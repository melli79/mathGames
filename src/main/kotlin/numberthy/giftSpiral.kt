package numberthy

import partitions.sqr
import kotlin.math.abs
import kotlin.math.max

enum class Color {
    Peach, Yellow, Lilac, Cyan, LimeGreen;
}

fun computeColor(pos :Pair<Int, Int>) :Color {
    val c = max(abs(pos.first), abs(pos.second)).toUInt()
    if (c==0u)
        return Color.Peach
    val n = sqr(2u*c-1u)-1uL
    val index = when {
        pos.second>-(c.toInt()) && pos.first==c.toInt() -> (c.toInt()+pos.second).toUInt()
        pos.second==c.toInt() -> (3*c.toInt()-pos.first).toUInt()
        pos.first==-(c.toInt()) -> (5*c.toInt()-pos.second).toUInt()
        pos.second==-(c.toInt()) -> (7*c.toInt()+pos.first).toUInt()
        else -> error("unexpected case.")
    }
    return Color.entries[((n+index.toULong())%Color.entries.size.toULong()).toInt()]
}

fun max(a :UShort, b :UShort) = when {
    a<b -> a
    else -> b
}

fun abs(a :Short) = when {
    a >= 0 -> a.toUShort()
    else -> (-a).toUShort()
}

fun main() {
    println("Corner 0,0 has color: "+ computeColor(Pair(0,0)))
    for (c in 1..3) {
        println("Corner $c,-${c-1} has color: "+ computeColor(Pair(c,1-c)))
        println("Corner $c,$c has color: "+ computeColor(Pair(c,c)))
        println("Corner -$c,$c has color: "+ computeColor(Pair(-c,c)))
        println("Corner -$c,-$c has color: "+ computeColor(Pair(-c,-c)))
        println("Corner $c,-$c has color: "+ computeColor(Pair(c,-c)))
    }
    for (pos in listOf(Pair(-49,50), Pair(-39,49)))
        println("Gift $pos has color ${computeColor(pos)}.")
}
