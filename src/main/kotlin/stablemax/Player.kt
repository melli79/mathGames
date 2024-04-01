package stablemax

interface Player {
    fun reset(limit :UInt, numPlayers :UShort) {}
    fun choice(limit: UInt, numPlayers :UShort) :UInt
    fun reward(limit: UInt, numPlayers :UShort, reward :Reward) {}
    fun describe() = toString()
}

enum class Reward(val value :Short) {
    Loss(-2), Overbid(-1), Win(10)
}
