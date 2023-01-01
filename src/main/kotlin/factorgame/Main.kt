package factorgame

fun main(args: Array<String>) {
    val server = Server(100u)
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
        println("Press <Ctrl>+<C> to abort.")
        //Thread.sleep(3000)
    }
}
