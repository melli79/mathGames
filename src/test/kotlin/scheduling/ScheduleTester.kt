package scheduling

import kotlin.test.*

class ScheduleTester {
    @Test fun emptySchedule() {
        val s = Schedule()
        assertEquals(0, s.days.size)
    }

    @Test fun scheduleSingleItem() {
        val s = Schedule()
        val math = Item("Mathe")
        s.schedule(listOf(math))

        assertEquals(1, s.days.size)
        val day = s.days.first()
        assertEquals(1, day.slots.size)
        assertEquals(1, day.slots.first().items.size)
        assertEquals(math, day.slots.first().items.first())
    }

    @Test fun removeItem() {
        val s = Schedule()
        val deutsch = Item("Deutsch")
        s.schedule(listOf(deutsch))
        assertEquals(1, s.days.size)
        val day = s.days.first()
        assertEquals(1, day.slots.size)
        assertEquals(1, day.slots.first().items.size)

        day.remove(deutsch)
        assertEquals(0, day.slots.size)
    }

    @Test fun forgeItems() {
        val math = Item("Mathe")
        val math2 = math.forge()

        assertEquals(math.subject, math2.subject)
        assertEquals(math.grade, math2.grade)
        assertEquals(math.section, math2.section)
        assertEquals(math.duration, math2.duration)
        assertNotEquals(math, math2)
    }

    @Test fun oneItemAtATime() {
        val s = Schedule()
        val math = Item("Mathe")

        s.addResourceLimit(SingleSubject())
        s.schedule(listOf(math, math.forge()))

        assertEquals(1, s.days.size)
        val day = s.days.first()
        assertEquals(2, day.slots.size)
        assertEquals(1, day.slots.first().items.size)
        assertEquals(1, day.slots.last().items.size)
    }

}
