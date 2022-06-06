package winningminorities

fun main(args: Array<String>) {
    val server = Server(listOf(optimist, pessimist, MoodyPlayer(), AdjustingRandomPlayer(), AdjustingRandomPlayer())
            +(1..10).map { RandomPlayer() }
            +(16..100).map { MultiStrategyPlayer() })
    server.numRounds = 100_000
    server.runGame()
    server.describeGame()
}
