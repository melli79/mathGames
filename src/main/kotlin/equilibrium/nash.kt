package equilibrium

fun nash2(pref1 :List<Int>, pref2 :List<Int>) :Pair<List<Int>, List<Int>> {
    val (shortList1, options1) = trimList(pref1)
    val (shortList2, options2) = trimList(pref2)
    shortList1.addAll(options2 -options1)
    shortList2.addAll(options1 -options2)
    assert(shortList1.size==shortList2.size)
    val result1 = mutableListOf<Int>()
    val result2 = mutableListOf<Int>()
    while (shortList1.isNotEmpty()||shortList2.isNotEmpty()) {
        if (shortList1.isNotEmpty()&&shortList2.size>=2) {
            result2.add(0, shortList1.removeAt(shortList1.size-1))
            shortList2.remove(result2.first())
        }
        if (shortList2.isNotEmpty()) {
            result1.add(0, shortList2.removeAt(shortList2.size-1))
            shortList1.remove(result1.first())
        }
    }
    return Pair(result1, result2)
}

private fun trimList(pref: List<Int>) :Pair<MutableList<Int>, Set<Int>> {
    val shortList = mutableListOf<Int>()
    val options = mutableSetOf<Int>()
    pref.forEach { e ->
        if (options.add(e))
            shortList.add(e)
    }
    return Pair(shortList, options)
}

fun nash22(pref1 :List<Int>, pref2 :List<Int>) :Pair<List<Int>, List<Int>> {
    val (shortList1, options1) = trimList(pref1)
    val (shortList2, options2) = trimList(pref2)
    shortList1.addAll(options2 -options1)
    shortList2.addAll(options1 -options2)
    assert(shortList1.size==shortList2.size)
    val result1 = mutableListOf<Int>()
    val result2 = mutableListOf<Int>()
    if (shortList1.size%4>0) {
        if (shortList2.size%4 == 3) {
            result2.add(0, shortList1.removeAt(shortList1.size-1))
            shortList2.remove(result2.first())
        }
        if (shortList1.isNotEmpty() && shortList2.size%4 >= 2) {
            result2.add(0, shortList1.removeAt(shortList1.size-1))
            shortList2.remove(result2.first())
        }
        if (shortList2.isNotEmpty()) {
            result1.add(0, shortList2.removeAt(shortList2.size-1))
            shortList1.remove(result1.first())
        }
    }
    while (shortList1.isNotEmpty()||shortList2.isNotEmpty()) {
        if (shortList2.isNotEmpty()&&shortList1.size>=4) {
            result1.add(0, shortList2.removeAt(shortList2.size-1))
            shortList1.remove(result1.first())
        }
        if (shortList1.isNotEmpty()&&shortList2.size>=3) {
            result2.add(0, shortList1.removeAt(shortList1.size-1))
            shortList2.remove(result2.first())
        }
        if (shortList1.isNotEmpty()&&shortList2.size>=2) {
            result2.add(0, shortList1.removeAt(shortList1.size-1))
            shortList2.remove(result2.first())
        }
        if (shortList2.isNotEmpty()) {
            result1.add(0, shortList2.removeAt(shortList2.size-1))
            shortList1.remove(result1.first())
        }
    }
    return Pair(result1, result2)
}

fun nash3(pref1: List<Int>, pref2: List<Int>, pref3: List<Int>) :Triple<List<Int>, List<Int>, List<Int>> {
    val (shortList1, options1) = trimList(pref1)
    val (shortList2, options2) = trimList(pref2)
    val (shortList3, options3) = trimList(pref3)
    shortList1.addAll(options2+options3 -options1)
    shortList2.addAll(options3+options1 -options2)
    shortList3.addAll(options1+options2 -options3)
    assert(shortList1.size==shortList2.size)
    assert(shortList1.size==shortList3.size)
    val result1 = mutableListOf<Int>()
    val result2 = mutableListOf<Int>()
    val result3 = mutableListOf<Int>()
    while (shortList1.isNotEmpty()||shortList2.isNotEmpty()||shortList3.isNotEmpty()) {
        if (shortList2.isNotEmpty()&&shortList1.size>=3) {
            result3.add(0, shortList2.removeAt(shortList1.size-1))
            shortList3.remove(result3.first())
            shortList1.remove(result3.first())
        }
        if (shortList1.isNotEmpty()&&shortList2.size>=2) {
            result2.add(0, shortList1.removeAt(shortList1.size-1))
            shortList2.remove(result2.first())
            shortList3.remove(result2.first())
        }
        if (shortList3.isNotEmpty()) {
            result1.add(0, shortList3.removeAt(shortList3.size-1))
            shortList2.remove(result1.first())
            shortList1.remove(result1.first())
        }
    }
    return Triple(result1, result2, result3)
}
