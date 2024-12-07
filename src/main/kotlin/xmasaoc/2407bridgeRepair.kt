package xmasaoc

import kotlin.streams.asSequence

infix fun ULong.conc(c :ULong) :ULong {
    var p = 10uL
    while (c>=p)  p *= 10u
    return times(p)+c
}

fun fix(input :Sequence<String>) :ULong = input.sumOf { line ->
    val idx = line.indexOf(':')
    val target = line.slice(0..<idx).toULong()
    val operands = line.substring(idx+1).trim().split(' ').map { it.toULong()  }.iterator()
    if (operands.hasNext()) {
        var res = setOf(operands.next())
        while (operands.hasNext()) {
            val op = operands.next()
            res = res.flatMap { listOf(it+op, it*op, it conc op)  }.toSet()
        }
        if (target in res) target.toULong()
        else 0uL
    } else 0uL
}

fun main() {
    // val input = listOf(
    //     "190: 10 19",
    //     "3267: 81 40 27",
    //     "83: 17 5",
    //     "156: 15 6",
    //     "7290: 6 8 6 15",
    //     "161011: 16 10 13",
    //     "192: 17 8 14",
    //     "21037: 9 7 18 13",
    //     "292: 11 6 16 20"
    // )
    val input = getInput(null, "2407tests.txt").toReader().lines()
    val result = fix(input.asSequence())
    println("The useful test values add up to $result.")
}
