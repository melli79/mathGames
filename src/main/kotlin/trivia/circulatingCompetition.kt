package trivia

fun compete(input :List<Short>, t :UInt) :List<Int> {
    if (input.isEmpty())
        return emptyList()
    if (input.size<2)
        return listOf(t.toInt())
    val result = (1..input.size).map { 0 }.toMutableList()
    val tournament = input.mapIndexed { i, v -> Pair(v, i) }.toMutableList()
    for (k in 1u..t) {
        val one = tournament.removeAt(0)
        val other = tournament.removeAt(0)
        val (first, second) = if (one.first < other.first)
            Pair(one, other)
        else
            Pair(other, one)
        result[first.second]++;  result[second.second]--
        tournament.add(tournament.size, second)
        tournament.add(tournament.size/2, first)
    }
    return result
}
