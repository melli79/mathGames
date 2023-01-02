package trivia

import java.util.*
import kotlin.test.*

class AnagramTester {
    @Test fun equalWords() {
        assertTrue(Anagrams.isAnagram("Hallo", "hallO"))
    }

    @Test fun totallyDifferentWords() {
        assertFalse(Anagrams.isAnagram("Hallo", "world"))
    }

    @Test fun findAllAnagrams() {
        Anagrams.loadWords(Locale.ENGLISH)
        for (word in listOf("an", "libre", "butterfly")) {
            val result = Anagrams.findAnagrams(word)
            println("Anagrams of '$word' are: $result")
            assertTrue(word in result)
        }
    }

    @Test fun findGermanAnagrams() {
        Anagrams.loadWords(Locale.GERMAN)
        for (word in listOf("an", "Tomate", "Haifisch")) {
            val result = Anagrams.findAnagrams(word)
            println("Anagrams of '$word' are: $result")
            assertTrue(word in result)
        }
    }

    @Test fun findGermanPartialAnagrams() {
        Anagrams.loadWords(Locale.GERMAN)
        for (word in listOf("Anna", "Tomate", "Haifisch")) {
            val result = Anagrams.findPartialAnagrams(word)
            println("Partial anagrams of '$word' are: $result")
            assertTrue(word in result)
        }
    }
}
