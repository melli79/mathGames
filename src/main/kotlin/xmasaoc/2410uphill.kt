package xmasaoc

import java.util.stream.Stream
import kotlin.streams.asSequence

private typealias GridB = Array<ByteArray>
private typealias Pos2 = Pair<Int, Int>

private fun readMap(input :Stream<String>) :Triple<GridB, List<Pos2>, List<Pos2>> {
    val startPoints = mutableListOf<Pos2>()
    val endPoints = mutableListOf<Pos2>()
    val grid = input.asSequence().mapIndexed { r :Int, row :String ->
        row.mapIndexed { c :Int, ch :Char ->
            val v = ch - '0'
            if (v==0) startPoints.add(Pos2(r, c))
            if (v==9) endPoints.add(Pos2(r, c))
            v.toByte()
        }.toByteArray()
    }.toList()
    return Triple(grid.toTypedArray(), startPoints, endPoints)
}

private fun GridB.countScore(start :Pos2, numEndPoints :UInt) :UInt {
    val opens = mutableListOf(Triple(start.first, start.second, 0))
    var count = 0u
    while (opens.isNotEmpty()) {
        val current = opens.removeLast()
        if (current.first>0 && get(current.first-1)[current.second].toInt()==current.third+1) {
            val new = Triple(current.first - 1, current.second, current.third + 1)
            get(new.first)[new.second] = 0 // prune path
            if (current.third == 8) ++count
            else opens.add(new)
        }
        if (current.second>0 && get(current.first)[current.second-1].toInt()==current.third+1) {
            val new = Triple(current.first, current.second-1, current.third+1)
            get(new.first)[new.second] = 0
            if (current.third == 8) ++count
            else opens.add(new)
        }
        if (current.second+1<first().size && get(current.first)[current.second+1].toInt()==current.third+1) {
            val new = Triple(current.first, current.second + 1, current.third + 1)
            get(new.first)[new.second] = 0
            if (current.third == 8) ++count
            else opens.add(new)
        }
        if (current.first+1<size && get(current.first+1)[current.second].toInt()==current.third+1) {
            val new = Triple(current.first + 1, current.second, current.third + 1)
            get(new.first)[new.second] = 0
            if (current.third == 8) ++count
            else opens.add(new)
        }
        if (count >= numEndPoints)  return count
    }
    return count
}

private fun GridB.countRating(start :Pos2) :UInt {
    val opens = mutableListOf(Triple(start.first, start.second, 0))
    var count = 0u
    while (opens.isNotEmpty()) {
        val current = opens.removeLast()
        if (current.first>0 && get(current.first-1)[current.second].toInt()==current.third+1) {
            val new = Triple(current.first - 1, current.second, current.third + 1)
            if (current.third == 8) ++count
            else opens.add(new)
        }
        if (current.second>0 && get(current.first)[current.second-1].toInt()==current.third+1) {
            val new = Triple(current.first, current.second-1, current.third+1)
            if (current.third == 8) ++count
            else opens.add(new)
        }
        if (current.second+1<first().size && get(current.first)[current.second+1].toInt()==current.third+1) {
            val new = Triple(current.first, current.second + 1, current.third + 1)
            if (current.third == 8) ++count
            else opens.add(new)
        }
        if (current.first+1<size && get(current.first+1)[current.second].toInt()==current.third+1) {
            val new = Triple(current.first + 1, current.second, current.third + 1)
            if (current.third == 8) ++count
            else opens.add(new)
        }
    }
    return count
}

private fun GridB.score(startPoints :List<Pos2>, numEndPoints :UInt) = startPoints.sumOf { start ->
    this.map { it.clone() }.toTypedArray().countScore(start, numEndPoints).toULong()
}

private fun GridB.rate(startPoints :List<Pos2>) = startPoints.sumOf { start ->
    countRating(start).toULong()
}

fun main() {
    // val input = listOf(
    //     "0123",
    //     "1234",
    //     "8765",
    //     "9876"
    // ).stream()
    // val input = listOf(
    //     "89010123",
    //     "78121874",
    //     "87430965",
    //     "96549874",
    //     "45678903",
    //     "32019012",
    //     "01329801",
    //     "10456732"
    // ).stream()
    val input = getInput(null, "2410hills.txt").toReader().lines()
    val (grid, startPoints, endPoints) = readMap(input)
    println("The system has ${startPoints.size} trail heads (and ${endPoints.size} end points).")
    val score = grid.score(startPoints, endPoints.size.toUInt())
    println("There are $score paths from 0s to 9s.")
    val rating = grid.rate(startPoints)
    println("The total rating for the ${startPoints.size} trail heads is $rating.")
}
