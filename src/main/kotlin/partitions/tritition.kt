package partitions

fun trititions(input :Collection<Int>, sum :Int) :Set<Triple<Int, Int, Int>> {
    val result = mutableSetOf<Triple<Int, Int, Int>>()
    val options = input.toSet().sorted()
    options.forEachIndexed { i, s1 ->
        val r1 = sum-s1
        if (r1>=s1) {
            for (s2 in options.subList(i, options.size)) {
                val s3 = r1-s2
                if (s3>=s2 && s3 in options) {
                    result.add(Triple(s1, s2, s3))
                }
            }
        }
    }
    return result
}
