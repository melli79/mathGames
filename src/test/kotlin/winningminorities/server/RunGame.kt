package winningminorities.server

import assertEquals
import winningminorities.Server
import winningminorities.optimist
import winningminorities.pessimist
import kotlin.test.*

object RunGame {
    @Test fun runSmallGame() {
        val server = Server(listOf(optimist, pessimist))
        server.numRounds = 10_000
        server.reset()

        val result = server.runGame()
        server.describeGame()

        assertEquals(10_000, result.first.size)
        result.first.take(100).forEach { p -> assertEquals(Pair(1u, 1u), p) }
        assertEquals(listOf(10_000.0, -10_000.0), result.second)
    }
}
