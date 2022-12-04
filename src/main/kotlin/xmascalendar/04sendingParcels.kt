package xmascalendar

import java.lang.System.currentTimeMillis
import kotlin.random.Random

val random = Random(currentTimeMillis())

fun main() {
    val (successes, failures) = findCandidates()
    harden(successes)
    println("failures: "+failures.entries.joinToString(".\n") { (numGoods, numGifts) ->
        "$numGoods failed for $numGifts gifts"
    })
}

private fun harden(successes :Set<UInt>) {
    for (numGoods in successes) for (numGifts in numGoods..3u* numGoods) {
        val pattern = Gifts.initRatio(numGifts, numGoods)
        repeat(numGifts.toInt()) {
            val gifts = Gifts(pattern.shuffled(random), Gifts::firstChoice)
            gifts.evolve()
            if (!gifts.isAllSent) {
                println("Later failure for $numGifts.")
            }
        }
    }
}

private fun findCandidates() :Pair<Set<UInt>, Map<UInt, UInt>> {
    val successes = mutableSetOf<UInt>()
    val failures = mutableMapOf<UInt, UInt>()
    for (numGifts in (1u..20u)) for (numGoods in 0u..numGifts) {
        val gifts = Gifts(Gifts.initRatio(numGifts, numGoods), Gifts::firstChoice)
        gifts.evolve()
        if (gifts.isAllSent) {
            if (successes.add(numGoods))
                println("$numGoods in $numGifts gifts.")
        } else
            failures.put(numGoods, numGifts)
    }
    return Pair(successes, failures)
}

class Gifts(states0 :List<State?> = initRandom(10u), val choose :(options :List<Pair<Int, State?>>)->Int) {

    companion object {
        fun initAlternating(numGifts :UInt) :List<State?> =
            (1u..numGifts/2u).flatMap { listOf(State.GOOD, State.DEFECTIVE) } +
                    if (numGifts%2u==0u) emptyList()  else listOf(State.GOOD)

        fun firstChoice(options :List<Pair<Int, State?>>) = options.first().first

        fun initRandom(numGifts :UInt) = (1u..numGifts)
            .map { random.nextState() }.toList<State?>()

        fun randomChoice(options :List<Pair<Int, State?>>) :Int {
            if (options.size != 1)
                println("multiple options: ${options.size}")
            return random.of(options)!!.first
        }

        fun Random.nextState() = if (nextBoolean()) State.GOOD  else State.DEFECTIVE

        fun initRatio(numGifts :UInt, numGoods :UInt = numGifts/2u) :List<State?> =
            (1u..numGoods).map { State.GOOD } +
                (numGoods+1u..numGifts).map { State.DEFECTIVE }
    }

    private val states = states0.toMutableList()

    val isAllSent :Boolean
        get() = states.all { it==null }

    override fun toString() = states.joinToString { it.toString() }

    fun mutate() :Boolean {
        val options = states.mapIndexed { i, state -> Pair(i, state) }.filter { it.second == State.GOOD }
        if (options.isEmpty())
            return true
        val choice = choose(options)
        states[choice] = null
        if (choice > 0)
            states[choice-1] = states[choice-1]?.not()
        if (choice < states.size-1)
            states[choice+1] = states[choice+1]?.not()
        return false
    }

    fun evolve() {
        while (true)
            if (mutate()) break
    }

    enum class State {
        GOOD, DEFECTIVE;

        operator fun not() = when(this) {
            GOOD -> DEFECTIVE
            DEFECTIVE -> GOOD
        }
    }
}

fun <E> Random.of(options :List<E>) :E? = if (options.isEmpty())
    null
  else
    options[nextInt(options.size)]
