package dynamic

import kotlin.test.*

class LevenshteinTester {
  @Test fun empties() {
    assertEquals(emptyList(), levenshtein("", ""))
  }

  @Test fun insertion() {
    assertEquals(listOf(Edit.Insert(0, '0')), levenshtein("", "0"))
  }

  @Test fun deletion() {
    assertEquals(listOf(Edit.Delete(0)), levenshtein("0", ""))
  }

  @Test fun hello() {
    assertEquals(listOf(Edit.Delete(4)), levenshtein("Hello", "Hell"))
  }

  @Test fun allo() {
    assertEquals(listOf(Edit.Insert(0, 'H'), Edit.Insert(5, '!')), levenshtein("allo", "Hallo!"))
  }

  @Test fun melli() {
    println(levenshtein("Melchior", "Melanie"))
  }
}

