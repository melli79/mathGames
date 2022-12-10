package xmasaoc

import java.io.InputStream

private const val RESOURCE_NAME = "08woods.txt"
private val debug = RESOURCE_NAME.matches("small".toRegex())

fun main(args :Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)
    val woods = parseWoods(input)
    if (debug)
        println("The test woods is: \n$woods")
    val visibles = computeVisibles(woods)
    println("There are ${visibles.size} visible trees.")
    val scores = computeScenicScores(woods)
    if (debug)
        println("The scenic scores are:\n$scores")
    println("The maximum scenic score is ${scores.getRows().map { it.max() }.max()}.")
}

fun computeScenicScores(woods: Matrix<UByte>): Matrix<UInt> {
    val values = (1u until woods.numRows-1u).map { r ->
        (1u until woods.numCols-1u).map { c ->
            computeScenicScore(woods, r, c)
        }
    }
    return Matrix(values, zero= 0u)
}

fun computeScenicScore(woods: Matrix<UByte>, r: UInt, c: UInt) :UInt {
    val t = woods[r, c]
    val row = woods.getRow(r)
    val col = woods.getColumn(c)
    val left = row.subList(0, c.toInt()).reversed()
    val right = row.subList(c.toInt()+1, woods.numCols.toInt())
    val up = col.subList(0, r.toInt()).reversed()
    val down = col.subList(r.toInt()+1, woods.numRows.toInt())
    val l = left.indexOfFirstOrElse(left.size) { it >= t }.toUInt()
    val r = right.indexOfFirstOrElse(right.size) { it >= t }.toUInt()
    val u = up.indexOfFirstOrElse(up.size) { it >= t }.toUInt()
    val d = down.indexOfFirstOrElse(down.size) { it >= t }.toUInt()
    return l * r *
            u * d
}

private fun <T> Iterable<T>.indexOfFirstOrElse(default :Int, predicate :(T)->Boolean) :Int {
    val result = indexOfFirst(predicate)
    return if (result>=0) result+1  else default
}

private fun computeVisibles(woods: Matrix<UByte>) =
    (woods.getRows().flatMapIndexed { r, row ->
        extractIncreasingSubsequenceIndices(row).map { c -> Pair(r.toUInt(), c) }
    } + woods.getRows().flatMapIndexed { r, row ->
        extractIncreasingSubsequenceIndices(row.reversed()).map { c -> Pair(r.toUInt(), woods.numCols - c - 1u) }
            .toSet()
    } + (0u until woods.numCols).flatMap { c ->
        extractIncreasingSubsequenceIndices(woods.getColumn(c)).map { r -> Pair(r, c) }.toSet()
    } + (0u until woods.numCols).flatMap { c ->
        extractIncreasingSubsequenceIndices(woods.getColumn(c).reversed())
            .map { r -> Pair(woods.numRows - r - 1u, c) }.toSet()
    }).toSet()

fun <T :Comparable<T>> extractIncreasingSubsequenceIndices(row: List<T>) :List<UInt> {
    val result = mutableListOf<UInt>()
    if (row.isEmpty())
        return result
    val i = row.iterator()
    var last = i.next(); var n = 0u
    result.add(n)
    while (i.hasNext()) {
        var next :T
        do {
            next = i.next(); n++
        } while (i.hasNext()&&next<=last)
        if (next>last) {
            result.add(n)
            last = next
        }
    }
    return result
}

fun parseWoods(input: InputStream): Matrix<UByte> {
    val rows = mutableListOf<List<UByte>>()
    input.toReader().forEachLine { line ->
        val row = line.trim().map { c :Char -> (c-'0').toUByte() }
        if (row.isNotEmpty())
            rows.add(row)
    }
    return Matrix(rows, 0u)
}

class Matrix<F>(values0 :List<List<F>> =emptyList(), val zero :F) {

    private val values = values0.map { row -> row.toMutableList() }.toMutableList()

    val numRows :UInt
        get() = values.size.toUInt()

    val numCols :UInt
        get() = values[0].size.toUInt()

    init {
        for (row in getRows()) {
            assert(numCols == row.size.toUInt())
        }
    }

    override fun toString() = values.joinToString("]\n [", prefix= " [", postfix= "]") { row -> row.joinToString(" ") { v -> "$v" } }

    fun getRows() :List<List<F>> = values

    fun getRow(i :UInt) :List<F> = values[i.toInt()]
    fun getColumn(j :UInt) = values.map { row -> row[j.toInt()] }

    operator fun get(i :UInt, j :UInt) = values[i.toInt()][j.toInt()]
}
