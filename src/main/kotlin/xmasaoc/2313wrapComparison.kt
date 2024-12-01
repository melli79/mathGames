package xmasaoc

import java.io.FileInputStream
import java.io.InputStream

private const val RESOURCE_NAME = "13pairWraps.txt"
private val debug = '-' in RESOURCE_NAME
private const val TWO = "[[2]]"
private const val SIX = "[[6]]"

fun main(args: Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)
    val pairs = parsePairsOfWraps(input)
    if (input is FileInputStream)
        input.close()
    val inOrders = mutableListOf<Int>()
    var n = 1
    for (p in pairs) {
        val cmp = myCompare(p.first, p.second)
        if (debug)  when (cmp) {
            -1 -> println("${p.first} < ${p.second}")
            0 -> println("${p.first} == ${p.second}")
            1 -> println("${p.first} > ${p.second}")
            else -> println("unknown")
        }
        if (cmp<=0) {
            inOrders.add(n)
        }
        n++
    }
    println("There are ${inOrders.size} pairs in order.  Their index sum is ${inOrders.sum()}.")
    val input2 = getInput(args.firstOrNull(), RESOURCE_NAME)
    val items = parseInput(input2) + listOf(TWO, SIX)
    val sorted = items.sortedWith(signalComparator)
    if (debug)
        println(sorted.joinToString("\n"))
    val i2 = sorted.indexOf(TWO)+1;  val i6 = sorted.indexOf(SIX)+1
    println("The markers are at $i2 and $i6, so the key is ${i2*i6}.")
}

object signalComparator :Comparator<String> {
    override fun compare(a :String, b :String) :Int {
        val (left, right) = conormalize(a, b)
        return myCompare(left, right)
    }
}

fun parseInput(input :InputStream) :List<String> {
    val result = mutableListOf<String>()
    input.toReader().forEachLine { line ->
        val list = line.trim()
        if (list.isNotEmpty())
            result.add(list)
    }
    return result
}

private val delim = setOf(',', ']', '[')
private val starting = setOf(',', '[')

// assuming only 1-digit numbers
private fun myCompare(a :String, b :String) :Int {
    var ia = 0; var ib = 0
    while (ia<a.length && ib<b.length) {
        while (ia<a.length&& a[ia] in starting && ib<b.length && a[ia]==b[ib]) {
            ia++;  ib++
        }
        if (ia==a.length||ib==b.length)
            break
        if (a[ia] in starting)
            return 1
        if (b[ib] in starting)
            return -1
        val (na, ea) = parseNumber(a, ia)
        val (nb, eb) = parseNumber(b, ib)
        if (na!=nb) {
            if (na.isEmpty())
                return -1
            if (nb.isEmpty())
                return 1
            return na.toInt().compareTo(nb.toInt())
        }
        ia = ea; ib = eb
        while (ia<a.length&&ib<b.length && a[ia]==']'&&b[ib]==']') {
            ia++;  ib++
        }
    }
    if (ia==a.length)
        return if (ib==b.length) 0  else -1
    return 1
}

fun parsePairsOfWraps(inputStream :InputStream) :List<Pair<String, String>> {
    val result = mutableListOf<Pair<String, String>>()
    val input = inputStream.toReader()
    while (input.ready()) {
        var line :String
        do {
            line = input.readLine().trim()
        } while (line.isBlank()&&input.ready())
        if (line.isBlank())
            break
        val first = line
        val second = input.readLine().trim()
        if (second.isBlank()) {
            println("Missing second element in pair.")
            break
        }
        result.add(conormalize(first, second))
    }
    return result
}

fun conormalize(a :String, b :String) :Pair<String, String> {
    var resA = a;  var resB = b
    var ia = 0;  var ib = 0
    outer@while (ia<resA.length && ib<resB.length) {
        while (resA[ia]==resB[ib]) {
            ia++;  ib++
            if (ia==resA.length||ib==resB.length)
                break@outer
        }
        if (resA[ia]=='[') {
            val (number, end) = parseNumber(resB, ib)
            if (number.isNotEmpty())
                resB = resB.substring(0, ib) +'['+number+']'+resB.substring(end)
        } else if (resB[ib]=='[') {
            val (number, end) = parseNumber(resA, ia)
            if (number.isNotEmpty())
               resA = resA.substring(0, ia) +'['+number+']'+resA.substring(end)
        }
        ia++;  ib++
    }
    return Pair(resA, resB)
}

private fun parseNumber(input :String, start :Int) :Pair<String, Int> {
    var number = "";  var end = start
    while (end < input.length) {
        val c = input[end]
        if (c in delim)
            break
        number += c;  end++
    }
    return Pair(number, end)
}
