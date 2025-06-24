package factorgame

fun main() {
    val server = Server(10u)
//    if (random.nextBoolean()) {
        val player = StrategicPlayer()
        server.add(player)
        server.add(player)
//    } else {
//        server.add(HumanPlayer("Melli"))
//        server.add(StrategicPlayer())
//    }
    while (true) {
        server.runGame()
        player.showWinningStrategy()
        println("Press <Ctrl>+<C> to abort.")
    }
}
