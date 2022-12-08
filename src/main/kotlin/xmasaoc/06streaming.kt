package xmasaoc

import java.io.BufferedReader
import java.io.InputStream
import java.io.StringReader

private const val RESOURCE_NAME = "06stream.txt"

fun main(args: Array<String>) {
    val data = getInput(args.firstOrNull(), RESOURCE_NAME)
    val input = convertDataToStream(data) ?: throw IllegalArgumentException("Cannot read input data")
    val pos = searchMarker(input, 14)
    println("Message starts at $pos.")
}

private fun searchMarker(input :BufferedReader, len :Int) :Int {
    var pos = 0
    val lastUniqueChars = mutableListOf<Char>()
    while (lastUniqueChars.size < len) {
        val i = input.read()
        if (i < 0) {
            println("End of stream reached")
            break
        }
        val c = i.toChar()
        pos++
        if (c in lastUniqueChars) {
            val index = lastUniqueChars.indexOf(c)
            repeat(index + 1) { lastUniqueChars.removeFirst() }
        }
        lastUniqueChars.add(c)
    }
    return pos
}

fun convertDataToStream(dataStream :InputStream) :BufferedReader? {
    val dataReader = dataStream.toReader()
    var line :String? = null
    while (line.isNullOrBlank() && dataReader.ready()) {
        line = dataReader.readLine().trim()
    }
    return if (line!=null)
        BufferedReader(StringReader(line))
    else
        null
}
