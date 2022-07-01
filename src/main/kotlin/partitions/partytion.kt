package partitions

fun partytion(input :Iterable<Int>, sum :Int) :Set<Pair<Int, Int>> {
    val result = mutableSetOf<Pair<Int, Int>>()
    val options = input.toSet()
    for (s in options.sorted()) {
        val r = sum - s
        if (r <s) break
        if (r in options) {
            result.add(Pair(s, r))
        }
    }
    return result
}
