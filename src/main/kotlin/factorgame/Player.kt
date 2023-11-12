package factorgame

typealias Board = Set<UInt>

enum class Reward {
    WIN, LOSE
}

interface Player {
    val name :String

    fun startGame(limit :UShort) {/* ignore */}
    fun choose(board :Board, lastMove :UInt) :UInt
    fun reward(r :Reward) {/* ignore */}
}
