package winningminorities

import common.math.approxEquals

interface Player {
    fun describe(): String

    fun choose(numPlayers :UInt) :Choice

    fun reward(r :Reward, numPlayers: UInt)
    fun reset(numPlayers: UInt)

    enum class Choice {
        GO, STAY
    }

    sealed class Reward(val value :Double) {}
    class Win(win :Double) :Reward(win) {
        override fun toString() = "Win($value)"

        override fun equals(other: Any?): Boolean {
            if (other !is Win)  return false
            return approxEquals(value, other.value)
        }

        override fun hashCode() = 1+ value.hashCode()
    }
    class Loss(loss :Double) :Reward(-loss) {
        override fun toString() = "Loss(${-value})"

        override fun equals(other: Any?): Boolean {
            if (other !is Loss)  return false
            return approxEquals(value, other.value)
        }

        override fun hashCode() = 2+ value.hashCode()
    }
}