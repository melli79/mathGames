package search

import xmasaoc.getInput
import xmasaoc.toReader

@OptIn(ExperimentalUnsignedTypes::class)
fun main() {
    val grid = readGrid("triangle.data", "src/main/resources")
    val result = findHeaviestPathReversed(grid)
    println("${result.first}: "+ result.second.joinToString { it.reversed().toString() }+", ...")
}

@OptIn(ExperimentalUnsignedTypes::class)
fun readGrid(resourceName :String, path :String ="src/test/resources") :List<UIntArray> {
    val input = getInput("$path/$resourceName", resourceName).toReader()
    return input.lines().map { line ->
        line.trim().split("\\s+".toRegex()).map { it.toUInt() }.toUIntArray()
    }.toList()
}