package racing

fun raceTop10(numRacers :UInt, events :List<Event>) :List<UInt> {
    return race(numRacers, events).take(10)
}

fun raceOf(numRacers :UInt, events :List<Event>, racer :UInt) :UInt {
    return (race(numRacers, events).indexOf(racer) +1).toUInt()
}

internal fun race(numRacers :UInt, events :List<Event>) :List<UInt> {
    val positions = (1u..numRacers).toMutableList()
    val failures = mutableListOf<UInt>()
    for (event in events)
        event.handle(positions, failures)
    return positions + failures.reversed()
}

internal fun Event.handle(
    positions: MutableList<UInt>,
    failures: MutableList<UInt>
) {
    when (this) {
        is Event.Overtake -> {
            val pos = positions.indexOf(racer)
            if (pos >= 1)
                positions.swap(pos, pos - 1)
        }

        is Event.Incident -> {
            if (positions.remove(racer))
                failures.add(racer)
        }

        is Event.Refuel -> {
            if (positions.remove(racer)) {
                val pos = positions.indexOf(reentrant)
                if (pos>=0)
                    positions.add(pos+1, racer)
                else
                    positions.add(racer)
            }
        }
    }
}

fun <E> MutableList<E>.swap(pos: Int, pos2: Int) {
    if (pos==pos2)  return
    val tmp = get(pos)
    set(pos, get(pos2))
    set(pos2, tmp)
}

sealed interface Event {
    val racer :UInt

    data class Overtake(override val racer :UInt) :Event {}
    data class Incident(override val racer: UInt) :Event {}
    data class Refuel(override val racer: UInt, val reentrant :UInt) :Event {}
}
