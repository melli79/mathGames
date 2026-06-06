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

fun estimateNumberOfPairs(k :UByte) = (k+sqr(k.toInt() -1)).toInt()

fun designCards(k :UByte) :Set<Set<UInt>> {
    val result = mutableSetOf<Set<UInt>>()
    val N = k+sqr(k.toInt() -1).toUInt()
    val firstOption = (0u until k.toUInt()).toList()
    result.add(firstOption.toSet())
    val options = mutableListOf<List<UInt>>()
    (k.toUInt() until N).groupBy { o -> (o-1u)/(k.toUInt() -1u) }.forEach { (_, choice) ->
        if (choice.size == k.toInt() -1) {
            val option = listOf(firstOption.first()) + choice
            options.add(option)
            result.add(option.toSet())
        }
    }
    if (k<=2u) return result
    if (k%2u==0u || k>3u&&k%3u==0u || k>5u&&k%5u==0u || k>7u&&k%7u==0u || k>11u&&k%11u==0u || k>13u&&k%13u==0u || k>17u&&k%17u==0u)
        println("$k is not prime!  This will fail.")
    for(choice in firstOption.drop(1)) {
        for (i in 1.toUByte() until k) {
            val card = (listOf(choice) + options.map { option ->
                option[permutation(k, choice.toUByte(), i.toUByte()).toInt()]
            }).toSet()
            if (card.size == k.toInt()) result.add(card)
        }
    }
    return result
}

private fun permutation(p :UByte, i :UByte, j :UByte) :UByte {
    return ((i*j)%p).toUByte()
}
