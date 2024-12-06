package xmasaoc

data class DiGraph(val edges :MutableMap<Int, MutableSet<Int>> = mutableMapOf()) {
  override fun toString() = edges.entries.joinToString("\n") { (s, ts) ->
      ts.joinToString { t -> "$s|$t" }
  }

  fun hasEdge(s :Int, t :Int) = edges[s]?.contains(t) ?: false

  fun addEdge(s :Int, t :Int) :Boolean {
    val es = edges.computeIfAbsent(s){ mutableSetOf() }
    return es.add(t)
  }
}

fun readGraph(input :Iterator<String>) :DiGraph {
  val result = DiGraph()
  while (true) {
    val line = input.next()
    if (line.isBlank()) break
    val numbers = line.trim().split("|")
    try {
      val s = numbers[0].toInt();  val t = numbers[1].toInt()
      result.addEdge(s, t)
    } catch (_ :NumberFormatException) {
      println("error on input line: '$line'")
    }
  }
  return result
}

fun DiGraph.validate(numbers :List<Int>) :Int {
  for (i in 0..< numbers.size-1) {
    val s = numbers[i]
    for (j in i+1..< numbers.size) {
      val t = numbers[j]
      if (hasEdge(t,s)) {
        // println("$s, ..., $t are in wrong order.")
        return 0
      }
    }
  }
  return numbers[numbers.size/2]
}

fun DiGraph.sort(numbers :List<Int>) :List<Int> {
  return numbers.sortedWith { s :Int, t :Int ->
    if (hasEdge(s,t)) -1
    else if (hasEdge(t,s)) 1
    else 0
  }
}

fun main() {
  // val input = listOf(
  //   "47|53",
  //   "97|13",
  //   "97|61",
  //   "97|47",
  //   "75|29",
  //   "61|13",
  //   "75|53",
  //   "29|13",
  //   "97|29",
  //   "53|29",
  //   "61|53",
  //   "97|53",
  //   "61|29",
  //   "47|13",
  //   "75|47",
  //   "97|75",
  //   "47|61",
  //   "75|61",
  //   "47|29",
  //   "75|13",
  //   "53|13",
  //   "",
  //   "75,47,61,53,29",
  //   "97,61,53,29,13",
  //   "75,29,13",
  //   "75,97,47,61,53",
  //   "61,13,29",
  //   "97,13,75,29,47"
  // ).stream().iterator()
  val input = getInput(null, "2405manual.txt").toReader().lines().iterator()
  val g = readGraph(input)
  // println("parsed graph: $g")
  val manuals = input.asSequence().map { it.split(",").map { it.toInt() } }.toList()
  val sum = manuals.sumOf { s -> g.validate(s) }
  println("\nThe sum of the middle numbers of the valid manual sequences is $sum.")
  val badManuals = manuals.filter { g.validate(it)==0 }
  var sum2 = 0L
  for (m in badManuals) {
    val sorted = g.sort(m)
    println(sorted)
    sum2 += sorted[sorted.size/2]
  }
  println("\n Sum of middle numbers of resorted manuals is $sum2.")
}

