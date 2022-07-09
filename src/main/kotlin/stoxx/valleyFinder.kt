package stoxx

fun findValleys(profile :List<Int>) :List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    if (profile.isEmpty())
        return result
    var left = profile.first(); var leftBoundary = 0
    var i = 1
    while (i<profile.size) {
        // going down
        while (i<profile.size && left>=profile[i]) {
            left = profile[i++]
        }
        // going up
        while(i<profile.size && left<=profile[i]) {
            left = profile[i++]
        }
        if (leftBoundary<i-1)
            result.add(Pair(leftBoundary, i-1))
        leftBoundary = i-1
    }
    return result
}
