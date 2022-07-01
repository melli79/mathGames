package partitions

fun partitions(sum :UInt, prefix :List<UInt> =emptyList()) :Set<List<UInt>> {
    if (sum==0u)
        return emptySet()
    val result = mutableSetOf<List<UInt>>()
    val start = prefix.lastOrNull() ?: 1u
    for (s1 in start..sum) {
        val r1 = sum-s1
        if (r1<s1) {
            result.add(prefix+ listOf(sum))
            break
        }
        result += partitions(r1, prefix+listOf(s1))
    }
    return result
}
