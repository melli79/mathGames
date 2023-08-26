package people

import kotlin.test.*

class PersonTester {
    @Test fun singlePerson() {
        val melli = Person("Grützmann", arrayOf("Melchior"), Gender.MALE, PartialDate(1979u,6u,20u),
            title= "Dr.")
        println("Created $melli.")
    }

    @Test fun parents() {
        val melli = Person("Grützmann", arrayOf("Melchior"), Gender.MALE, PartialDate(1979u,6u,20u))
        melli.parents.add(Person("Grützmann", arrayOf("Wolfgang", "Dieter"), Gender.MALE, PartialDate(1954u), title= "Dr."))
        melli.parents.add(Person("Grützmann", arrayOf("Astrid", "Ilse", "Barbara"), Gender.FEMALE, PartialDate(1955u)))
        println(melli.describe())
    }

    @Test fun spouse() {
        val vati = Person("Grützmann", arrayOf("Wolfgang", "Dieter"), Gender.MALE, PartialDate(1954u), title= "Dr.")
        vati.marry(Person("Kulisch", arrayOf("Astrid", "Ilse", "Barbara"), Gender.FEMALE, PartialDate(1955u)))

        println(vati.describe())
    }

    @Test fun children() {
        val mutti = Person("Kulisch", arrayOf("Astrid", "Ilse", "Barbara"), Gender.FEMALE, PartialDate(1955u))
        Person("Grützmann", arrayOf("Wolfgang", "Dieter"), Gender.MALE, PartialDate(1954u))
            .marry(mutti)
        val melli = mutti.giveBirth(arrayOf("Melchior"), Gender.MALE, PartialDate(1979u,6u,20u))

        println(melli.describe())
    }

    @Test fun siblings() {
        val mutti = Person("Kulisch", arrayOf("Astrid", "Ilse", "Barbara"), Gender.FEMALE, PartialDate(1955u))
        Person("Grützmann", arrayOf("Wolfgang", "Dieter"), Gender.MALE, PartialDate(1954u))
            .marry(mutti)
        val melli = mutti.giveBirth(arrayOf("Melchior"), Gender.MALE, PartialDate(1979u,6u,20u))
        val radi = mutti.giveBirth(arrayOf("Konrad"), Gender.MALE, PartialDate(1981u,7u,22u))

        val siblings = melli.findSiblings()
        println("$melli's siblings are: $siblings")
        assertEquals(setOf(radi), siblings)
    }
}
