package stoxx

fun findMaxProfit(values :List<Int>) :Pair<Int, Int>? {
    var min :Int? =null
    var max :Int? = null
    var candidate :Pair<Int, Int>? =null
    for (v in values) {
        if (min==null||v<min) {
            min = v
            max = null
        } else if (max==null||v>max) {
            max = v
            if (candidate==null || candidate.second-candidate.first < max-min)
                candidate = Pair(min, max)
        }
    }
    if (candidate!=null) {
        if (min==null||max==null|| candidate.second-candidate.first> max-min)
            return candidate
    }
    if (min!=null&&max!=null)
        return Pair(min, max)
    return null
}
