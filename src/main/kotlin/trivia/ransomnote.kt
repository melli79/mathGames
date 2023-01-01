package trivia

fun ransomNote(magazine :String, message :String) :List<Int>? {
    val result = mutableListOf<Int>()
    val input = magazine.iterator(); var i=0
    val filter = message.iterator()
    while (filter.hasNext()) {
        var c = filter.next()
        while (input.hasNext()) {
            val t = input.next()
            if (c!=t)
                i++
            else {
                result.add(i++)
                c = 0.toChar()
                break
            }
        }
        if (c!=0.toChar())
            return null
    }
    return result
}
