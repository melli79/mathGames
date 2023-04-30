package dynamic

import common.Quadruple

fun computeKnapsack(items :List<Pair<UInt, UInt>>, size :UInt) :Set<Int> {
    val result = mutableSetOf<Int>()
    var value = 0u
    val input = items.mapIndexed { i, p -> Triple(p.first, p.second, i) }
    input.filter { t -> t.second==0u }.forEach {t ->
        result.add(t.third)
        value += t.first
    }
    val options = input.filter { t -> t.second!=0u }
        .map { t -> Quadruple(t.first.toDouble()/t.second.toDouble(), t.first, t.second, t.third) }
        .toMutableList()
    return result +bestChoices(size, options).second
}

private fun bestChoices(size :UInt, options :List<Quadruple<Double, UInt, UInt, Int>>) :Pair<UInt, Set<Int>> {
    val bestOptions = options.filter { q -> q.third <= size }
        .sortedWith(Comparator
            .comparing<Quadruple<Double, UInt, UInt, Int>, Double> { -it.first }
            .thenComparing<Int> { -it.second.toInt() })
    if (bestOptions.isNotEmpty()) {
        val choice = bestOptions.first()
        val remainingOptions = bestOptions -setOf(choice)
        val alternative = bestChoices(size, remainingOptions)
        val rest = bestChoices(size -choice.third, remainingOptions)
        val value = choice.second + rest.first
        if (value >= alternative.first) {
            return Pair(value, rest.second + setOf(choice.fourth))
        }
        return alternative
    }
    return Pair(0u, emptySet())
}
