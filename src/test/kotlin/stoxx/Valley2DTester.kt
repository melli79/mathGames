package stoxx

import kotlin.test.*

class Valley2DTester {
    @Test fun empty() {
        assertEquals(emptySet(), findValleys(Profile2D()))
    }

    @Test fun singleValley() {
        val profile = Profile2D(listOf(
            listOf(1,1,1,1),
            listOf(1,0,0,1),
            listOf(1,0,0,1),
            listOf(1,1,1,1)))
        val square = listOf(Triple(0,0,3), Triple(1,0,3),
            Triple(2,0,3), Triple(3,0,3))

        assertEquals(setOf(square), findValleys(profile))
    }

    @Ignore
    @Test fun twoValleys() {
        val profile = Profile2D(listOf(
            listOf(1,1,1,1,1),
            listOf(1,0,1,0,1),
            listOf(1,1,1,1,1)
        ))
        val sq1 = listOf(Triple(0,0,2), Triple(1,0,2), Triple(2,0,2))
        val sq2 = listOf(Triple(0,2,4), Triple(1,2,4), Triple(2,2,4))

        assertEquals(setOf(sq1, sq2), findValleys(profile))
    }
}
