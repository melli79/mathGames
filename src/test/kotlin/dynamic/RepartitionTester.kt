package dynamic

import kotlin.test.*

class RepartitionTester {
    @Test fun one() {
        val result = repartition(4u, 4u, 1u)
        println("Single round: ${result.first().first()}")
        assertEquals(listOf(listOf((0u until 4u).toSet())), result)
    }

    @Test fun pussiTournament() {
        val result = repartition(16u, 4u, 5u)
        println("Pussi tournament: "+ (result.mapIndexed { r, groups -> "Round ${r+1}: "+groups.joinToString() }.joinToString("\n")))
    }

    @Test fun largeTournament() {
        val result = repartition(64u, 4u, 9u)
        println("Large tournament: "+ (result.mapIndexed { r, groups -> "Round ${r+1}: "+groups.joinToString() }.joinToString("\n")))
    }

    @Test fun extendedTournament() {
        val result = repartition(20u, 4u, 6u)
        println("Large tournament: "+ (result.mapIndexed { r, groups -> "Round ${r+1}: "+groups.joinToString() }.joinToString("\n")))
    }

    @Test fun biggerTournament() {
        val result = repartition(24u, 4u, 7u)
        println("Large tournament: "+ (result.mapIndexed { r, groups -> "Round ${r+1}: "+groups.joinToString() }.joinToString("\n")))
    }
}
