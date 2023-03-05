package cmdgames

import java.lang.System.currentTimeMillis
import kotlin.random.Random

const val LIMIT = 100
val random = Random(currentTimeMillis())

fun main() {
    while (true) {
        if (if (random.nextBoolean())
                youGuess()
            else
                iGuess())
            break
    }
    println("Thanks for playing the number guess game.")
}

fun iGuess() :Boolean {
    println("\nThink of an integral number between 1 and 100.  I will try to guess it.")
    var m = 1;  var M = 100
    while (m<M) {
        val i = (m+M)/2
        print("Is it $i (<,>,=)? ")
        val input = readlnOrNull()?.trim() ?: return true
        if (input.isEmpty())
            return true
        when (input[0]) {
            'q', 'Q', 'x', 'X' -> return true
            '<', '-' -> M = i-1
            '>', '+' -> m = i+1
            '=', '0' -> {
                println("Wow!  I guessed it.")
                break
            }
            else -> println("I did not understand your answer: '$input'")
        }
    }
    if (M<m)
        println("There is no such number!")
    if (m==M)
        println("So it must be $m, right?")
    return false
}

private fun youGuess() :Boolean {
    val n = random.nextInt(1, LIMIT)
    println("\nGuess an integral number between 1 and 100.")
    while (true) {
        print("Your guess: ")
        val input = readlnOrNull()?.trim()?.toInt() ?: return true
        if (input < 1)
            return true
        val cmp = n.compareTo(input)
        when (cmp) {
            -1 -> println("The number is smaller.")
            1 -> println("The numer is bigger.")
            else -> {
                println("You guessed it: $n")
                break
            }
        }
    }
    return false
}
