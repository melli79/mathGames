package xmasaoc

import java.io.FileInputStream
import java.io.InputStream

private const val RESOURCE_NAME = "05stacks.txt"

private const val debug = false

fun main(args :Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)
    val (stacks, moves) = parseStacksAndMoves(input)
    if (input is FileInputStream)
        input.close()
    assert(moves.size==501) { "Missing ${501-moves.size} moves!" }
    val stacks1 = stacks.map { it.toMutableList() }
    moveParcels(moves, stacks1)
    print("Result: ")
    printStatus(stacks1)
    val stacks2 = stacks.map { it.toMutableList() }
    moveParcels(moves, stacks2, reversed= false)
    print("modified Result: ")
    printStatus(stacks2)
}

private fun moveParcels(moves :List<Move>, stacks :List<MutableList<Char>>, reversed :Boolean =true) {
    var n = 1
    for (move in moves) {
        if (debug) {
            print("%3d – ".format(n))
            printStatus(stacks)
        }
        val src = move.src.toInt()
        if (src > stacks.size) {
            println("Invalid: $move")
            continue
        }
        val stack = stacks[src - 1]
        var packages :List<Char> = (1..move.amount.toInt()).map { stack.removeAt(0) }
        if (reversed)
            packages = packages.reversed()
        val dst = move.dst.toInt()
        if (dst > stacks.size)
            throw IllegalArgumentException("Invalid: $move")
        stacks[dst - 1].addAll(0, packages)
        n++
    }
}

private fun printStatus(stacks: List<List<Char>>) {
    println(stacks.mapIndexed { n: Int, stack ->
            """${n+1}: ${stack.size}"""
        }.joinToString("; ") +
        stacks.joinToString("", prefix= " -- [", postfix= "]") {
            (it.firstOrNull() ?: 'Ø').toString()
        })
}

private fun parseStacksAndMoves(inputStream: InputStream): Pair<List<List<Char>>, List<Move>> {
    val stacks = mutableListOf<MutableList<Char>>()
    val moves = mutableListOf<Move>()
    val input = inputStream.toReader()
    var mode = true
    input.forEachLine { line ->
        val segments = line.trim().split("\\s+".toRegex())
        if (mode) {
            if (segments.startsWith("1", "2", "3"))
                mode = false
            else
                parseToStacks(line, stacks)
        } else { // parsing moves
            val move = Move.parse(segments)
            if (move!=null)
                moves.add(move)
            else
                println("error parsing line '$line'")
        }
    }
    return Pair(stacks, moves)
}

fun <E> List<E>.startsWith(vararg begin: E) =
    size>=begin.size && zip(begin).all { (f, s) -> f==s }

private fun parseToStacks(line: String, stacks: MutableList<MutableList<Char>>) {
    var pos = -3
    while (true) {
        pos = line.indexOf('[', startIndex = pos+3)
        if (pos<0) break
        val num = pos/4
        while (stacks.size<=num) stacks.add(mutableListOf())
        stacks[num].add(line[pos+1])
    }
}

private data class Move(val src :UByte, val dst :UByte, val amount :UShort) {
    companion object {
        fun parse(input: List<String>) :Move? {
            if (input[0]!="move")
                return null
            val amount = input[1].toUShort()
            if (input[2]!="from")
                return null
            val src = input[3].toUByte()
            if (input[4]!="to")
                return null
            val dst = input[5].toUByte()
            return Move(src, dst, amount)
        }
    }

    override fun toString() = "move $amount from $src to $dst"
}
