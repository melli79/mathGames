package xmascalendar

import common.math.gcd
import kotlin.math.abs

fun main() {
    val m = LongMatrix(listOf(listOf(3,1,0,1), listOf(0,2,1,2), listOf(1,1,2,1)))
    val b = LongMatrix.col(125, 230, 185)
    println("Trying to solve\n ${m.join(b).toString(4u)}...\n")
    val echelon = makeEchelon(m, b)
    val soln = makeDiagonal(echelon, 4u)
    println("It remains to interpret:\n ${soln.toString(4u)}\n")
    readOff(soln, 4u)
    val x0 = 10u;  val x2 = 40u
    val x13 = (x0/5u +1u until x2/5u).map { x5 -> Pair(5u*x5, 95u-5u*x5) }
        .filter { (_, x3) -> x2<x3 }
    if (x13.isEmpty())
        println("There is no solution for x2, x3.")
    else
        println("${x13.size} solutions: (x1, x3) in $x13")
}

fun readOff(soln :LongMatrix, split :UInt) {
    var r = 0u
    for (row in soln.rows) {
        val rhs = row.drop(split.toInt())
        val cs = row.subList(0, split.toInt()).mapIndexed { i, v -> Pair(i.toUInt(), v) }
        val i = cs.firstOrNull { p -> p.second!=0L }
        if (i!=null) {
            if (cs.drop(i.first.toInt()).all { it.second==0L }) {
                val lc = i.second
                println("x${i.first} = $rhs/$lc")
            } else {
                println(cs.filter { p -> p.second!=0L }.joinToString(" ") { p -> when (p.second) {
                        1L -> "+"
                        -1L -> "-"
                        else -> p.second.toString()
                    } +"x${p.first}" }+" = $rhs")
            }
        } else if (rhs.any { it!=0L }) {
            println("Has no solution")
            break
        }
        r++
    }
}

fun makeDiagonal(echelon :LongMatrix, split :UInt) :LongMatrix {
    val result = LongMatrix(echelon.rows)
    var r = result.numRows.toInt() -1
    var stop = false
    while (r>=0&&!stop) {
        while (r>=0 && result.isZeroRow(r.toUInt(), split))
            r--
        if (r<0) {
            println("stopping")
            break
        }
        val row = result.getRow(r.toUInt())
        val (c, lc) = row.subList(0, split.toInt()).mapIndexed { c, v -> Pair(c.toUInt(), v) }
            .reversed().first { p -> p.second!=0L }
        for (r2 in 0u until r.toUInt()) {
            val lc2 = result[r2, c]
            if (lc2%lc!=0L) {
                stop = true
                continue
            }
            val f = -lc2/lc
            result.plusAssign(r2, row*f)
            result.factorOut(r2)
        }
        r--
    }
    return result
}

fun makeEchelon(m :LongMatrix, b :LongMatrix) :LongMatrix {
    val result = m.join(b)
    val split = m.numCols
    var r = 0u
    var stop = false
    var c = 0u
    while (r<result.numRows && c<split) {
        var bestRow :Pair<UInt, List<Long>>? =null
        while (c<split) {
            bestRow = result.rows.drop(r.toInt()).mapIndexed { i, row -> Pair(r + i.toUInt(), row) }
                .filter { p -> p.second[c.toInt()] != 0L }
                .minByOrNull { p -> abs(p.second[c.toInt()]) }
            if (bestRow == null)
                c++
            else
                break
        }
        if (bestRow==null) {
            println("All remaining columns contain 0s")
            break
        }
        val r1 = bestRow.first
        if (r1!=r) {
            val row = result.getRow(r)
            result.setRow(r, bestRow.second)
            result.setRow(r1, row)
        }
        var lc = bestRow.second[c.toInt()]
        if (lc<0) {
            result.timesAssign(r, -1L)
            lc = -lc
        }
        val row = result.getRow(r)
        for (r2 in r+1u until result.numRows) {
            val lc2 = result[r2, c]
            if (lc2%lc!=0L) {
                println("cannot fully cancel row $r2 with $row.")
                stop = true
            }
            val f = -lc2/lc
            result.plusAssign(r2, row*f)
            result.factorOut(r2)
        }
        if (stop) break
        r++
    }
    return result
}

private operator fun List<Long>.times(f :Long) = map { v -> v*f }

class LongMatrix(values0 :List<List<Long>> = emptyList()) {

    val numRows :UInt
        get() = values.size.toUInt()

    val numCols :UInt
        get() = values[0].size.toUInt()

    private val values = values0.map { row -> row.toMutableList() }.toMutableList()
    val rows :List<List<Long>>
        get() = values

    companion object {
        fun of(values0 :Map<Pair<Int, Int>, Long>) :LongMatrix {
            val numRows = values0.entries.maxOf { entry -> entry.key.first }
            val numCols = values0.entries.maxOf { entry -> entry.key.second }
            val rows :Map<Int, MutableList<Long>> = values0.entries.groupBy { it.key.first }
                .map { entry ->
                    val cols = entry.value.map { en -> en.value }
                    Pair(entry.key, (0 until numCols).map { c -> cols[c] }.toMutableList())
                }.toMap()
            return LongMatrix((0 until numRows)
                .map { r -> rows[0] ?: (0 until numCols).map{0L}.toMutableList() }
                .toMutableList())
        }

        fun col(vararg vals :Long) = LongMatrix(vals.map { v -> listOf(v) })
    }

    override fun toString() = values.map { it.toString() }.joinToString("\n")

    fun toString(split :UInt) = values.map { row ->
        row.subList(0, split.toInt()).joinToString(" ") + " | " + row.drop(split.toInt()).joinToString(" ")
    }.joinToString("\n")

    operator fun get(i :UInt, j :UInt) = values[i.toInt()][j.toInt()]

    fun getRow(i :UInt) :List<Long> = values[i.toInt()]
    fun getCol(j :UInt) = values.map { row -> row[j.toInt()] }

    operator fun set(i :UInt, j :UInt, value :Long) {
        values[i.toInt()][j.toInt()] = value
    }

    fun join(other :LongMatrix) :LongMatrix {
        assert(values.size==other.values.size)
        return LongMatrix(values.zip(other.values).map{ (r, r2) -> r+r2 })
    }

    operator fun times(f :Long) = LongMatrix(values.map { row -> row.map { v -> v*f } })
    operator fun div(f :Long) = LongMatrix(values.map { row -> row.map { v -> v/f } })
    fun setRow(i :UInt, row :List<Long>) {
        assert(row.size.toUInt()==numCols)
        values[i.toInt()] = row.toMutableList()
    }

    fun timesAssign(i :UInt, f :Long) {
        values[i.toInt()] = values[i.toInt()].map { v -> v*f }.toMutableList()
    }

    fun plusAssign(r :UInt, row :List<Long>) {
        assert(row.size.toUInt()==numCols)
        values[r.toInt()] = values[r.toInt()].zip(row).map { (v, w) -> v+w }.toMutableList()
    }

    fun factorOut(r :UInt) {
        val row = values[r.toInt()]
        if (row.size<1)
            return
        if (row.size==1) {
            values[r.toInt()] = mutableListOf(1L)
            return
        }
        val gcf = row.drop(1).fold(abs(row[0]).toULong()) { f,v -> gcd(f, abs(v).toULong()) }
        if (gcf==0uL)
            return
        val f = sgn(row.first { it!=0L }) * gcf.toLong()
        values[r.toInt()] = row.map { v -> v/f }.toMutableList()
    }

    private fun sgn(x :Long) = when {
        x>0L -> 1L
        x==0L -> 0L
        else -> -1L
    }

    fun isZeroRow(r :UInt, split :UInt =numCols) =
        values[r.toInt()].subList(0, split.toInt()).all { it==0L }
}
