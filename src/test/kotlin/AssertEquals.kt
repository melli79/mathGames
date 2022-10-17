fun assertEquals(expected :List<Double>, actual :Iterable<Double>, epsilon :Double =1e-12) {
    expected.zip(actual).forEachIndexed { i, p -> kotlin.test.assertEquals(p.first, p.second, epsilon, "Difference at position $i: expected:${p.first}, actual:${p.second}") }
    kotlin.test.assertEquals(expected.size, actual.count())
}