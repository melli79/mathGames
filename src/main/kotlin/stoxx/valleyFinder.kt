package stoxx

import kotlin.math.max
import kotlin.math.min

fun findValleys(profile :List<Int>) :List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    if (profile.isEmpty())
        return result
    var left = profile.first(); var leftBoundary = 0
    var i = 1
    while (i<profile.size) {
        // going down
        while (i<profile.size && left>=profile[i]) {
            left = profile[i++]
        }
        // going up
        while(i<profile.size && left<=profile[i]) {
            left = profile[i++]
        }
        if (leftBoundary<i-1)
            result.add(Pair(leftBoundary, i-1))
        leftBoundary = i-1
    }
    return result
}

class Profile2D(ini :Collection<List<Int>>) :ArrayList<List<Int>>(ini) {
    constructor() :this(emptyList())
}

class DisconnectedException() :Throwable("Valley is disconnected") {
}

fun findValleys(profile :Profile2D) :Set<List<Triple<Int, Int, Int>>> {
    val valleys = profile.map { p -> findValleys(p).toMutableList() }
    val result = mutableSetOf<List<Triple<Int, Int, Int>>>()
    while (valleys.isNotEmpty()) {
        var y = valleys.indices.firstOrNull { y -> valleys[y].isNotEmpty() } ?: break
        val segment = valleys[y].firstOrNull()!!
        valleys[y].remove(segment)
        var lastSegment = Triple(y++, segment.first, segment.second)
        val valley = mutableListOf(lastSegment)
        while (y<valleys.size) {
            val nextSegment = valleys[y].firstOrNull { s -> lastSegment.second<=s.second }
            if (nextSegment==null||lastSegment.third<nextSegment.first) {
                break
            }
            valleys[y].remove(nextSegment)
            lastSegment = Triple(y++, nextSegment.first, nextSegment.second)
            valley.add(lastSegment)
        }
        result.add(valley)
    }
    return result
}

fun MutableList<Triple<Int, Int, Int>>.syncUp(
    valleys1 :MutableList<Pair<Int, Int>>,
    y :Int,
    v2 :Pair<Int, Int>,
    lastStep :Int,
    segments :MutableList<Triple<Int, Int, Int>>) {
    val v1 = valleys1.firstOrNull { v -> v2.first<=v.second+1 } ?: throw DisconnectedException()
    val inters = intersect(v1, v2) ?: throw DisconnectedException()
    add(Triple(y, inters.first, lastStep-1))
    if (v1.first>=inters.first-1) {
        segments.add(Triple(y-1, v1.first, v1.second))
    }
}

private fun syncDown(valleys0 :List<Pair<Int, Int>>, valleys1 :List<Pair<Int, Int>>)
        :Pair<MutableList<Pair<Int, Int>>, MutableList<Triple<Int, Int, Int>>> {
    if (valleys0.isEmpty()||valleys1.isEmpty())
        return Pair(mutableListOf(), mutableListOf())
    val v0 = valleys0.first();  val v1 = valleys1.first()
    val inters = intersect(v0, v1) ?: return Pair(mutableListOf(), mutableListOf())
    val segments = mutableListOf<Triple<Int, Int, Int>>()
    val boundary = if (v0.first>=inters.first-1) {
        if (v0.second<=inters.second+1) {
            segments.add(Triple(0, v0.first, v0.second))
            (v0.first..v0.second).map { x -> Pair(0, x) }.toMutableList()
        } else
            (v0.first .. inters.second).map { x -> Pair(0, x)}.toMutableList()
    } else {
        if (v0.second<=inters.second+1)
            (inters.first .. v0.second).map { x -> Pair(0, x) }.toMutableList()
        else
            (inters.first .. inters.second).map { x -> Pair(0, x) }.toMutableList()
    }
    if (v1.second<=inters.second+1)
        boundary.add(Pair(1, v1.second))
    else
        boundary.add(Pair(1, inters.second))
    return Pair(boundary, segments)
}

fun intersect(v0 :Pair<Int, Int>, v1 :Pair<Int, Int>) :Pair<Int, Int>? {
    val left = max(v0.first, v1.first)
    val right = min(v0.second, v1.second)
    if (left<=right)
        return Pair(left, right)
    return null
}
