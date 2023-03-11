package search

import kotlin.math.max

@OptIn(ExperimentalUnsignedTypes::class)
fun findHeaviestPathFullSearch(weights :List<UIntArray>) :Pair<UInt, Set<UIntArray>> {
    var candidates = listOf(Triple(0u,0u, setOf(emptyList<UInt>())))
    for (row in weights) {
        if (row.isEmpty())
            break
        val newCandidates = candidates.produceCandidates(row)
        candidates = newCandidates.groupBy { Pair(it.first, it.second) }
            .map { (key, values) -> Triple(key.first, key.second, values.flatMap { it.third }.toSet()) }
    }
    return getBest(candidates)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun findHeaviestPathPruned(weights :List<UIntArray>) :Pair<UInt, Set<UIntArray>> {
    val limits = mutableListOf<UInt>()
    var lastLimit = 0u
    for (row in weights.reversed()) {
        limits.add(0, lastLimit)
        if (row.isEmpty())
            lastLimit = 0u
        else
            lastLimit = row.diam()
    }
    var candidates = listOf(Triple(0u,0u, setOf(emptyList<UInt>())))
    var n = 1
    for (row in weights) {
        if (row.isEmpty())
            break
        val newCandidates = candidates.produceCandidates(row)
        println("${n++}: ${newCandidates.size}, ${newCandidates.sumOf { it.third.size }}")
        val limit = newCandidates.maxOf { it.first }.toInt() -limits.removeAt(0).toInt()
        candidates = newCandidates
            .filter { it.first.toInt()>=limit }
            .groupBy { Pair(it.first, it.second) }
            .map { (key, values) -> Triple(key.first, key.second, values.flatMap { it.third }.toSet()) }
    }
    return getBest(candidates)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun findHeaviestPathReversed(weights :List<UIntArray>) :Pair<UInt, Set<UIntArray>> {
    if (weights.isEmpty())
        return Pair(0u, setOf(uintArrayOf()))
    val limits = mutableListOf<UInt>()
    var lastLimit = 0u
    for (row in weights) {
        limits.add(0, lastLimit)
        if (row.isEmpty())
            lastLimit = 0u
        else
            lastLimit = row.diam()
    }
    val width = weights.last().size.toUInt()
    var candidates = (1u until max(width, 2u)).map { p ->
        Triple(0u,p, setOf(emptyList<UInt>()))
    }
    var n = 1
    for (row in weights.reversed()) {
        if (row.isEmpty())
            break
        val newCandidates = candidates.produceCandidates(row)
        println("${n++}: ${newCandidates.size}, ${newCandidates.sumOf { it.third.size }}")
        val limit = newCandidates.maxOf { it.first }.toInt() -limits.removeAt(0).toInt()
        candidates = newCandidates
            .filter { it.first.toInt()>=limit }
            .groupBy { Pair(it.first, it.second) }
            .map { (key, values) -> Triple(key.first, key.second, values.flatMap { it.third }.take(2).toSet()) }
    }
    return getBest(candidates)
}

@OptIn(ExperimentalUnsignedTypes::class)
private fun getBest(candidates :List<Triple<UInt, UInt, Set<List<UInt>>>>) :Pair<UInt, Set<UIntArray>> {
    val max = candidates.maxOf { it.first }
    val result = candidates.filter { it.first == max }.flatMap { it.third.map { s -> s.toUIntArray() } }
    return Pair(max, result.toSet())
}

@OptIn(ExperimentalUnsignedTypes::class)
fun UIntArray.diam() = max() - min()

@OptIn(ExperimentalUnsignedTypes::class)
private fun List<Triple<UInt, UInt, Set<List<UInt>>>>.produceCandidates(row :UIntArray) =
    flatMap { t :Triple<UInt, UInt, Set<List<UInt>>> ->
        val index = t.second.toInt()
        if (t.second > 0u) {
            if (index < row.size - 1)
                listOf(
                    Triple(t.first + row[index - 1], t.second - 1u, t.third.map { it + listOf(t.second - 1u) }.toSet()),
                    Triple(t.first + row[index], t.second, t.third.map { it + listOf(t.second) }.toSet()),
                    Triple(t.first + row[index + 1], t.second + 1u, t.third.map { it + listOf(t.second + 1u) }.toSet())
                )
            else if (index < row.size)
                listOf(Triple(
                    t.first + row[index - 1],
                    t.second - 1u,
                    t.third.map { it + listOf(t.second - 1u) }.toSet()
                ),
                    Triple(t.first + row[index], t.second, t.third.map { it + listOf(t.second) }.toSet())
                )
            else if (index - 1 < row.size)
                listOf(
                    Triple(
                        t.first + row[index - 1],
                        t.second - 1u,
                        t.third.map { it + listOf(t.second - 1u) }.toSet()
                    )
                )
            else
                emptyList()
        } else {
            if (index < row.size - 1)
                listOf(Triple(t.first + row[index], t.second, t.third.map { it + listOf(t.second) }.toSet()),
                    Triple(t.first + row[index + 1], t.second + 1u, t.third.map { it + listOf(t.second + 1u) }.toSet())
                )
            else if (index < row.size)
                listOf(Triple(t.first + row[index], t.second, t.third.map { it + listOf(t.second) }.toSet()))
            else
                emptyList()
        }
    }
