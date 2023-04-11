package racing

import kotlin.test.*

class Formula1Tester {
    @Test fun emptyRace() {
        val result = race(0u, emptyList())
        println("finals: $result")
        assertEquals(emptyList(), result)
    }

    @Test fun eventlessRace() {
        val result = raceTop10(11u, emptyList())
        println("finals: $result")
        assertEquals((1u..10u).toList(), result)
    }

    @Test fun overtake() {
        val result = race(3u, listOf(Event.Overtake(2u)))
        println("finals: $result")
        assertEquals(listOf(2u,1u,3u), result)
    }

    @Test fun incident() {
        val result = race(4u, listOf(Event.Incident(2u)))
        println("finals: $result")
        assertEquals(listOf(1u,3u,4u,2u), result)
    }

    @Test fun refuel() {
        val result = race(4u, listOf(Event.Refuel(1u, 3u)))
        println("finals: $result")
        assertEquals(listOf(2u,3u,1u,4u), result)
    }

    @Test fun raceOf() {
        val place = raceOf(4u, listOf(Event.Overtake(2u), Event.Refuel(2u,3u)), 2u)
        assertEquals(3u, place)
    }
}
