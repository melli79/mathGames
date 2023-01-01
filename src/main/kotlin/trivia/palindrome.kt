package trivia

import java.util.*

fun isPalindrome(text :String) :Boolean {
    val input = text.filter { it.isLetter() }.lowercase(Locale.getDefault())
    for (pos in 0 until input.length/2) {
        if (input[pos]!=input[input.length-1-pos])
            return false
    }
    return true
}