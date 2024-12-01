package xmasaoc.maze

import partitions.abs
import xmasaoc.Matrix
import xmasaoc.getInput
import xmasaoc.toReader
import java.io.InputStream

private const val RESOURCE_NAME = "12maze.txt"
private val debug = "-" in RESOURCE_NAME
private const val start = 'S'
private const val stop = 'E'

fun main(args: Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)
    val (maze, begin, end) = parseMaze(input)
    println("The maze ${maze.numRows}x${maze.numCols}: $begin -> $end\n" +maze.format())
    val path = findPath(maze, begin, end)
    println("Solution:\n$path")
}

fun Matrix<Byte>.format() = getRows().mapIndexed { r, row ->
    row.joinToString("", prefix= "%2d: ".format(r)) { ('a'+(it-1)).toString() }
}.joinToString("\n")

typealias Dir = Char
const val UNKNOWN = '.'
const val UP = '^'
const val DOWN = 'v'
const val LEFT = '<'
const val RIGHT = '>'
val Direction_values = listOf(DOWN, RIGHT, UP, LEFT)

fun findPath(maze :Matrix<Byte>, begin :Pair<Int, Int>, end :Pair<Int, Int>) :Matrix<Dir> {
    val result = Matrix((1u..maze.numRows).map { (1u..maze.numCols).map { UNKNOWN } }, UNKNOWN)
    val length = result.seek(maze, begin, end, Int.MAX_VALUE)
    if (length>10240)
        throw IllegalArgumentException("Could not find any path to the target.")
    println("Shortest path length is $length.")
    return result
}

private fun Matrix<Dir>.seek(maze :Matrix<Byte>, begin :Pair<Int, Int>, end :Pair<Int, Int>, limit :Int) :Int {
    if (begin==end)
        return 0
    if (limit==0)
        return Int.MAX_VALUE
    val currentHeight = maze[begin.first, begin.second]
    val options = Direction_values.filter { d ->
        val t = begin.move(d, numRows, numCols)
        t!=null && get(t.first, t.second)==UNKNOWN && abs(maze[t.first, t.second]-currentHeight)<=1u
    }
    var min = limit;  var solution :Matrix<Dir>? =null
    val empty = copy()
    for (d in options) {
        this[begin.first, begin.second] = d
        val t = begin.move(d, numRows, numCols)!!
        val length = seek(maze, t, end, min-1)
        if (length<min-1) {
            min = length+1
            solution = copy()
            println("Candidate\n$solution\n")
            set(empty)
        }
    }
    if (solution!=null)
        set(solution)
    else
        set(begin.first, begin.second, UNKNOWN)
    return min
}

fun Pair<Int, Int>.move(d :Dir, numRows :UInt, numCols :UInt) :Pair<Int, Int>? = when (d) {
    RIGHT -> if (this.second<numCols.toInt()-1) Pair(this.first, this.second+1)  else null
    DOWN -> if (this.first<numRows.toInt()-1) Pair(this.first+1, this.second)  else null
    LEFT -> if (this.second>0) Pair(this.first, this.second-1)  else null
    UP -> if (this.first>0) Pair(this.first-1, this.second)  else null
    else -> null
}

fun parseMaze(input :InputStream) :Triple<Matrix<Byte>, Pair<Int, Int>, Pair<Int, Int>> {
    val result = Matrix<Byte>(emptyList(), 0)
    lateinit var begin :Pair<Int, Int>
    lateinit var end :Pair<Int, Int>
    input.toReader().forEachLine { line ->
        val row = line.trim().mapIndexed { col, c -> when (c) {
            start -> {
                begin = Pair(result.numRows.toInt(), col)
                1
            }
            stop -> {
                end = Pair(result.numRows.toInt(), col)
                26
            }
            else -> (c-'a'+1).toByte()
        } }
        if (row.isNotEmpty())
            result.add(row)
    }
    return Triple(result, begin, end)
}
