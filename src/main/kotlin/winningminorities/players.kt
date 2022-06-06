package winningminorities

object optimist :Player {
    override fun toString() = "optimist"
    override fun describe() = "Optimist"

    override fun choose(numPlayers: UInt) = Player.Choice.GO

    override fun reward(r: Player.Reward, numPlayers: UInt) {}
    override fun reset(numPlayers: UInt) {}
}

object pessimist :Player {
    override fun toString() = "pessimist"
    override fun describe() = "Pessimist"

    override fun choose(numPlayers: UInt) = Player.Choice.STAY

    override fun reward(r: Player.Reward, numPlayers: UInt) {}
    override fun reset(numPlayers: UInt) {}
}
