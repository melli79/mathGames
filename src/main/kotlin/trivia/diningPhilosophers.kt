@file:OptIn(ExperimentalUnsignedTypes::class)
package trivia

/**
 * There are n philosophers sitting in a circle and n forks, one between each 2 philosophers.
 * A philosopher can: pick up the left fork (if it is still there), pick up the right fork (-"-),
 * eat for one cycle if he has both forks.  When he is finished, he will return both forks.
 *
 * You are to find an algorithm that each philosopher can follow such that
 * 1. No philosopher starves,
 * 2. every philosopher has the almost same eating time.
 */

enum class Handed {
    NOBODY, LEFT, RIGHT;
}

enum class State {
    WAIT, TAKE_LEFT, TAKE_RIGHT, EAT, RELEASE_RIGHT, RELEASE_LEFT;
}

fun dine(n :UShort) :UIntArray {
    val forks = Array(n.toInt()) { Handed.NOBODY }
    val phil2food = UIntArray(n.toInt()) { 0u }
    val phil2state = Array(n.toInt()) { State.WAIT }
    repeat (131_072) {
        val hunger = phil2food.mapIndexed { p :Int, food :UInt -> Pair(p.toUShort(), food) }
            .groupBy { it.second }.map { Pair(it.key, it.value.map { it.first }) }
            .sortedBy { it.first }
        val dinner = hunger.flatMap { it.second }
        var changed = dinner.freeResources(phil2state, forks, false)
        changed = dinner.prepareEating(phil2state, forks, phil2food, n) || changed
        check(changed) { "Deadlock!" }
    }
    return phil2food
}

private fun List<UShort>.prepareEating(
    phil2state :Array<State>,
    forks :Array<Handed>,
    phil2food :UIntArray,
    n :UShort,
) :Boolean {
    var result = false
    for (p in this) {
        val pair = preparePhilosopher(phil2state[p.toInt()], forks, p, n, phil2food)
        result = result || pair.second
        phil2state[p.toInt()] = pair.first
    }
    return result
}

private fun preparePhilosopher(
    s :State,
    forks :Array<Handed>,
    p :UShort,
    n :UShort,
    phil2food :UIntArray
) :Pair<State, Boolean> {
    var changed = false
    val state = when (s) {
        State.WAIT -> {
            if (forks[((p + (n-1u)) % n).toInt()] != Handed.LEFT && forks[((p + 1u) % n).toInt()] != Handed.RIGHT) {
                if (forks[((p + (n-1u)) % n).toInt()] != Handed.RIGHT) {
                    forks.reserve(p, Handed.LEFT)
                    changed = true
                }
                if (forks[((p + 1u) % n).toInt()] != Handed.LEFT) {
                    forks.reserve(p, Handed.RIGHT)
                    changed = true
                }
                println("$p takes his left fork.")
                State.TAKE_LEFT
            } else
                State.WAIT
        }

        State.TAKE_LEFT -> {
            changed = true
            println("$p takes his right fork.")
            State.TAKE_RIGHT
        }

        State.TAKE_RIGHT -> {
            phil2food.eat(p)
            changed = true
            State.EAT
        }

        else -> s
    }
    return Pair(state, changed)
}

private fun List<UShort>.freeResources(
    phil2state :Array<State>,
    forks :Array<Handed>,
    changed :Boolean
) :Boolean {
    var result = changed
    for (p in reversed()) {
        phil2state[p.toInt()] = when (phil2state[p.toInt()]) {
            State.EAT -> {
                forks.release(p, Handed.LEFT)
                result = true
                State.RELEASE_LEFT
            }

            State.RELEASE_LEFT -> {
                forks.release(p, Handed.RIGHT)
                result = true
                State.RELEASE_RIGHT
            }

            State.RELEASE_RIGHT -> {
                result = true
                State.WAIT
            }

            else -> phil2state[p.toInt()]
        }
    }
    return result
}

private fun UIntArray.eat(p :UShort) {
    this[p.toInt()]++
    println("philosopher $p eats one portion: ${this[p.toInt()]}")
}

fun Array<Handed>.reserve(p :UShort, side :Handed) = when (side) {
    Handed.LEFT -> {
        val f = (p.toInt() + size-1) % size
        check(this[f]==Handed.NOBODY) { "Resource busy" }
        this[f] = Handed.RIGHT
    }
    Handed.RIGHT -> {
        val f = (p.toInt() + 1) % size
        check(this[f]==Handed.NOBODY) { "Resource busy" }
        this[f] = Handed.LEFT
    }
    else -> {/* nothing to do */}
}

private fun Array<Handed>.release(p :UShort, side :Handed) = when (side) {
    Handed.LEFT -> {
        val f = (p.toInt() + size-1) % size
        println("$p releases his left fork.")
        check(this[f]==Handed.RIGHT) { "Resource not owned" }
        this[f] = Handed.NOBODY
    }
    Handed.RIGHT -> {
        println("$p releases his right fork.")
        val f = (p.toInt() + 1) % size
        check(this[f]==Handed.LEFT) { "Resource not owned" }
        this[f] = Handed.NOBODY
    }
    else -> {/* nothing to do */}
}

fun main() {
    val n :UShort = 5u
    val eaten = dine(n)
    val m = eaten.min();  val M = eaten.max()
    check(M-m<=1u) { "Philosopher ${eaten.indexOf(m)} was overreached." }
    println("All $n philosophers got their share.")
}
