package dynamic
/** P and S play a game
 * two integers 1 < x < y < 100, and S knows x+y, P knows x*y
 * 1. S says: P cannot know (x,y)
 * 2. P says: now I know (x,y)
 * 3. S says: now I know (x,y) too
 *
 * What are x and y?
 */

class Sp(val limit :UInt =100u) {
    val s2xy :Map<UInt, List<Pair<UInt, UInt>>>
    val p2xy :Map<UInt, List<Pair<UInt, UInt>>>

    init {
        val s2xy = mutableMapOf<UInt, MutableList<Pair<UInt, UInt>>>()
        val p2xy = mutableMapOf<UInt, MutableList<Pair<UInt, UInt>>>()
        for (x in 2u..<limit) for (y in x+1u..limit) {
            val xys = s2xy.getOrPut(x+y) { mutableListOf() }
            xys.add(Pair(x, y))
            val pxys = p2xy.getOrPut(x*y) { mutableListOf() }
            pxys.add(Pair(x, y))
        }
        this.s2xy = s2xy
        this.p2xy = p2xy
    }

    private fun computeCleanS() :Set<UInt> {
        val result = mutableSetOf<UInt>()
        outer@ for ((s, xys) in s2xy) if (xys.size > 1) { // 0. S does not know (x,y) yet
            for (xy in xys) {
                val p = xy.first*xy.second
                if (p2xy[p]!!.size == 1)
                    continue@outer
            }
            // 1. P cannot know (x,y)
            result.add(s)
        }
        return result
    }

    private fun computeGoodP(cleanS :Set<UInt>) :Set<UInt> {
        val result = mutableSetOf<UInt>()
        for (s in cleanS) for (xy in s2xy[s]!!) {
            print("(${xy.first},${xy.second}): ")
            val p = xy.first*xy.second
            print("s=$s, p=$p, ")
            // 1. P cannot know (x,y)
            var numS = 0u
            for (xy2 in p2xy[p]!!) {
                val s2 = xy2.first + xy2.second
                if (s2 in cleanS) {
                    numS++
                }
            }
            if (numS != 1u) {
                println("fail 2: P cannot know (x,y).")
                continue
            }
            // 2. P knows (x,y)
            result.add(p)
        }
        return result
    }

    fun findSP() {
        val cleanS = computeCleanS()
        val goodP = computeGoodP(cleanS)
        println("\n")
        var numSoln = 0u
        for (s in cleanS) for (xy in s2xy[s]!!) {
            // 1. P cannot know (x,y)
            val p = xy.first * xy.second
            if (p in goodP) {
                // 2. P knows (x,y)
                print("(${xy.first},${xy.second}): s=$s, p=$p, ")
                var numP = 0
                for (xy2 in s2xy[s]!!) {
                    val p2 = xy2.first*xy2.second
                    if (p2 in goodP)
                        numP++
                }
                if (numP!=1) {
                    println("fail 3: S cannot know (x,y).")
                } else {
                    numSoln++
                    println("success! +++")
                }
            }
        }
        println("\nThere are $numSoln solutions.")
    }
}

fun main() = Sp(100u).findSP()
