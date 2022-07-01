package stoxx

fun hfTrade(values :List<Int>) :List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    var min :Int? =null
    var max :Int? =null
    for (value in values) {
        if (min==null)
            min = value
        else if (max!=null&&value<max) {
            result.add(Pair(min, max))
            max = null
            min = value
        } else if (value<min)
            min = value
        else if (max==null||max<value)
            max = value
    }
    if (min!=null&&max!=null)
        result.add(Pair(min, max))
    return result
}
