package rational

import common.math.DivisionByZeroException
import common.math.Rational

class QPolynomial(cs0 :Collection<Rational>) {
    companion object {
        val ZERO = QPolynomial(listOf(Rational.ZERO))
        val ONE = QPolynomial(listOf(Rational.ONE))
        val X = QPolynomial(listOf(Rational.ZERO, Rational.ONE))

        fun const(c :Rational) = QPolynomial(listOf(c))
        fun monom(d :UByte) :QPolynomial {
            val cs = (0 until d.toInt()).map { Rational.ZERO } + listOf(Rational.ONE)
            return QPolynomial(cs)
        }
    }

    private val cs :Array<Rational>

    init {
        if (cs0.isEmpty()) {
            this.cs = arrayOf(Rational.ZERO)
        } else {
            val cs = cs0.toList()
            var d = cs.size - 1
            while (d > 0 && cs[d] == Rational.ZERO)
                d--
            this.cs = cs.subList(0, d + 1).toTypedArray()
        }
    }

    val deg = if (cs.isNotEmpty() || cs[0]!=Rational.ZERO) cs.size-1  else -1

    val lc :Rational
        get() = if (deg>=0) cs[deg]  else Rational.ZERO

    override fun toString() = toString("X")

    fun toString(x :String): String {
        if (deg<=0)
            return cs[0].toString()
        return cs.mapIndexed { d, c -> Pair(c, d) }.filter { it.first!= Rational.ZERO }
            .joinToString("+") { when (it.second) {
                0 -> it.first.toString()
                1 -> if (it.first==Rational.ONE) x else if (it.first==Rational.MINUS_ONE) "-$x"  else "${it.first}*$x"
                else -> "${it.first}*$x^${it.second}"
            } }
    }

    operator fun plus(s :QPolynomial) = if (deg<s.deg)
        QPolynomial(cs.zip(s.cs).map { (c, c1) -> c+c1 } +s.cs.slice((deg+1)..s.deg))
    else
        QPolynomial(cs.zip(s.cs).map { (c, c1) -> c+c1 } +cs.slice((s.deg+1)..deg))

    operator fun unaryMinus() = QPolynomial(cs.map { c -> -c }.toList())

    operator fun minus(s :QPolynomial) = plus(-s)

    override fun equals(other: Any?): Boolean {
        if (other !is QPolynomial)
            return false
        return deg==other.deg && cs.contentEquals(other.cs)
    }

    override fun hashCode() = cs.hashCode()

    operator fun times(f :Rational) :QPolynomial {
        if (f==Rational.ZERO)
            return ZERO
        return QPolynomial(cs.map { c -> c*f })
    }

    fun shl(d :UShort) :QPolynomial {
        val result :List<Rational> = (0 until d.toInt()).map { Rational.ZERO } +cs.toList()
        return QPolynomial(result)
    }

    operator fun times(p :QPolynomial) :QPolynomial {
        if (deg<0||p.deg<0)
            return ZERO
        if (deg==0)
            return p.times(cs[0])
        if (p.deg==0)
            return times(p.cs[0])
        return if (deg<p.deg)
            cs.mapIndexed { d, c -> Pair(c, d) }.filter { it.first!=Rational.ZERO }
                .map { (c, d) -> (p*c).shl(d.toUShort()) }
                .foldRight(ZERO) { s, s1 -> s+s1 }
        else
            p.cs.mapIndexed { d, c -> Pair(c, d) }.filter { it.first!=Rational.ZERO }
                .map { (c, d) -> times(c).shl(d.toUShort()) }
                .foldRight(ZERO) { s, s1 -> s+s1 }
    }

    fun div(denom :QPolynomial) :Pair<QPolynomial, QPolynomial> {
        if (denom==ZERO)
            throw DivisionByZeroException()
        if (denom.deg==0)
            return Pair(this*(denom.lc.inv()), ZERO)
        var r = this
        var q = ZERO
        val f = denom.lc.inv()
        while (r.deg>=denom.deg) {
            val q1 = r.lc * f;  val d = (r.deg -denom.deg).toUShort()
            if (q1!=Rational.ZERO) {
                val qx = const(q1).shl(d)
                q += qx
                r -= qx*denom
            }
        }
        return Pair(q, r)
    }
}
