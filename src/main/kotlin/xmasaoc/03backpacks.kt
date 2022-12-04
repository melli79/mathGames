package xmasaoc

import java.io.InputStream

private const val RESOURCE_NAME = "03backpacks.txt"

fun main(args : Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)

    val backpacks = parseBackpacks(input)
    val scattered = backpacks.map { b -> b.first.firstOrNull { i -> i in b.second } }
    println("Erroneous items: $scattered.")
    val priorities = scattered.filterNotNull().map { c -> prioritize(c) }
    println("Total priority ${priorities.sum()}.")
    val badges = backpacks
        .mapIndexed { n, b -> Pair(n/3+1, b) }.groupBy { p -> p.first }
        .map { (g, ps) -> extractBadge(g, ps) }
    println("""The ${badges.size} badges are: ${badges.joinToString(",\n") {
            "${it.first}: ${it.second}"
        }}""")
    println("That gives a total priority of ${badges.sumOf { p -> prioritize(p.second) }}.")
}

private fun prioritize(c :Char) = if (c in 'A'..'Z')
    c.code - 'A'.code + 27
  else c.code - 'a'.code + 1

fun extractBadge(g :Int, ps :List<Pair<Int, Pair<List<Char>, List<Char>>>>) :Pair<Int, Char> =
    ps.flatMapIndexed { i :Int, (_, b) ->
        b.first.toSet().map { c -> Pair(c, i) } + b.second.toSet().map { c -> Pair(c, i) }
    }.groupBy { p -> p.first }.filter { entry -> entry.value.size==3 }
    .map { entry -> Pair(g, entry.key) }.first()

fun parseBackpacks(inputStram :InputStream) :List<Pair<List<Char>, List<Char>>> {
    val input = inputStram.toReader()
    val result = mutableListOf<Pair<List<Char>, List<Char>>>()
    input.forEachLine { line ->
        assert(line.length%2==0) { "Error on line '$line'!" }
        val part1 = line.substring(0 until line.length/2).toList()
        val part2 = line.substring(line.length/2).toList()
        if (part1.isNotEmpty())
            result.add(Pair(part1, part2))
    }
    return result
}
