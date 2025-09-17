package scheduling

import swap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class IntegrationTester {
    @Test
    fun grade4() {
        val s = Schedule()
        s.addResourceLimit(SingleSubjectPerSection())
        s.addConstraint(DoubleLecture())
        val deutsch = Item("Deutsch", grade = 4u)
        val math = Item("Mathe", grade = 4u, duration = 2u)
        val english = Item("English", grade = 4u)
        val sports = Item("Sport", grade = 4u)
        val ethics = Item("Etik", grade = 4u)
        val music = Item("Musik", grade = 4u)
        val art = Item("Kunst", grade = 4u)
        val heimatkunde = Item("Heimatkunde", grade = 4u)
        s.addConstraint(ShortDay())
        s.schedule(listOf(deutsch, math, english, sports))
        s.schedule(listOf(deutsch.forge(), Item("Werken", grade = 4u)))
        s.schedule(
            listOf(
                math.forge(), deutsch.forge(), english.forge(), sports.forge()
            )
        )
        s.schedule(listOf(deutsch.forge(), art))
        s.schedule(
            listOf(
                heimatkunde, music, Item("Schulgarten", grade = 4u),
                heimatkunde.forge(), deutsch.forge(), ethics
            )
        )

        println("4th grade Schedule: ")
        println(s)
        assertEquals(5, s.days.size)
    }

    @Test
    fun twoSections() {
        val s = Schedule()
        s.addResourceLimit(SingleSubjectPerSection())
        s.addConstraint(DoubleLecture())
        val deutsch = Item("Deutsch", grade = 5u)
        val math = Item("Mathe", grade = 5u, duration = 2u)
        val english = Item("English", grade = 5u)
        val sports = Item("Sport", grade = 5u)
        val biology = Item("Biologie", grade = 5u)
        val history = Item("Geschichte", grade = 5u)
        val music = Item("Musik", grade = 5u)
        val art = Item("Kunst", grade = 5u)
        val ethics = Item("Ethik", grade = 5u)
        s.addResourceLimit(SingleTeacher(art.subject))
        s.addResourceLimit(SingleTeacher(music.subject))
        s.addResourceLimit(SingleTeacher(ethics.subject))
        s.addResourceLimit(SingleTeacher(biology.subject))
        s.addResourceLimit(SingleTeacher(history.subject))
        s.addResourceLimit(SingleTeacher(math.subject))
        s.addResourceLimit(SharableTeachers(sports.subject))
        s.addConstraint(MidlongDays())
        val mo5 = listOf(deutsch, math, english, biology)
        s.schedule(mo5)
        s.scheduleForSection(2u, mo5)
        val tu2 = listOf(history, deutsch.forge(), ethics, sports)
        s.schedule(tu2)
        s.scheduleForSection(2u, tu2)
        val we5 = listOf(english.forge(), history.forge(), biology.forge(), ethics.forge())
        s.schedule(we5)
        s.scheduleForSection(2u, we5)
        val th5 = listOf(deutsch.forge(), deutsch.forge(), math.forge(), music, sports.forge())
        s.scheduleForSection(2u, th5)
        s.schedule(th5)
        val fr5 = mutableListOf(art, art.forge(), english.forge(), deutsch.forge(), music.forge())
        s.scheduleForSection(2u, fr5)
        fr5.swap(3, 4)
        s.schedule(fr5)

        println("Schedule grade 5")
        println(s)
        assertEquals(5, s.days.size)
        val mo = s.days.first()
        // MidlongDays
        assertEquals(4, mo.slots.size)
        val tu = s.days[1]
        assertEquals(6, tu.slots.size)
        val we = s.days[2]
        assertEquals(4, we.slots.size)
        val th = s.days[3]
        assertEquals(6, th.slots.size)
        val fr = s.days[4]
        assertEquals(4, fr.slots.size)

        // single subjects per section
        for (day in s.days)
            for (slot in day.slots) {
                val items = slot.items.sortedBy { it.section }
                assertEquals(2, items.size)
                assertNotEquals(items.first(), items.last())
            }
    }
}
