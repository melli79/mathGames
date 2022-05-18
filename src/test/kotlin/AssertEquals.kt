fun assertEquals(expected :List<Double>, actual :Iterable<Double>, epsilon :Double =1e-12) {
    actual.forEachIndexed { i, v -> kotlin.test.assertEquals(expected[i], v, epsilon) }
    kotlin.test.assertEquals(expected.size, actual.count())
}