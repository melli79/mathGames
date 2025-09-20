package scheduling
/***
 * Students in Course system
 */

import kotlin.test.*

class CourseTester {
    @Test fun groupStudents() {
        val mathM = Subject("Math", Importance.Major)
        val deutschM = Subject("Deutsch", Importance.Major)
        val englishM = Subject("English", Importance.Major)
        val english = englishM.copy(importance= Importance.Minor)
        val physM = Subject("Phyisk", Importance.Major)
        val infoM = Subject("Informatik", Importance.Major)
        val info = infoM.copy(importance= Importance.Minor)
        val bioM = Subject("Biology", Importance.Major)
        val students = listOf(
            Student(name= "Anton", preferences= mutableListOf(mathM, physM, english, info)),
            Student(name= "Rita", preferences= mutableListOf(mathM, infoM, english)),
            Student(name= "Marcus", preferences= mutableListOf(mathM, physM, english)),
            Student(name= "Claudia", preferences= mutableListOf(deutschM, bioM, english)),
            Student(name= "Joerg", preferences= mutableListOf(deutschM, englishM, info))
        )

        val result = students.groupBySubject()
        println("Course preference counts: $result")

        assertEquals(3u, result[mathM])
        assertEquals(4u, result[english])
        assertEquals(2u, result[deutschM])
        assertEquals(2u, result[physM])
        assertEquals(2u, result[info])
        assertEquals(1u, result[bioM])
        assertEquals(1u, result[infoM])
        assertEquals(1u, result[englishM])
        assertEquals(8, result.size)
    }
}
