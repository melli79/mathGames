package numberthy

import kotlin.test.*

class ReflectiveNumberTester {
    @Test fun reflectiveDigits() {
        val result = generateReflectives(1u)
        println(result)
        assertEquals(listOf("0", "1", "2", "5", "8"), result)
    }

    @Test fun reflective2Digits() {
        val result = generateReflectives(2u)
        println(result)
        assertEquals(listOf("11", "22", "55", "69", "88", "96"), result)
    }

    @Test fun reflective3Digits() {
        val result = generateReflectives(3u)
        println(result)
        assertEquals(listOf("101", "111", "121", "151", "181", "202", "212", "222", "252", "282", "505", "515", "525", "555", "585",
            "609", "619", "629", "659", "689", "808", "818", "828", "858", "888", "906", "916", "926", "956", "986"), result)
    }

    @Test fun from1til9() {
        val result = generateReflectives(1uL, 9uL)
        println(result)
        assertEquals(listOf("1", "2", "5", "8"), result)
    }

    @Test fun from0to10() {
        val result = generateReflectives(0uL, 10uL)
        println(result)
        assertEquals(listOf("0", "1", "2", "5", "8"), result)
    }

    @Test fun from1to10() {
        val result = generateReflectives(1uL, 10uL)
        println(result)
        assertEquals(listOf("1", "2", "5", "8"), result)
    }

    @Test fun from9to12() {
        val result = generateReflectives(9uL, 12uL)
        println(result)
        assertEquals(listOf("11"), result)
    }

    @Test fun from1to100() {
        val result = generateReflectives(1uL, 100uL)
        println(result)
        assertEquals(listOf("1", "2", "5", "8", "11", "22", "55", "69", "88", "96"), result)
    }

    @Test fun from10to100() {
        val result = generateReflectives(10uL, 100uL)
        println(result)
        assertEquals(listOf("11", "22", "55", "69", "88", "96"), result)
    }
}
