package algebra

import kotlin.math.max

class Polynomial private constructor(val cs :List<Double>) {
    companion object {
        val ZERO = Polynomial(emptyList())
        fun const(c :Double) = Polynomial(listOf(c))
        val X = Polynomial(listOf(0.0, 1.0))

        fun of(cs :List<Double>) :Polynomial {
            if (cs.isNotEmpty() && cs.last()==0.0) {
                val cs1 = cs.dropLast(1).toMutableList()
                while (cs1.isNotEmpty() && cs1.last()==0.0) cs1.removeLast()
                return Polynomial(cs1)
            }
            return Polynomial(cs)
        }

        fun monomial(d :UInt) :Polynomial {
            val cs = MutableList(d.toInt()) { 0.0 }
            cs.add(1.0)
            return Polynomial(cs)
        }
    }
    val deg = cs.size-1
    val lc = cs.lastOrNull() ?: 0.0

    override fun toString() = if (this==ZERO) "0"  else cs.mapIndexed { d, c -> Pair(c, d) }.reversed().joinToString(" ") { (c, d) ->
        if (c!=0.0) when (d) {
            0 -> "%+f".format(c)
            1 -> if (c==1.0) "+X" else if (c==-1.0)"-X"  else "%+g*X".format(c)
            else -> if (c==1.0) "+X^$d" else if (c==-1.0)"-X^$d"  else "%+g*X^$d".format(c)
        }  else ""
    }

    override fun equals(other :Any?) = other is Polynomial && deg==other.deg && cs==other.cs

    override fun hashCode() = deg +37* cs.hashCode()

    operator fun plus(s :Polynomial) :Polynomial {
        val result = ArrayList<Double>(max(cs.size, s.cs.size))
        (cs zip s.cs).forEach { (c, c1) -> result.add(c + c1) }
        if (deg==s.deg) return of(result)
        else if (deg<s.deg) result.addAll(s.cs.drop(deg+1))
        else result.addAll(cs.drop(s.deg+1))
        return Polynomial(result)
    }

    operator fun plus(s :Double) = plus(const(s))
    operator fun unaryMinus() = Polynomial(cs.map { -it })
    operator fun minus(s :Polynomial) = plus(s.unaryMinus())
    operator fun minus(s :Double) = plus(-s)
    operator fun times(f :Double) = if (f==0.0) ZERO  else if (f==1.0) this  else Polynomial(cs.map { c -> c*f })

    operator fun invoke(x :Double) = cs.reversed().foldRight(0.0) { c, s -> s*x + c }

    operator fun times(f :Polynomial) :Polynomial {
        if (this==ZERO || f==ZERO) return ZERO
        val result = MutableList(deg+f.deg+1) {0.0}
        if (deg<=f.deg) cs.forEachIndexed { d, f1 ->
            f.cs.mapIndexed { d1, c -> result[d+d1] += f1*c }
        } else f.cs.forEachIndexed { d, f1 ->
            cs.mapIndexed { d1, c -> result[d+d1] += f1*c }
        }
        return Polynomial(result)
    }
}

inline operator fun Double.plus(p :Polynomial) = p +this
inline operator fun Double.minus(p :Polynomial) = -p +this
inline operator fun Double.times(p :Polynomial) = p*this
