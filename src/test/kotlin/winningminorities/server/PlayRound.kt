package winningminorities.server

import io.mockk.mockkObject
import io.mockk.verify
import winningminorities.Player
import winningminorities.Server
import winningminorities.optimist
import winningminorities.pessimist
import kotlin.test.*

object PlayRound {
    @Test fun optimistAgainstPessimist() {
        mockkObject(optimist)
        mockkObject(pessimist)
        val server = Server(listOf(optimist, pessimist))
        server.reset()

        val result = server.playRound()
        assertEquals(Pair(1u,1u), result.first)
        assertEquals(listOf(Player.Win(1.0), Player.Loss(1.0)), result.second)

        verify{
            optimist.reset(2u)
            optimist.reward(Player.Win(1.0), 2u)
        }
        verify {
            pessimist.reset(2u)
            pessimist.reward(Player.Loss(1.0), 2u)
        }
    }

    @Test fun twoOptimistsAgainstPessimist() {
        val server = Server(listOf(optimist, optimist, pessimist))
        server.reset()

        val result = server.playRound()
        assertEquals(Pair(2u, 1u), result.first)
        assertEquals(listOf(Player.Loss(2.0/3), Player.Loss(2.0/3), Player.Win(4.0/3)), result.second)
    }
}
