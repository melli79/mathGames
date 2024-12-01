package xmasaoc.maze

import partitions.abs
import xmasaoc.Matrix
import xmasaoc.getInput
import java.lang.System.out

private const val RESOURCE_NAME = "12maze.txt"
private val debug = "-" in RESOURCE_NAME
private const val start = 'S'
private const val stop = 'E'

fun main(args: Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)
    val (maze, begin, end) = parseMaze(input)
    println("The maze ${maze.numRows}x${maze.numCols}: $begin -> $end\n" +maze.format())
    val tree = sortTree(maze, begin, end)
    println("")
}

fun sortTree(maze :Matrix<Byte>, begin :Pair<Int, Int>, end :Pair<Int, Int>) :Tree<Pair<Int, Int>> {
    val numRows = maze.numRows;  val numCols = maze.numCols
    val result = Tree(begin)
    val opens = mutableListOf(result)
    val closed = mutableSetOf<Pair<Int, Int>>()
    outer@while (opens.isNotEmpty()) {
        val node = opens.removeAt(0)
        val pos = node.value
        closed.add(pos)
        val currentHeight = maze[pos.first, pos.second]
        for (d in Direction_values) {
            val next = pos.move(d, numRows, numCols)
            if (next!=null && next !in closed && abs(maze[next.first, next.second] -currentHeight) <=1u) {
                val t = Tree(next)
                node.children.add(t)
                opens.add(t)
                if (next==end) {
                    println("Solved the maze in ${result.maxDepth()-1} steps.")
                    break@outer
                }
            }
        }
        if (closed.size%100==0) {
            print("."); out.flush()
            if (closed.size>(numRows*numCols).toInt())
                throw IllegalStateException("In too deep!")
        }
    }
    return result
}

data class Tree<T>(val value :T, val children :MutableSet<Tree<T>> =mutableSetOf()) {
    fun maxDepth() :Int = children.maxOfOrNull { n -> 1+n.maxDepth() } ?: 1
}
