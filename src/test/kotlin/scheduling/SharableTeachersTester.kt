package scheduling

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class SharableTeachersTester {
    @Test
    fun shareBetweenSections() {
        val s = Schedule()
        val sports = Item("Sports")
        s.addResourceLimit(SharableTeachers(sports.subject))
        s.schedule(listOf(sports))
        s.schedule(listOf(sports.copy(section = 2u)))

        assertEquals(1, s.days.size)
        val day = s.days.first()
        assertEquals(1, day.slots.size)
        val slot = day.slots[0].items.sortedBy { it.section }
        assertEquals(2, slot.size)
        assertNotEquals(slot.first().section, slot.last().section)
    }

    @Test fun dontShareBetweenGrades() {
        val s = Schedule()
        val sports = Item("Sports")
        s.addResourceLimit(SharableTeachers(sports.subject))
        s.schedule(listOf(sports.copy(grade= 1u)))
        s.schedule(listOf(sports.copy(grade= 2u)))

        assertEquals(1, s.days.size)
        val day = s.days.first()
        assertEquals(2, day.slots.size)
        val slot1 = day.slots.first().items.sortedBy { it.section }
        assertEquals(1, slot1.size)
        val slot2 = day.slots.last().items.sortedBy { it.section }
        assertEquals(1, slot2.size)
    }
}
