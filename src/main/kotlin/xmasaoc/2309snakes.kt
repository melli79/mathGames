package xmasaoc.snakes

import xmasaoc.getInput
import xmasaoc.toReader
import java.io.InputStream
import java.util.Locale
import kotlin.math.abs

private const val RESOURCE_NAME = "09moves.txt"
private val debug = RESOURCE_NAME.matches(".*-.*".toRegex())

fun main(args :Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)
    val moves = readMoves(input)
    val grid = Grid(3u, 3u)
    val snake = Snake(10u);  snake.putOnGrid(grid, Pair(1,1))
    if (debug)
        println("Initial grid:\n$grid")
    else
        println("processing ${moves.size} moves... ")
    moves.forEach {
        move -> snake.move(move)
    }
    if (debug)
        snake.print()
    println("\nSnake visited ${grid.countVisiteds()} cells.")
}

class Snake(val length :UInt) {
    private lateinit var pos :MutableList<Pair<Int, Int>>

    lateinit var grid :Grid
        private set

    fun putOnGrid(grid :Grid, pos :Pair<Int, Int>) {
        this.pos = (1u..length).map { pos }.toMutableList()
        this.grid = grid
        visit()
    }

    private fun dInf(tail :Pair<Int, Int>, head :Pair<Int, Int>) :Int =
        max(abs(tail.first -head.first), abs(tail.second -head.second))

    private fun visit() = grid.visit(pos.last().first, pos.last().second)

    private fun max(a :Int, b :Int) = when {
        a < b -> b
        else -> a
    }

    fun move(move :Move) {
        repeat(move.mult.toInt()) {
            when (move) {
                is Move.Up -> if (pos.first().first<1) {
                        grid.padUp()
                        shiftTail { Pair(it.first +1, it.second) }
                    } else
                        pos[0] = Pair(pos.first().first -1, pos.first().second)
                is Move.Down -> pos[0] = Pair(pos.first().first +1, pos.first().second)
                is Move.Left -> if (pos.first().second<1) {
                        grid.padLeft()
                        shiftTail { Pair(it.first, it.second +1) }
                    } else
                        pos[0] = Pair(pos.first().first, pos.first().second -1)
                is Move.Right -> pos[0] = Pair(pos.first().first, pos.first().second +1)
            }
            tailFollow()
        }
    }

    private fun shiftTail(f :(Pair<Int, Int>) -> Pair<Int, Int>) {
        for (s in 1 until length.toInt()) {
            pos[s] = f(pos[s])
        }
    }

    private fun tailFollow() {
        var s = 0
        while (true) {
            s++
            if (s >= length.toInt())
                break
            val head = pos[s - 1]
            var tail = pos[s]
            if (dInf(head, tail) <= 1)
                break
            when (head.first.compareTo(tail.first)) {
                -1 -> pos[s] = Pair(tail.first -1, tail.second)
                1 -> pos[s] = Pair(tail.first +1, tail.second)
                else -> {}
            }
            tail = pos[s]
            when (head.second.compareTo(tail.second)) {
                -1 -> pos[s] = Pair(tail.first, tail.second -1)
                1 -> pos[s] = Pair(tail.first, tail.second +1)
                else -> {}
            }
        }
        if (s == length.toInt())
            visit()
    }

    fun print() {
        val rows = grid.getRows().map { it.toMutableList() }.toMutableList()
        val numRows = max(rows.size, pos.maxOf { it.first } +1)
        val oldRowSize = rows[0].size
        val numCols = max(oldRowSize, pos.maxOf { it.second } +1)
        if (oldRowSize <numCols) {
            rows.forEach { it.addAll((oldRowSize until numCols).map { Grid.EMPTY }) }
        }
        if (rows.size<numRows)
            rows.addAll((rows.size until numRows).map { Grid.createEmptyRow(numCols) })
        for (s in (1 until length.toInt()).reversed())
            rows[pos[s].first][pos[s].second] = '0'+s
        rows[pos[0].first][pos[0].second] = 'H'
        println(format(rows))
    }
}

fun readMoves(input :InputStream) :List<Move> {
    val result = mutableListOf<Move>()
    input.toReader().forEachLine { line ->
        val tokens = line.trim().split("\\s+".toRegex())
        if (tokens.size>=2) {
            val mult = tokens[1].toUByte()
            when (tokens[0].lowercase(Locale.getDefault())) {
                "u" -> result.add(Move.Up(mult))
                "d" -> result.add(Move.Down(mult))
                "l" -> result.add(Move.Left(mult))
                "r" -> result.add(Move.Right(mult))
                else -> println("unknown move '$line'")
            }
        }
    }
    return result
}

sealed interface Move {
    val mult :UByte

    data class Up(override val mult :UByte) :Move {}
    data class Down(override val mult :UByte) :Move {}
    data class Left(override val mult :UByte) :Move {}
    data class Right(override val mult :UByte) :Move {}
}
class Grid(numRows :UInt =3u, numCols :UInt =3u) {
    companion object {
        const val EMPTY = '.'
        const val VISITED = '#'

        fun createEmptyRow(numCols :Int) = (1..numCols).map { EMPTY }.toMutableList()
    }
    var numRows = numRows.toInt()
        private set

    var numCols = numCols.toInt()
        private set

    private val data = (1u..numRows).map { createEmptyRow(numCols.toInt()) }.toMutableList()

    fun visit(r :Int, c :Int) {
        if (c>=numCols) {
            data.forEach { row ->
                row.addAll((numCols..c).map { EMPTY })
            }
            numCols = c+1
        }
        if (r>=numRows) {
            data.addAll((numRows..r).map { createEmptyRow(numCols) })
            numRows = r+1
        }
        data[r][c] = VISITED
    }

    operator fun get(r :Int, c :Int) = data[r][c]

    override fun toString() = format(data)

    fun countVisiteds() = data.map { it.count { it==VISITED } }.sum()
    fun padUp() {
        data.add(0, createEmptyRow(numCols))
        print("T")
    }

    fun padLeft() {
        data.forEach { row -> row.add(0, EMPTY) }
        print("L")
    }

    fun getRows() :List<List<Char>> = data
}

fun format(data :List<List<Char>>) = data.joinToString("\n ", prefix = " ") { it.joinToString("") { it.toString() } }
