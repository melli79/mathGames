package partitions

fun optimizeNumberOfCards(k :UByte) :Pair<ULong, ULong> {
    val N = k.toULong() +sqr(k.toInt()-1)
    return Pair(N, N)
}

fun sqr(x :Int) = abs(x).toULong()*abs(x).toULong()

fun abs(x :Int) = when {
    x >=0 -> x.toUInt()
    else -> (-x).toUInt()
}

fun estimateNumberOfPairs(k :UByte) = designCards(k).size

fun designCards(k :UByte) :Set<Set<UInt>> {
    val result = mutableSetOf<Set<UInt>>()
    val N = k+sqr(k.toInt() -1).toUInt()
    val firstOption = (0u until k.toUInt()).toList()
    result.add(firstOption.toSet())
    val options = mutableListOf<List<UInt>>()
    (k.toUInt() until N).groupBy { o -> (o-1u)/(k.toUInt() -1u) }.forEach { choice ->
        if (choice.value.size == k.toInt() -1) {
            val option = listOf(firstOption.first()) + choice.value
            options.add(option)
            result.add(option.toSet())
        }
    }
    for(choice in firstOption.slice(1 until k.toInt())) {
        for (i in 1.toUByte() until k) {
            val card = (listOf(choice) + options.map { option ->
                option[permutation(k, choice.toUByte(), i.toUByte()).toInt()]
            }).toSet()
            result.add(card)
        }
    }
    return result
}

private fun permutation(n :UByte, i :UByte, j :UByte) :UByte {
    return ((i*j)%n).toUByte()
}
