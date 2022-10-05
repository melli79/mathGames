package common.math

fun factor(n :ULong) :Map<ULong, UByte> {
    if (n<=1uL)
        return emptyMap()
    var n = n
    var p = 2uL
    val result = mutableMapOf<ULong, UByte>()
    while (p*p<=n) {
        var e = 0
        while (n%p==0uL) {
            e++
            n /= p
        }
        if (e>0)
            result[p] = e.toUByte()
        if (p<3uL)
            p++
        else
            p += 2uL
    }
    if (n>1uL)
        result[n] = ((result[n]?: 0u)+1u).toUByte()
    return result
}
