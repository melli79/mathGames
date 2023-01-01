package trivia

import kotlin.test.*

class PalindromeTester {
    @Test fun trueForBlank() {
        assertTrue(isPalindrome(".? !"))
    }

    @Test fun falseFor2differentLetters() {
        assertFalse(isPalindrome("a,B"))
    }

    @Test fun trueForSingleLetter() {
        assertTrue(isPalindrome("A,.!"))
    }

    @Test fun trueForSentence() {
        assertTrue(isPalindrome("Cigar? Toss it in a can! It is so tragic."))
    }
}
