package dynamic

import java.util.Stack

object TowerOfHanoi {
    private val splits = mutableListOf(mutableMapOf(Pair(0u,0u)), mutableMapOf(Pair(0u,0u)), mutableMapOf(Pair(0u,0u), Pair(1u,0u)),
        mutableMapOf(Pair(0u,0u), Pair(1u,1u)))
    private val moves = mutableListOf(mutableMapOf(Pair(0u, 0uL)), mutableMapOf(Pair(0u, 0uL)), mutableMapOf(Pair(0u, 0uL), Pair(1u, 1uL)),
        mutableMapOf(Pair(0u, 0uL), Pair(1u, 1uL)))

    fun computeSplit(n :UInt, t :UByte) :UInt {
        if (n==0u)
            return 0u
        if (t.toInt()==2)
            return n-1u
        assert(t>=3u)
        if (t.toInt()==3)
            return 1u
        while (splits.size<=t.toInt()) {
            splits.add(mutableMapOf(Pair(0u,0u), Pair(1u,1u)))
            moves.add(mutableMapOf(Pair(0u, 0uL), Pair(1u, 1uL)))
        }
        val candidates = splits[t.toInt()]
        if (n !in candidates)
            candidates[n] = optimize(n, t)
        return candidates[n]!!
    }

    private fun optimize(n :UInt, t :UByte) :UInt {
        var opt = 1u;  var min = ULong.MAX_VALUE
        for (s in 1u until n) {
            val num = 2u* computeNumMoves(n-s, t) +computeNumMoves(s, (t-1u).toUByte())
            if (num<min) {
                opt = s;  min = num
            }
        }
        splits[t.toInt()][n] = opt
        moves[t.toInt()][n] = min
        return opt
    }

    fun computeNumMoves(n :UInt, t :UByte) :ULong {
        if (n<2u)
            return n.toULong()
        if (t<=2u)
            return ULong.MAX_VALUE
        if (t.toInt()==3)
            return 1uL.shl(n.toInt()) -1uL
        while (moves.size<=t.toInt()) {
            moves.add(mutableMapOf(Pair(0u, 0uL), Pair(1u, 1uL)))
            splits.add(mutableMapOf(Pair(0u,0u), Pair(1u,1u)))
        }
        val candidates = moves[t.toInt()]
        if (n !in candidates)
            optimize(n, t)
        return candidates[n]!!
    }

    fun <T :Comparable<T>> shuffle(src :Stack<T>, n :UInt, tar :Stack<T>, auxs :List<Stack<T>>) {
        if (n==0u)
            return
        if (n==1u) {
            move(src, tar)
            return
        }
        assert(auxs.isNotEmpty())
        if (auxs.size==1) {
            if (n % 2u == 0u)
                shuffle3(src, n, auxs.first(), tar)
            else
                shuffle3(src, n, tar, auxs.first())
            return
        }
        // Frame-Steward algorithm
        val s = computeSplit(n, (auxs.size+2).toUByte())
        shuffle(src, n-s, auxs.first(), auxs.slice(1 until auxs.size)+ listOf(tar))
        shuffle(src, s, tar, auxs.slice(1 until auxs.size))
        shuffle(auxs.first(), n-s, tar, auxs.slice(1 until auxs.size)+ listOf(src))
    }

    private fun <T :Comparable<T>> shuffle3(src :Stack<T>, n :UInt, t1 :Stack<T>, t2 :Stack<T>) {
        val end = computeNumMoves(n, 3u)
        var count = 0uL
        for (i in 0uL..end/6u) {
            move(src, t1);  count++
            if (count>=end)
                break
            assert(src.isNotEmpty() || t2.isNotEmpty())
            if (src.isNotEmpty() && (t2.isEmpty() || src.peek()<t2.peek()))
                move(src, t2)
            else
                move(t2, src)
            count++

            move(t1, t2);  count++
            if (count>=end)
                break
            assert(src.isNotEmpty() || t1.isNotEmpty())
            if (t1.isNotEmpty() && (src.isEmpty() || t1.peek()<src.peek()))
                move(t1, src)
            else
                move(src, t1)
            count++

            move(t2, src);  count++
            if (count>=end)
                break
            assert(t2.isNotEmpty() || t1.isNotEmpty())
            if (t2.isNotEmpty() && (t1.isEmpty() || t2.peek()<t1.peek()))
                move(t2, t1)
            else
                move(t1, t2)
            count++
        }
    }

    private fun <T :Comparable<T>> move(src :Stack<T>, tar :Stack<T>) {
        assert(src.isNotEmpty())
        val disk = src.pop()
        assert(tar.isEmpty() || disk<tar.peek())
        tar.push(disk)
    }
}
