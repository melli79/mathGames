package trivia

fun computeMinFillTime(volumes :List<UInt>) :UInt {
    var result = volumes.firstOrNull() ?: 0u
    var support = 0u
    for (v in volumes) {
        support++
        if (v>result) {
            result += (v-result+support-1u)/support
        }
    }
    return result
}

fun computeMinFillPipes(volumes :List<UInt>, time :UInt) :Int {
    var result = 0u
    var avail = 0u // maximally available additional pipes
    var excess = 0u
    for (v in volumes) {
        avail++
        if (v>excess) {
            if (v-excess > avail*time)
                return -1
            val new = (v-excess +time-1u)/time
            excess += time*new -v
            avail -= new
            result += new
        }else {
            excess -= v
        }
    }
    return result.toInt()
}
