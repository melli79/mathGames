package xmascalendar

import kotlin.math.abs

private const val threshold = 5u
fun main() {
    val ballots = mapOf(
        Pair('A', setOf('A', 'D', 'G', 'J')), Pair('B', setOf('B')),
        Pair('C', setOf('A', 'B', 'J')), Pair('D', setOf('C', 'E', 'G')),
        Pair('E', setOf('E', 'F', 'H', 'J')), Pair('F', setOf('D', 'G', 'J')),
        Pair('G', setOf('G', 'C', 'E', 'H')), Pair('H', setOf('H', 'D', 'G', 'J')),
        Pair('I', setOf('I', 'C', 'J')), Pair('J', setOf('B', 'E', 'H', 'I'))
    )
    val votes = count(ballots)
    val goods = setOf('A', 'C', 'D', 'E', 'H');  val bads = ballots.keys -goods
    val actions = findActions(votes, goods, bads, ballots)
    val options = actions.flatMap { it.third }.toSet()
    findBribery(options, actions)
}

private fun findBribery(options :Set<Char>, actions :List<Triple<Char, Int, Set<Char>>>) {
    if (tryBribe1elf(options, actions))
        return
    println("Bribing 1 elf is not enough.")
    if (tryBribe2elves(options, actions))
        return
    println("Bribing 2 elves is not enough.")
    if (tryBribe3elves(options, actions))
        return
    println("Bribing 3 elves is not enough.")
    if (!tryBribe4elves(options, actions))
        throw IllegalStateException("Don't know how to do that")
}

private fun findActions(
    votes :Map<Char, UInt>,
    goods :Set<Char>,
    bads :Set<Char>,
    ballots :Map<Char, Set<Char>>
) :List<Triple<Char, Int, Set<Char>>> {
    val actions = votes.entries.map { entry ->
        if (entry.key in goods && entry.value < threshold)
            Pair(entry.key, (threshold - entry.value).toInt())
        else if (entry.key in bads && entry.value >= threshold)
            Pair(entry.key, (entry.value + 1u - threshold).toInt().unaryMinus())
        else
            null
    }.filterNotNull()
        .map { m ->
            if (m.second > 0)
                Triple(m.first, m.second, ballots.entries.filter { en -> m.first !in en.value }.map { it.key }.toSet())
            else
                Triple(m.first, m.second, ballots.entries.filter { en -> m.first in en.value }.map { it.key }.toSet())
        }
    println("We need: " + actions.joinToString(";\n") { "${it.first}: %+1d from %s".format(it.second, it.third) })
    return actions
}

private fun tryBribe4elves(options :Set<Char>, actions :List<Triple<Char, Int, Set<Char>>>) :Boolean {
    var success = false
    val opts = options.mapIndexed { i, c -> Pair(c, i)}
    for ((c1, i1) in opts)  for ((c2, i2) in opts)  if (i1<i2) {
        for ((c3, i3) in opts)  if (i2<i3) {
            outer@ for ((c4, i4) in opts)  if (i3<i4) {
                val action = mutableMapOf(Pair(c1, mutableSetOf<String>()), Pair(c2, mutableSetOf()),
                    Pair(c3, mutableSetOf()), Pair(c4, mutableSetOf()))
                for (a in actions) {
                    val change = abs(a.second)
                    val mod = if (a.second>0) "+"+a.first  else "-"+a.first
                    when (change) {
                        2 -> if (c1 in a.third) {
                                if (!(c1==a.first || c2 in a.third || c3 in a.third || c4 in a.third))
                                    continue@outer
                                else if (c1==a.first)
                                    action[c1]!!.add(mod)
                                else if (c2 in a.third) {
                                    action[c1]!!.add(mod);  action[c2]!!.add(mod)
                                }else if (c3 in a.third) {
                                    action[c1]!!.add(mod);  action[c3]!!.add(mod)
                                } else {
                                    action[c1]!!.add(mod);  action[c4]!!.add(mod)
                                }
                            }else if (c2 in a.third) {
                                if (!(c2==a.first || c3 in a.third || c4 in a.third))
                                    continue@outer
                                else if (c2==a.first)
                                    action[c2]!!.add(mod)
                                else if (c3 in a.third) {
                                    action[c2]!!.add(mod);  action[c3]!!.add(mod)
                                } else {
                                    action[c2]!!.add(mod);  action[c4]!!.add(mod)
                                }
                            } else if (c3 in a.third) {
                                if (!(c3==a.first || c4 in a.third))
                                    continue@outer
                                else if (c3==a.first)
                                    action[c3]!!.add(mod)
                                else {
                                    action[c3]!!.add(mod);  action[c4]!!.add(mod)
                                }
                            } else if (!(c4 in a.third && c4==a.first))
                                continue@outer
                            else
                                action[c4]!!.add(mod)

                        1, 3, 4, 5 -> TODO("to be implemented")

                        else -> return false
                    }
                }
                val choice = action.entries.joinToString(";  ") { "${it.key}: "+it.value.joinToString { it } }
                if (success)
                    println("   or $choice")
                else
                    println("It is sufficient to \nbribe $choice")
                success = true
            }
        }
    }
    return success
}

private fun tryBribe3elves(options :Set<Char>, actions :List<Triple<Char, Int, Set<Char>>>) :Boolean {
    var success = false
    val opts = options.mapIndexed { i, c -> Pair(c, i)}
    for ((c1, i1) in opts) {
        for ((c2, i2) in opts)  if (i1<i2) {
            outer@ for ((c3, i3) in opts)  if (i2 < i3) {
                val action = mutableMapOf(Pair(c1, mutableSetOf<String>()), Pair(c2, mutableSetOf()),
                    Pair(c3, mutableSetOf()))
                for (a in actions) {
                    val change = abs(a.second)
                    val mod = if (a.second>0) "+"+a.first  else "-"+a.first
                    when (change) {
                        1 -> if (!(c1 in a.third || c2 in a.third || c3 in a.third))
                                continue@outer
                            else if (c1 in a.third)
                                action[c1]!!.add(mod)
                            else if (c2 in a.third)
                                action[c2]!!.add(mod)
                            else
                                action[c3]!!.add(mod)

                        2 -> if (c1 in a.third) {
                                if (!(c1==a.first || c2 in a.third || c3 in a.third))
                                    continue@outer
                                else if (c1==a.first)
                                    action[c1]!!.add(mod)
                                else if (c2 in a.third) {
                                    action[c1]!!.add(mod);  action[c2]!!.add(mod)
                                } else {
                                    action[c1]!!.add(mod);  action[c3]!!.add(mod)
                                }
                            }else if (c2 in a.third) {
                                if (!(c2==a.first || c3 in a.third))
                                    continue@outer
                                else if (c2==a.first)
                                    action[c2]!!.add(mod)
                                else {
                                    action[c2]!!.add(mod);  action[c3]!!.add(mod)
                                }
                            } else if (!(c3 in a.third && c3==a.first))
                                continue@outer
                            else
                                action[c3]!!.add(mod)

                        3 -> if (c1 in a.third) {
                                if (c2 in a.third) {
                                    if (!(c1==a.first||c2==a.first|| c3 in a.third))
                                        continue@outer
                                } else if (!(c3 in a.third && (c1==a.first||c3==a.first)))
                                    continue@outer
                            }else if (c2 in a.third && c3 in a.third) {
                                if (!(c2==a.first||c3==a.first))
                                    continue@outer
                            } else
                                continue@outer

                        4 -> if (!(c2 in a.third&&c2 in a.third&&c3 in a.third&&(c1==a.first||c2==a.first||c3==a.first)))
                            continue@outer

                        else -> return false
                    }
                }
                val choice = action.entries.joinToString(";  "){ "${it.key}: "+it.value.joinToString { it } }
                if (success)
                    println("   or $choice")
                else
                    println("It is sufficient to \nbribe $choice.")
                success = true
            }
        }
    }
    return success
}

private fun tryBribe2elves(options :Set<Char>, actions :List<Triple<Char, Int, Set<Char>>>) :Boolean {
    for (c1 in options) {
        outer@ for (c2 in options) if (c1 != c2) {
            for (a in actions) {
                val change = abs(a.second)
                when (change) {
                    1 -> if (!(c1 in a.third || c2 in a.third))
                        continue@outer

                    2 -> if (!((c1 in a.third && c1 == a.first) || (c2 in a.third && c2 == a.first) && (c1 in a.third && c2 in a.third)))
                        continue@outer

                    3 -> if (!(c1 in a.third && c2 in a.third))
                        continue@outer

                    else -> return false
                }
            }
            println("It is sufficient to bribe $c1 and $c2.")
            return true
        }
    }
    return false
}

private fun tryBribe1elf(
    options :Set<Char>,
    actions :List<Triple<Char, Int, Set<Char>>>
) :Boolean {
    outer@ for (c in options) {
        for (a in actions) {
            if (c !in a.third)
                continue@outer
            val change = abs(a.second)
            if (c!=a.first && change>1)
                continue@outer
            if (change > 2)
                continue@outer
        }
        println("Rigging $c is sufficient")
        return true
    }
    return false
}

fun count(ballots :Map<Char, Set<Char>>) :Map<Char, UInt> {
    val result = mutableMapOf<Char, UInt>()
    ballots.forEach { c, votes ->
        votes.forEach { v -> result.inc(v) }
        if (c in votes) result.inc(c)
    }
    return result
}

fun <K> MutableMap<K, UInt>.inc(key :K) :UInt {
    val newValue = getOrDefault(key, 0u) +1u
    set(key, newValue)
    return newValue
}