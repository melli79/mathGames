package scheduling

/**
 * Given a set of Intervals, some of which may overlap.
 * 1. The Schedule width d is the maximum number of intervals that overlap on more than a point.
 * Find an algorithm that Schedules all intervals with d parallel processes.
 */

fun <F :Comparable<F>> scheduleParallel(requests :Collection<Interval<F>>) :Set<List<Interval<F>>> {
    val opens = requests.sortedBy { it.beg }.toMutableList()
    val result = mutableSetOf<MutableList<Interval<F>>>()
    outer@while (opens.isNotEmpty()) {
        val i = opens.first();  opens.remove(i)
        for (q in result)
            if (q.isEmpty() || !i.overlaps(q.last())) {
                q.add(i)
                continue@outer
            }
        result.add(mutableListOf(i))
    }
    return result
}

