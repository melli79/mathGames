package algebra

import kotlin.math.max

class ZPoly private constructor(val cs :List<Int>) {
    companion object {
        val ZERO = ZPoly(emptyList())
        fun const(c :Int) = ZPoly(listOf(c))
        val X = ZPoly(listOf(0, 1))

            fun of(cs :List<Int>) :ZPoly {
            if (cs.isNotEmpty() && cs.last()==0) {
                val cs1 = cs.dropLast(1).toMutableList()
                while (cs1.isNotEmpty() && cs1.last()==0) cs1.removeLast()
                return ZPoly(cs1)
            }
            return ZPoly(cs)
        }

        fun monomial(d :UInt) :ZPoly {
            val cs = MutableList(d.toInt()) { 0 }
            cs.add(1)
            return ZPoly(cs)
        }
    }
    val deg = cs.size-1
    val lc = cs.lastOrNull() ?: 0

    override fun toString() = if (this==ZERO) "0"  else cs.mapIndexed { d, c -> Pair(c, d) }.reversed().joinToString(" ") { (c, d) ->
        if (c!=0) when (d) {
            0 -> "%+d".format(c)
            1 -> if (c==1) "+X" else if (c==-1)"-X"  else "%+d*X".format(c)
            else -> if (c==1) "+X^$d" else if (c==-1)"-X^$d"  else "%+d*X^$d".format(c)
        }  else ""
    }

    override fun equals(other :Any?) = other is ZPoly && deg==other.deg && cs==other.cs

    override fun hashCode() = deg +31* cs.hashCode()

    operator fun plus(s :ZPoly) :ZPoly {
        val result = ArrayList<Int>(max(cs.size, s.cs.size))
        (cs zip s.cs).forEach { (c, c1) -> result.add(c + c1) }
        if (deg==s.deg) return of(result)
        else if (deg<s.deg) result.addAll(s.cs.drop(deg+1))
        else result.addAll(cs.drop(s.deg+1))
        return ZPoly(result)
    }

    operator fun plus(s :Int) = plus(const(s))
    operator fun unaryMinus() = ZPoly(cs.map { -it })
    operator fun minus(s :ZPoly) = plus(s.unaryMinus())
    operator fun minus(s :Int) = plus(-s)
    operator fun times(f :Int) = if (f==0) ZERO  else if (f==1) this  else ZPoly(cs.map { c -> c*f })

    operator fun invoke(x :Int) = cs.foldRight(0) { c, s -> s*x + c }
    operator fun invoke(x :Double) = cs.foldRight(0.0) { c, s -> s*x + c }

    operator fun times(f :ZPoly) :ZPoly {
        if (this==ZERO || f==ZERO) return ZERO
        val result = MutableList(deg+f.deg+1) {0}
        if (deg<=f.deg) cs.forEachIndexed { d, f1 ->
            f.cs.mapIndexed { d1, c -> result[d+d1] += f1*c }
        } else f.cs.forEachIndexed { d, f1 ->
            cs.mapIndexed { d1, c -> result[d+d1] += f1*c }
        }
        return ZPoly(result)
    }
}

inline operator fun Int.plus(p :ZPoly) = p +this
inline operator fun Int.minus(p :ZPoly) = -p +this
inline operator fun Int.times(p :ZPoly) = p*this

fun sqr(p :ZPoly) = p*p
fun cb(p :ZPoly) = p*p*p
fun bsqr(p :ZPoly) = sqr(sqr(p))
fun pent(p :ZPoly) = bsqr(p)*p
