package trivia

import kotlin.math.pow

fun repeatedDices(s :UByte, n :UByte) :Map<UShort, Double> {
    val f :Double = s.toDouble().pow(-n.toInt())
    return repeatDice(s, n).entries.map { e -> Pair(e.key, f*e.value.toDouble()) }.toMap()
}

private fun repeatDice(s :UByte, n:UByte) :Map<UShort, UInt> = when (n.toInt()) {
    0 -> emptyMap()
    1 -> (1u..s.toUInt()).map { Pair(it.toUShort(), 1u) }.toMap()
    else -> {
        val n2 = n/2u
        val c2s = repeatDice(s, (n-n2).toUByte()).entries
        repeatDice(s, n2.toUByte()).entries.flatMap { e1 -> c2s.map { e2 ->
            Pair(e1.key+e2.key, e1.value*e2.value)
        } }.groupBy { e -> e.first }
            .map { e -> Pair(e.key.toUShort(), e.value.sumOf { p -> p.second }) }
            .toMap()
    }
}

fun multiDices(ds :Collection<Pair<UByte, UByte>>) :Map<UShort, Double> {
    var result = mapOf(Pair(0.toUShort(), 1.0))
    for (e in ds) {
        result = repeatedDices(e.first, e.second).flatMap { e1 -> result.entries.map { e2 ->
            Pair(e1.key+e2.key, e1.value*e2.value)
        } }.groupBy { p -> p.first }
            .map { e1 -> Pair(e1.key.toUShort(), e1.value.sumOf { it.second }) }
            .toMap()
    }
    return result
}
