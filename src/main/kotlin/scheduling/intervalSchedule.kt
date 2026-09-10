package scheduling

/**
 * Scheduling the highest number of intervals from a set
 */

data class Interval<F :Comparable<F>>(val beg :F, val end :F) {
    init {
        require(beg<=end) { "begin must be before end" }
    }

    override fun toString() = "[$beg, $end]"
}

fun <F :Comparable<F>> Interval<F>.overlaps(other :Interval<F>) :Boolean
= if (beg<=other.beg)
    return other.beg<end
  else
    return beg<other.end

fun <F :Comparable<F>> schedule(requests :Collection<Interval<F>>) :Set<Interval<F>> {
    var opens = requests.sortedBy { it.end }.toMutableList()
    val result = mutableSetOf<Interval<F>>()
    while (opens.isNotEmpty()) {
        val i = opens.first();  opens.remove(i)
        result.add(i)
        opens = opens.dropWhile { i.overlaps(it) }.toMutableList()
    }
    return result
}
