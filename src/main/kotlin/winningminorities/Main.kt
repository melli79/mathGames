package winningminorities

fun main(args: Array<String>) {
    val server = Server(listOf(optimist, pessimist, MoodyPlayer()) +(1..10).map { RandomPlayer() }
            +(14..100).map { MultiStrategyPlayer() })
    server.numRounds = 100_000
    server.runGame()
    server.describeGame()
}
