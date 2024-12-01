package xmasaoc

import java.io.FileInputStream
import java.io.InputStream

private const val RESOURCE_NAME = "16small-valves.txt"
private val debug = '-' in RESOURCE_NAME
private const val time = 30u
private const val start = "AA"

fun main(args: Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)
    val valves = parseValves(input)
    if (input is FileInputStream)
        input.close()
    if (debug)
        println(valves.values.joinToString("\n"))
    println("Parsed ${valves.size} valves.")
    val (totalRelease, strategy) = fillGreedy(valves)
    println("We reach $totalRelease with visiting the following valves: " +strategy.joinToString { p ->
        (if(p.second) "open"  else "pass")+" valve "+p.first.name
    })
}

private fun fillGreedy(valves :Map<String, Valve>) :Pair<UInt, List<Pair<Valve, Boolean>>> {
    val best = valves.values.filter { it.rate>0u }.sortedByDescending { it.rate }
    val openeds = mutableSetOf<Valve>()
    var totalRelease = 0u
    val result = mutableListOf<Pair<Valve, Boolean>>()
    var valve = valves[start]!!
    var remainingTime = time
    for (goal1 in best) if (goal1 !in openeds) {
        val shortPath = bfs(valve, goal1, remainingTime-1u, openeds)
        if (shortPath!=null) {
            val (release, trail) = openeds.openSome(shortPath, remainingTime)
            val path = if (result.isEmpty()) trail else trail.subList(1, trail.size)
            result.addAll(path)
            totalRelease += release
            remainingTime -= path.sumOf { if (it.second) 2u else 1.toUInt() }
            if (remainingTime<=2u)
                break
            valve = path.last().first
        }
    }
    return Pair(totalRelease, result)
}

private fun MutableSet<Valve>.openSome(path :List<Valve>, maxTime :UInt) :Pair<UInt, List<Pair<Valve, Boolean>>> {
    var release = 0u
    val result = mutableListOf<Pair<Valve, Boolean>>()
    var remainingLength = path.size.toUInt()
    var time = 0u;  var n = 0
    for (valve in path) {
        val open = remainingLength==0u || valve.rate>0u && maxTime-time>remainingLength && valve !in this
        result.add(Pair(valve, open))
        time += if (open) 2u else 1u
        if (open) {
            add(valve)
            release += valve.rate * (maxTime - time)
        }
        remainingLength--
    }
    return Pair(release, result)
}

private fun bfs(start :Valve, goal :Valve, remainingTime :UInt, useless :Set<Valve>) :List<Valve>? {
    val opens = mutableListOf(Pair(start, listOf(start)))
    var cutoff = remainingTime.toInt()
    var candidate :List<Valve>? = null
    while (opens.isNotEmpty()) {
        val (valve, prefix) = opens.removeAt(0)
        if (prefix.size+1>cutoff)
            continue
        val closed = prefix.toSet()
        for (neighbor in valve.neighbors) if (neighbor !in closed) {
            val list = prefix + listOf(neighbor)
            if (neighbor==goal) {
                cutoff = list.size
                if (candidate==null || candidate.myCompare(list, useless)<0)
                    candidate = list
            }
            opens.add(Pair(neighbor, list))
        }
    }
    return candidate
}

private fun <E :Comparable<E>> List<E>.myCompare(other :List<E>, useless :Set<E>) :Int {
    if (this==other) return 0
    val aas = filter { it !in useless }.mapIndexed { n, e -> Pair(e,n) }.sortedByDescending { p -> p.first }
    val bs = other.filter { it !in useless }.mapIndexed { n, e -> Pair(e, n) }.sortedByDescending { p -> p.first }
    for ((a, b) in aas.zip(bs)) {
        if (a.first!=b.first)
            return a.first.compareTo(b.first)
        if (a.second!=b.second)
            return -a.second.compareTo(b.second)
    }
    return 0
}

private fun parseValves(input :InputStream) :Map<String, Valve> {
    val result = mutableMapOf<String, Valve>()
    input.toReader().forEachLine { line ->
        val valve = Valve.parse(line, result)
        if (valve!=null) {
            result[valve.name] = valve
            valve.neighbors.forEach { it.neighbors.add(valve) }
        }
    }
    return result
}

private data class Valve(val name :String, val rate :UInt, val neighbors :MutableSet<Valve> =mutableSetOf())
        :Comparable<Valve> {

    companion object {
        fun parse(line :String, others :Map<String,Valve>) :Valve? {
            val input = line.trim().split("\\s+".toRegex())
            if (input.size<9||input[0]!="Valve"||input[2]!="has"||input[3]!="flow"||!input[4].startsWith("rate=")) {
                println("Wrong structure first half of line: ${input.subList(0, 5).joinToString(" ")}")
                return null
            }
            if(!input[5].startsWith("tunnel")||!input[6].startsWith("lead")||input[7]!="to"||!input[8].startsWith("valve")) {
                println("Wrong structure in second half of line: ${input.subList(5, input.size).joinToString(" ")}")
                return null
            }
            val rate = input[4].substring(5, input[4].length-1).toUIntOrNull()
            if (rate==null) {
                println("Error parsing the flow rate from '${input[4]}'.")
                return null
            }
            val result = Valve(input[1], rate)
            input.subList(9, input.size).forEach { segment ->
                val name = segment.trim(',')
                others[name]?.let{ other -> result.neighbors.add(other) }
            }
            return result
        }
    }

    override fun toString() = "Valve $name has flow rate=$rate; tunnels lead to valves " +
            neighbors.joinToString { it.name }

    override fun compareTo(other :Valve) = rate.compareTo(other.rate)

    override fun equals(other :Any?) :Boolean {
        if (other !is Valve) return false
        return name==other.name && rate==other.rate
    }

    override fun hashCode() = rate.hashCode() +31*name.hashCode()
}
