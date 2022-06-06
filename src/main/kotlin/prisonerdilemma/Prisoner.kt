package prisonerdilemma

interface Prisoner {
    fun restart()
    fun choose() :Choice
    fun reward(r :Reward)
    fun finish(winnings :Int, numRounds :Int)

    enum class Choice {
        TRICK, COOPERATE, QUIT;
    }

    interface Reward {
        val value :Int
    }
    enum class KnownReward(override val value :Int) :Reward {
        LOSE(0), JOIN(3), WIN(5)
    }
    class Quit(override val value :Int) :Reward {}
}
