package geometry

import kotlin.test.*

class PackingTester {
    @Test fun twoBy2Cell() {
        val g = arrayOf(byteArrayOf(Empty, Empty), byteArrayOf(Empty, Empty))
        val start = Pair(0,0);  g[start.first][start.second] = Forbidden
        g[1][1] = Gift
        val gifts = g.flatMapIndexed { r: Int, row: ByteArray -> row.mapIndexed { c, value -> Triple(r,c,value) } }
            .filter { it.third== Gift }
        assertTrue(gifts.all { (r,c,_) -> g.isReachable(start, Pair(r,c)) })
        println("On ${g.size}x${g[0].size} we can position ${gifts.size} gifts at \n"+ g.show())
    }

    @Test fun sevenBy7() {
        val g = (1..7).map { byteArrayOf(Empty,Empty,Empty,Empty, Empty,Empty,Empty) }.toTypedArray()
        listOf(Pair(6,3), Pair(6,5), Pair(4,0),Pair(4,4), Pair(2,2),Pair(2,4), Pair(1,2), Pair(0,4),Pair(0,5),Pair(0,6))
            .forEach { g[it.first][it.second] = Forbidden }
        val start = Pair(6,3)
        val blockeds = g.map { it.clone() }.toTypedArray()
        val gifts = mutableListOf<Pair<Int, Int>>()
        for (r in blockeds.indices) {
            for (c in blockeds[0].indices) {
                if (blockeds[r][c]==Empty) {
                    blockeds[r][c] = Gift
                    gifts.add(Pair(r,c))
                    print('.');  System.out.flush()
                    val empties = blockeds.flatMapIndexed { r :Int, row :ByteArray ->
                        row.mapIndexed { c, v ->
                            if (v != Gift) Pair(r, c)
                            else null
                        }
                    }.filterNotNull()
                    if (empties.all { blockeds.isReachable(start, it) }) {
                        if (c+1<blockeds[0].size)
                            blockeds[r][c+1] = -2
                        if (r+1 < blockeds.size)
                            blockeds[r+1][c] = -2
                        g[r][c] = Gift
                        print("\bv");  System.out.flush()
                    } else {
                        gifts.removeLast()
                        blockeds[r][c] = Empty
                        print("\b");  System.out.flush()
                    }
                }
            }
        }
        println("\nOn ${g.size}x${g[0].size} we can position ${gifts.size} gifts at \n"+ g.show())
    }

    @Test fun sevenTimesSevenFrom2() {
        val g = (1..7).map { byteArrayOf(Empty,Empty,Empty,Empty, Empty,Empty,Empty) }.toTypedArray()
        listOf(Pair(6,3), Pair(6,5), Pair(4,0),Pair(4,4), Pair(2,2),Pair(2,4), Pair(1,2), Pair(0,4),Pair(0,5),Pair(0,6))
            .forEach { g[it.first][it.second] = Forbidden }
        val start = Pair(6,3)
        val blockeds = g.map { it.clone() }.toTypedArray()
        val gifts = mutableListOf<Pair<Int, Int>>()
        for (r in blockeds.indices) {
            for (c in blockeds[0].indices) {
                if (r==0&&c==0)
                    continue
                if (blockeds[r][c]==Empty) {
                    blockeds[r][c] = Gift
                    gifts.add(Pair(r,c))
                    print('.');  System.out.flush()
                    val empties = blockeds.flatMapIndexed { r :Int, row :ByteArray ->
                        row.mapIndexed { c, v ->
                            if (v != Gift) Pair(r, c)
                            else null
                        }
                    }.filterNotNull()
                    if (empties.all { blockeds.isReachable(start, it) }) {
                        if (c+1<blockeds[0].size)
                            blockeds[r][c+1] = -2
                        if (r+1 < blockeds.size)
                            blockeds[r+1][c] = -2
                        g[r][c] = Gift
                        print("\bv");  System.out.flush()
                    } else {
                        gifts.removeLast()
                        blockeds[r][c] = Empty
                        print("\b");  System.out.flush()
                    }
                }
            }
        }
        println("\nOn ${g.size}x${g[0].size} we can position ${gifts.size} gifts at \n"+ g.show())
    }
}
