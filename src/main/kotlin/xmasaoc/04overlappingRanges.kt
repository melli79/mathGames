package xmasaoc

import java.io.InputStream

private const val RESOURCE_NAME = "04sections.txt"

fun main(args: Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)
    val teams = parseTeams(input)
    val overlaps = teams.mapIndexed { n, team ->
        Pair(n+1, computeOverlap(team))
    }.filter { p -> p.second!=Overlap.DISJOINT }
    val containments = overlaps.filter { p -> p.second != Overlap.PARTIALLY }
    containments.forEach { p -> when (p.second) {
        Overlap.EQUAL -> println("${p.first}: ranges are equal.")
        Overlap.ONE_IN_TWO -> println("${p.first}: range 1 is contained in range 2.")
        Overlap.TWO_IN_ONE -> println("${p.first}: range 2 is contained in range 1.")
        Overlap.PARTIALLY -> println("${p.first}: partial overlap.")
        else -> {}
    } }
    println("That makes ${containments.size} containments.")
    println("And ${overlaps.size} (partial) overlaps.")
}

private fun computeOverlap(team :Pair<IntRange, IntRange>) = when {
    team.first == team.second -> Overlap.EQUAL
    team.first.all { i -> i in team.second } -> Overlap.ONE_IN_TWO
    team.second.all { i -> i in team.first } -> Overlap.TWO_IN_ONE
    team.first.any { i -> i in team.second } -> Overlap.PARTIALLY
    else -> Overlap.DISJOINT
}

enum class Overlap {
    DISJOINT, PARTIALLY, ONE_IN_TWO, TWO_IN_ONE, EQUAL
}

private fun parseTeams(inputStream :InputStream) :List<Pair<IntRange, IntRange>> {
    val result = mutableListOf<Pair<IntRange, IntRange>>()
    inputStream.toReader().forEachLine { line ->
        val members = line.split(",")
        val i1 = members[0].split("-").map { it.toInt() }
        val i2 = members[1].split("-").map { it.toInt() }
        result.add(Pair((i1[0]..i1[1]), (i2[0]..i2[1])))
    }
    return result
}
