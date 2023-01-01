package trivia

fun computeBeauty(aa :Collection<Int>, bs :Collection<Int>) = aa.max()-aa.min() +bs.max()-bs.min()

fun minimizeBeauty(elements :Collection<Int>) :Pair<Collection<Int>, Collection<Int>> {
    val remains = elements.sorted().toMutableList()
    var lower = remains.removeFirst()
    val firsts = mutableListOf(lower)
    val middles = mutableListOf<Int>()
    var d = 0
    while (remains.isNotEmpty()) {
        val upper = remains.removeFirst()
        val d1 = upper - lower
        if (d1>d) {
            firsts.addAll(middles)
            middles.clear()
            d = d1
        }
        middles.add(upper)
        lower = upper
    }
    return Pair(firsts, middles+remains)
}
