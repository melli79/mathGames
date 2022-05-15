interface Prisoner {
    fun restart()
    fun choose() :Choice
    fun reward(r :Reward)
    fun finish(winnings :Int, numRounds :Int)

    enum class Choice {
        TRICK, COOPERATE;
    }

    enum class Reward(val value :Byte) {
        LOSE(0), JOIN(3), WIN(5)
    }
}
