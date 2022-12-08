package xmascalendar

private val fallbackP = 0.2
private val fallbackDistance = 2
private val end = 5

fun main() {
    var t = 0
    var ps = mapOf(Pair(0,1.0))
    while (t<10) {
        val newP = mutableMapOf<Int, Double>()
        ps.forEach { (x, p) ->
            if (x==end)
                newP[x] = (newP[x] ?: 0.0) +p
            else {
                newP[x+1] = (newP[x+1] ?: 0.0) +(1 -fallbackP) *p
                newP[x -fallbackDistance] = (newP[x -fallbackDistance] ?: 0.0) +fallbackP *p
            }
        }
        ps = newP
        t++
    }
    println(ps.entries.sortedBy { it.key }.joinToString { (x, p) -> "%d: %.4f".format(x, p) })
    println("The probability that the imp will come home by dinner is ${"%.1f".format((ps[end] ?: 0.0)*100)}%.")
}
