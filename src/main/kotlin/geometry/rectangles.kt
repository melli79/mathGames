package geometry

import kotlin.math.max
import kotlin.math.min

fun computeTotalArea(rs: Collection<Rectangle>) :ULong {
    var result = rs.sumOf { it.area }
    var candidates = rs.map { Pair(setOf(it), it) }.toMap()
    var depth = 2
    while (candidates.isNotEmpty()) {
        val pair = checkIntersections(candidates, depth, result)
        result = pair.second
        candidates = pair.first
        depth++
    }
    return result
}

private fun checkIntersections(
    candidates :Map<Set<Rectangle>, Rectangle>,
    depth :Int,
    lastPartialArea :ULong
) :Pair<MutableMap<Set<Rectangle>, Rectangle>, ULong> {
    var partialArea = lastPartialArea
    val newCandidates = mutableMapOf<Set<Rectangle>, Rectangle>()
    for (i1 in candidates) {
        for (i2 in candidates) if (i1 != i2) {
            partialArea = mergeIntersections(i1, i2, depth, newCandidates, partialArea)
        }
    }
    return Pair(newCandidates, partialArea)
}

private fun mergeIntersections(
    i1 :Map.Entry<Set<Rectangle>, Rectangle>,
    i2 :Map.Entry<Set<Rectangle>, Rectangle>,
    depth :Int,
    newCandidates :MutableMap<Set<Rectangle>, Rectangle>,
    partialArea :ULong
) :ULong {
    val rects = i1.key + i2.key
    if (rects.size == depth && rects !in newCandidates.keys) {
        val intersection = i1.value.intersect(i2.value)
        if (intersection != null) {
            newCandidates[rects] = intersection
            return if (depth % 2 == 0) {
                partialArea - intersection.area
            } else {
                partialArea + intersection.area
            }
        }
    }
    return partialArea
}

data class Rectangle(val x0 :Int, val y0 :Int, val width :UInt, val height :UInt) {
    val area :ULong
        get() = width.toULong()*height

    fun intersect(r2 :Rectangle) :Rectangle? {
        val ix = Interval(x0, width).intersect(Interval(r2.x0, r2.width))
        val iy = Interval(y0, height).intersect(Interval(r2.y0, r2.height))
        if (ix!=null && iy!=null)
            return Rectangle(ix.begin, iy.begin, ix.width, iy.width)
        return null
    }
}

internal data class Interval(val begin :Int, val width :UInt) {
    val end = begin +width.toInt()
    fun intersect(i2 :Interval) :Interval? {
        val begin = max(begin, i2.begin)
        val end = min(end, i2.end)
        if (begin<end)
            return Interval(begin, (end-begin).toUInt())
        return null
    }
}
