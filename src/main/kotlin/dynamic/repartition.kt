package dynamic

fun repartition(N :UInt, k :UInt, numRounds :UInt) :List<List<Set<UInt>>> {
    if (numRounds<1u)
        return emptyList()
    if (N<=k)
        return listOf(listOf((0u until N).toSet()))
    assert(k>1u)
    val n = k*((N+k-1u)/k)
    if (numRounds>(n+k-3u)/(k-1u))
        println("From $N ($n) players I will not be able to produce $numRounds disjoint rounds, at most ${(n+k-3u)/(k-1u)}.")
    val players = 0u until n
    val round1 = players.groupBy { it/k }
        .map { (_, vs) -> vs.toSet() }
    val result = mutableListOf(round1)

    for (r in 2u..numRounds) {
        val round = fillRound(if (r <= 5u)
                players.toMutableList()
            else
                players.shuffled().toMutableList(),
            k, result)
        if (round.isNotEmpty())
            result.add(round)
        else {
            println("Combinatorics exhausted")
            break
        }
    }
    return result
}

private fun fillRound(
    remainings :MutableList<UInt>,
    k :UInt,
    earlierRounds :List<List<Set<UInt>>>
) :List<Set<UInt>> {
    val round = mutableListOf<Set<UInt>>()
    outer@while (remainings.isNotEmpty()) {
        val group = mutableSetOf(remainings.removeAt(0))
        if (!fillGroup(group, k, remainings, earlierRounds))
            break@outer
        round.add(group)
    }
    return round
}

private fun fillGroup(
    group :MutableSet<UInt>,
    k :UInt,
    remainings :MutableList<UInt>,
    earlierRounds :List<List<Set<UInt>>>
) :Boolean {
    outer@while (group.size < k.toInt()) {
        for (p in remainings) {
            if (hasOverlap(group, earlierRounds, p))
                continue
            remainings.remove(p)
            group.add(p)
            continue@outer
        }
        print("."); System.out.flush()
        return false
    }
    return true
}

private fun hasOverlap(
    group :Set<UInt>,
    earlierRounds :List<List<Set<UInt>>>,
    p :UInt
) :Boolean {
    for (competitor in group) {
        for (round in earlierRounds) for (cg in round)
            if (competitor in cg && p in cg)
                return true
    }
    return false
}
