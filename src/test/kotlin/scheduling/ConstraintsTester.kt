package scheduling

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConstraintsTester {
    @Test
    fun shortDay() {
        val s = Schedule()
        s.addResourceLimit(SingleSubject())
        s.addConstraint(ShortDay())
        val math = Item("Mathe")
        val deutsch = Item("Deutsch")
        s.schedule(listOf(math, deutsch, math.forge(), deutsch.forge(), math.forge()))

        assertTrue(s.days.isNotEmpty())
        val day = s.days.first()
        assertEquals(4, day.slots.size)
        assertEquals(2, s.days.size)
        val otherDay = s.days.last()
        assertEquals(1, otherDay.slots.size)
    }

    @Test
    fun doubleLecture() {
        val s = Schedule()
        val math = Item("Mathe", duration = 2u)

        s.addConstraint(DoubleLecture())
        s.schedule(listOf(math))

        assertEquals(1, s.days.size)
        val day = s.days.first()
        assertEquals(2, day.slots.size)
        assertContentEquals(listOf(math), day.slots.first().items)
        assertContentEquals(listOf(math), day.slots.last().items)
    }

    @Test
    fun singleTeacher() {
        val s = Schedule()
        s.addResourceLimit(SingleTeacher("Mathe"))
        val math = Item("Mathe")
        s.schedule(listOf(math))
        s.schedule(listOf(math.copy(section = 2u)))

        assertEquals(1, s.days.size)
        val day = s.days.first()
        assertEquals(2, day.slots.size)
        val slot1 = day.slots[0]
        assertEquals(1, slot1.items.size)
        val slot2 = day.slots[1]
        assertEquals(1, slot2.items.size)
    }
}
