package xmasaoc.intepreter

import xmasaoc.getInput
import xmasaoc.toReader
import java.io.InputStream

private const val RESOURCE_NAME = "10program.prg"
private val debug = '-' in RESOURCE_NAME

fun main(args :Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)
    val ast = parse(input)
    if (debug)
        println(ast.mapIndexed { n, instr -> "$n: $instr" }.joinToString("\n"))
    println("${ast.size} sloc.")
    val trace = Interpreter.trace(ast)
    if (debug)
        println(trace)
    val times = (0..(220/40)).map { 20 +40*it }
    val dump = times.map { c -> Pair(c, trace[c-1]) }
    println(dump.joinToString { (c, x) -> "$c: $x" })
    println("Checksum: ${dump.sumOf { it.first*it.second }}")
}

object Interpreter {
    var x = 1

    var cycle = 0
        private set

    fun trace(ast :Iterable<Instruction>) :List<Int> {
        val result = mutableListOf<Int>()
        ast.forEach { instruction ->
            result.add(x);  cycle++
            when (instruction) {
                is Instruction.Noop -> {}
                is Instruction.Addx -> {
                    result.add(x)
                    x += instruction.op
                    cycle++
                }
            }
        }
        result.add(x)
        return result
    }
}

fun parse(input :InputStream) :List<Instruction> {
    val result = mutableListOf<Instruction>()
    input.toReader().forEachLine { line ->
        val tokens = line.trim().split("\\s+".toRegex())
        if (tokens.isNotEmpty()) {
            val instruction = Instruction.parse(tokens)
            if (instruction!=null)
                result.add(instruction)
        }
    }
    return result
}

sealed interface Instruction {
    companion object {
        fun parse(tokens :List<String>) :Instruction? {
            if (tokens.isEmpty())
                return null
            return when (tokens[0]) {
                Noop.name -> Noop
                Addx.name -> {
                    val op = tokens.getOrNull(1)?.toInt()
                    if (op!=null)
                        Addx(op)
                    else {
                        println("Missing integer operand to addx")
                        null
                    }
                }
                else -> {
                    println("Unknown instruction: ${tokens.joinToString(" ")}")
                    null
                }
            }
        }
    }

    val cycles :UByte

    object Noop :Instruction {
        val name = "noop"
        override val cycles = 1.toUByte()

        override fun toString() = name
    }

    data class Addx(val op :Int) :Instruction {
        companion object {
            val name = "addx"
        }
        override val cycles = 2.toUByte()

        override fun toString() = "$name $op"
    }
}
