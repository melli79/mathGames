package search

import java.lang.Math.abs

@OptIn(ExperimentalUnsignedTypes::class)
fun solve(bribes :UIntArray, solution :UInt) :Pair<UInt, List<UInt>> {
    var price = 0u
    val path = mutableListOf<UInt>()
    var m = 0;  var M = bribes.size
    while (m<M) {
        val totalPrice = bribes.slice(m until M).sum()
        var index = bribes.findMiddle(totalPrice/2u)
        if (index<0) break
        index += m
        price += bribes[index];  path.add(index.toUInt())
        when (solution.compareTo(index.toUInt())) {
            -1 -> M = index
            0 -> break
            else -> m = index+1
        }
    }
    return Pair(price, path)
}

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalStdlibApi::class)
private fun UIntArray.findMiddle(midValue: UInt): Int {
    val result = cumSum().argMinRange { abs(midValue.toInt() - it.toInt()) }
    return (result.start+result.endInclusive)/2
}

private fun <E, C :Comparable<C>> List<E>.argMinRange(f:(E) -> C): IntRange {
    if (isEmpty()) return -1..-1
    var start = 0; var m = f(get(start))
    var end = start
    for (candidate in 1 until size) {
        val mc = f(get(candidate))
        if (mc==m)
            end++
        if (mc<m) {
            start = candidate;  m = mc;  end = start
        }
    }
    return start .. end
}

fun <E, C :Comparable<C>> List<E>.argMin(f:(E)->C): Int {
    if (isEmpty()) return -1
    var result = 0; var m = f(get(result))
    for (candidate in 1 until size) {
        val mc = f(get(candidate))
        if (mc<m) {
            result = candidate;  m = mc
        }
    }
    return result
}

@OptIn(ExperimentalUnsignedTypes::class)
fun UIntArray.cumSum(): List<UInt> {
    var s = 0u
    return listOf(s) +map { s+=it; s }
}
