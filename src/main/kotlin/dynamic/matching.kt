package dynamic

/**
 * Finding a stable Matching
 */

interface Binary<M :Binary<M, F>, F :Binary<F, M>> {
    fun compare(b :F, b2 :F) :Int
}

fun <M :Binary<M, F>, F :Binary<F, M>> match(men :Set<M>, women :Set<F>) :Triple<Set<Pair<M, F>>, Set<M>, Set<F>> {
    val freeM = men.toMutableSet()
    val freeF = women.toMutableSet()
    val result = mutableSetOf<Pair<M, F>>()
    val failedM = mutableSetOf<M>()
    outer@while (freeM.isNotEmpty()) {
        val m = freeM.first();  freeM.remove(m)
        for (f in women.sortedWith(m::compare).reversed()) {
            if (f in freeF) {
                freeF.remove(f)
                result.add(Pair(m, f))
                continue@outer
            } else {
                val oldP = result.first { p -> p.second==f }
                val m2 = oldP.first
                if (f.compare(m, m2)>0) {
                    result.remove(oldP)
                    result.add(Pair(m, f))
                    freeM.add(m2)
                    continue@outer
                }
            }
        }
        failedM.add(m)
    }
    return Triple(result, failedM, freeF)
}
