package xmasaoc

import common.math.gcd
import partitions.abs
import kotlin.streams.asSequence

fun getAntennas(input :Sequence<String>) :Triple<Int, Int, Map<Char, List<Pair<Int, Int>>>> {
    var numRows = 0;
    var numCols = 0
    val antennas = input.flatMapIndexed { r :Int, row :String ->
        if (numCols < row.length) numCols = row.length
        numRows = r + 1
        row.mapIndexed { c :Int, a -> if (a != '.' && a != '#') Triple(a, r, c) else null }.filterNotNull()
    }.groupBy { it.first }.map { en -> Pair(en.key, en.value.map { Pair(it.second, it.third) }) }.toMap()
    println("Geometry: $numRows x $numCols")
    return Triple(numRows, numCols, antennas)
}

fun computeAntiNodes(numRows :Int, numCols :Int, antennas :Map<Char, List<Pair<Int, Int>>>) :Set<Pair<Int, Int>> {
    val result = mutableSetOf<Pair<Int, Int>>()
    for ((type, ants) in antennas.entries) {
        println("'$type'-Antennas: $ants")
        for (a1 in ants) for (a2 in ants) if (a1!=a2) {
            val v = Pair(a2.first-a1.first, a2.second-a1.second)
            val n1 = Pair(a1.first-v.first, a1.second-v.second)
            if (n1.first in 0..<numRows && n1.second in 0..<numCols)
                result.add(n1)
        }
    }
    return result
}

fun computeFullAntiNodes(numRows :Int, numCols :Int, antennas :Map<Char, List<Pair<Int, Int>>>) :Set<Pair<Int, Int>> {
    val result = mutableSetOf<Pair<Int, Int>>()
    for ((type, ants) in antennas.entries) {
        println("'$type'-Antennas: $ants")
        for (a1 in ants) for (a2 in ants) if (a1!=a2) {
            val v1 = Pair(a2.first-a1.first, a2.second-a1.second)
            val d = gcd(abs(v1.first).toULong(), abs(v1.second).toULong()).toInt()
            val v = Pair(v1.first/d, v1.second/d)
            var n = a2
            while (n.first in 0..<numRows && n.second in 0..<numCols) {
                result.add(n)
                n = Pair(n.first+v.first, n.second+v.second)
            }
        }
    }
    return result
}

fun main() {
    // val input = listOf(
    //     "............",
    //     "........0...",
    //     ".....0......",
    //     ".......0....",
    //     "....0.......",
    //     "......A.....",
    //     "............",
    //     "............",
    //     "........A...",
    //     ".........A..",
    //     "............",
    //     "............"
    // ).asSequence()
    val input = getInput(null, "2408antennas.txt").toReader().lines().asSequence()
    val (numRows, numCols, antennas) = getAntennas(input)
    val result = computeAntiNodes(numRows, numCols, antennas)
    println("There are ${result.size} anti-nodes.\n")
    val res2 = computeFullAntiNodes(numRows, numCols, antennas)
    println("There are ${res2.size} full anti-nodes.")
}
