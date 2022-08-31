package factorgame

typealias Board = Set<UInt>

enum class Reward {
    WIN, LOSE
}

interface Player {
    val name :String

    fun startGame(limit :UShort)
    fun choose(board :Board, lastMove :UInt) :UInt
    fun reward(r :Reward)
}

open class BasicStrategy {
    fun findOptions(board :Board, lastMove :UInt) = when (lastMove) {
        0u -> board.filter { o -> o%2u == 0u }.toSet()
        else -> board.filter { it%lastMove==0u || lastMove%it==0u }.toSet()
    }
}
