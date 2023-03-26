package diophantine

import einstein.next

fun findHopping(moves :Set<UInt>, avoids :Set<UInt>) :Pair<Set<List<UInt>>, ULong> {
    if (moves.isEmpty())
        return Pair(setOf(emptyList()), 1uL)
    assert(moves.size == avoids.size+1)
    assert( moves.sum() !in avoids)
    val remaining = moves.toMutableSet()
    val result = mutableSetOf<List<UInt>>()
    var count = 0uL
    val candidate = mutableListOf<UInt>()
    var pos = 0u
    do {
        while (remaining.isNotEmpty()) {
            val first = remaining.min()
            remaining.remove(first)
            candidate.add(first)
            pos += first
            if (pos in avoids)
                break
        }
        if (remaining.isEmpty()) {
            count++
            if (result.size<10)
                result.add(candidate.copy())
        }
        outer@ while (candidate.isNotEmpty()) {
            var next = candidate.removeLast()
            pos -= next
            while (true) {
                next = remaining.next(next) ?: break
                if (pos + next !in avoids) {
                    candidate.add(next)
                    pos += next
                    break@outer
                }
            }
        }

    } while (candidate.isNotEmpty())
    return Pair(result, count)
}

fun <E> List<E>.copy() = toMutableList().toList()
