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

    @Test fun courses() {
        val dm = listOf("Deutsch", "Mathe").map { Subject(it, Importance.Major) }
        val sciences = listOf("Physik", "Biologie", "Informatik", "Chemie")
        val humanities = listOf("W & R", "Geografie", "Sozialkunde")
        val languages = listOf("English", "Espanol", "Francais", "Turkiye", "Chinesisch")
        val am = listOf("Kunsterziehung", "Musik")
        val size = 60
        val students = createStudentsWithPreferences(size, dm, 0.3, sciences, 0.3, humanities, 0.25, languages, am)

        var subjects = students.groupBySubject().entries.toList()
        subjects = (subjects.filter { it.key.importance==Importance.Major }.sortedByDescending { it.value }.take(2+4)
                +subjects.filter { it.key.importance==Importance.Minor }.sortedByDescending { it.value }.take(10))

        val distribution :Map<Subject, MutableSet<Student>> =
            students.collectDistributionNormalizePreferences(
                subjects.map { it.key }, dm, sciences, humanities, languages, am)

        print("Angebotene ${subjects.size} Kurse: "+ distribution.entries.joinToString{ it.key.toString()+" "+it.value.size.toString() })

        val corr :Map<Subject, Map<Subject, UInt>> = students.correlations()
        println("\nCorrelation matrix:")
        val rows = subjects.map { (s :Subject, _ :UInt) -> Pair(s, corr[s]) }
        println("\t\t\t"+ (1u..subjects.size.toUInt()).joinToString { pad(it) })
        println((-1..subjects.size).joinToString("") { "-----" })
        for (row in rows) {
            val r :Map<Subject, UInt> = row.second!!
            val sorted = subjects.map { r[it.key] }
            println(row.first.toString().slice(0..7) +"\t"+ sorted.joinToString { pad(it ?: 0u) })
        }
    }

    private fun createStudentsWithPreferences(
        size :Int,
        dm :List<Subject>,
        p0 :Double, sciences :List<String>,
        p1 :Double, humanities :List<String>,
        p2 :Double, languages :List<String>,
        am :List<String>
    ) :List<Student> = (1..size).map { number ->
        val preferences = mutableListOf<Subject>()
        val (type :Int, bias :Double) = when (random.nextDouble()) {
            in 0.0..p0 -> {
                preferences.add(Subject(sciences.poisson(random), Importance.Major))
                Pair(0, if (preferences.first().name=="Physik") 0.0  else 1.0/3)
            }

            in p0..(p0 + p1) -> {
                preferences.add(Subject(humanities.poisson(random), Importance.Major))
                Pair(1, 2.0/3)
            }

            in (p0 + p1)..(p0 + p1 + p2) -> {
                preferences.add(Subject(languages.poisson(random), Importance.Major))
                Pair(2, 3.0/4)
            }

            else -> {
                preferences.add(Subject(am.random(random), Importance.Major))
                Pair(3, 3.0/4)
            }
        }
        if (random.flipCoin(bias)) {
            preferences.add(dm.first())
            preferences.add(dm.last().copy(importance= Importance.Minor))
        } else {
            preferences.add(dm.last())
            preferences.add(dm.first().copy(importance= Importance.Minor))
        }
        preferences.addType(type==0, sciences)
        preferences.addType(type==1, humanities)
        preferences.addType(type==2, languages)
        if (type != 3) preferences.add(
            Subject(
                if (random.flipCoin(2.0/3)) am.first() else am.last(),
                Importance.Minor
            )
        ) else preferences.add(Subject((humanities + languages).random(random), Importance.Minor))
        Student("Student$number", preferences= preferences)
    }

    private fun MutableList<Subject>.addType(specific :Boolean, subjects :List<String>) {
        add(Subject(
            if (specific) {
                var s = subjects.random(random)
                while (s == first().name)
                    s = subjects.random(random)
                s
            } else
                subjects.random(random)
            ,
            Importance.Minor
        ))
    }
}

fun pad(number :UInt, digits :Int =2) :String = " "+ (number.toString().padStart(digits, '0'))
