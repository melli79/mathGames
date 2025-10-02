package geometry

import common.math.factor
import common.math.findPrimes

data class F2(val p :UInt, val v0 :UInt, val v1 :UInt) {
    companion object {
        fun zero(p :UInt) = F2(p, 0u,0u)
        fun e0(p :UInt) = F2(p, 1u,0u)
        fun e1(p :UInt) = F2(p, 0u,1u)
    }

    override fun toString() = "[$v0, $v1] % $p"

    override fun equals(other :Any?) :Boolean {
        if (other !is F2) return false
        return p==other.p && v0==other.v0 && v1==other.v1
    }

    operator fun plus(s :F2) = F2(p, (v0+s.v0)%p, (v1+s.v1)%p)

    operator fun unaryMinus() = F2(p, (p-v0)%p, (p-v1)%p)

    operator fun times(f :UInt) = F2(p, (v0*f)%p, (v1*f)%p)

    fun dot(w :F2) = (v0*w.v0 +v1*w.v1)%p
}

data class EndF2(val p :UInt, val m00 :UInt, val m01 :UInt, val m10 :UInt, val m11 :UInt) {
    companion object {
        fun zero(p :UInt) = EndF2(p, 0u,0u, 0u, 0u)
        fun eye(p :UInt) = EndF2(p, 1u, 0u, 0u, 1u)
        fun eta(p :UInt) = EndF2(p, 1u,0u,0u, p-1u)
        fun e00(p :UInt) = EndF2(p, 1u, 0u, 0u, 0u)
        fun e01(p :UInt) = EndF2(p, 0u, 1u, 0u, 0u)
        fun e10(p :UInt) = EndF2(p, 0u, 0u, 1u, 0u)
        fun e11(p :UInt) = EndF2(p, 0u, 0u, 0u, 1u)
    }

    override fun toString() = "[$m00, $m01; $m10, $m11] % $p"

    override fun equals(other :Any?) :Boolean {
        if (other !is EndF2) return false
        return p==other.p && m00==other.m00 && m01==other.m01 && m10==other.m10 && m11==other.m11
    }

    fun det() = (m00*m11 +p-(m10*m01%p)) % p

    fun transpose() = EndF2(p, m00, m10, m01,m11)

    operator fun plus(s :EndF2) = EndF2(p, (m00+s.m00)%p, (m01+s.m01)%p, (m10+s.m10)%p, (m11+s.m11)%p)

    operator fun unaryMinus() = EndF2(p, (p-m00)%p, (p-m01)%p, (p-m10)%p, (p-m11)%p)

    operator fun times(f :UInt) = EndF2(p, (m00*f)%p, (m01*f)%p, (m10*f)%p, (m11*f)%p)

    operator fun times(v :F2) = F2(p, (m00*v.v0+m01*v.v1)%p, (m10*v.v0+m11*v.v1)%p)

    operator fun times(m :EndF2) = EndF2(p, (m00*m.m00+m01*m.m10)%p, (m00*m.m01+m01*m.m11)%p,
        (m10*m.m00+m11*m.m10)%p, (m10*m.m01+m11*m.m11)%p)
}

data class F3(val p :UInt, val v0 :UInt, val v1 :UInt, val v2 :UInt) {
    companion object {
        fun zero(p :UInt) = F3(p, 0u,0u,0u)
        fun e0(p :UInt) = F3(p, 1u,0u,0u)
        fun e1(p :UInt) = F3(p, 0u,1u,0u)
        fun e2(p :UInt) = F3(p, 0u,0u,1u)
    }

    override fun toString() = "[$v0, $v1, $v2] % $p"

    override fun equals(other :Any?) :Boolean {
        if (other !is F3) return false
        return p==other.p && v0==other.v0 && v1==other.v1 && v2==other.v2
    }

    operator fun plus(s :F3) = F3(p, (v0+s.v0)%p, (v1+s.v1)%p, (v2+s.v2)%p)

    operator fun unaryMinus() = F3(p, (p-v0)%p, (p-v1)%p, (p-v2)%p)

    operator fun times(f :UInt) = F3(p, (v0*f)%p, (v1*f)%p, (v2*f)%p)

    fun dot(w :F3) = (v0*w.v0 +v1*w.v1 +v2*w.v2)%p
}

data class EndF3(val v0 :F3, val v1 :F3, val v2 :F3) {
    companion object {
        fun zero(p :UInt) = EndF3(F3.zero(p),F3.zero(p), F3.zero(p))
        fun eye(p :UInt) = EndF3(F3.e0(p), F3.e1(p), F3.e2(p))
        fun eta12(p :UInt) = EndF3(F3.e0(p),-F3.e1(p), -F3.e2(p))
        fun eta21(p :UInt) = EndF3(F3.e0(p),F3.e1(p), -F3.e2(p))
        fun e00(p :UInt) = EndF3(F3.e0(p), F3.zero(p), F3.zero(p))
        fun e01(p :UInt) = EndF3(F3.e1(p), F3.zero(p), F3.zero(p))
        fun e02(p :UInt) = EndF3(F3.e2(p), F3.zero(p), F3.zero(p))
        fun e10(p :UInt) = EndF3(F3.zero(p), F3.e0(p), F3.zero(p))
        fun e11(p :UInt) = EndF3(F3.zero(p), F3.e1(p), F3.zero(p))
        fun e12(p :UInt) = EndF3(F3.zero(p), F3.e2(p), F3.zero(p))
        fun e20(p :UInt) = EndF3(F3.zero(p), F3.zero(p), F3.e0(p))
        fun e21(p :UInt) = EndF3(F3.zero(p), F3.zero(p), F3.e1(p))
        fun e22(p :UInt) = EndF3(F3.zero(p), F3.zero(p), F3.e2(p))
    }

    override fun toString() = "[$v0; $v1; $v2]"

    override fun equals(other :Any?) :Boolean {
        if (other !is EndF3) return false
        return v0==other.v0 && v1==other.v1 && v2==other.v2
    }

    fun det() = (v0.v0*v1.v1*v2.v2%v0.p +v0.v1*v1.v2*v2.v0%v0.p +v0.v2*v1.v0*v2.v1%v0.p +3u*v0.p
            -(v0.v2*v1.v1*v2.v0%v0.p +v0.v1*v1.v0*v2.v2%v0.p +v0.v0*v1.v2*v2.v1%v0.p)) % v0.p

    fun transpose() = EndF3(F3(v0.p, v0.v0, v1.v0, v2.v0), F3(v0.p, v0.v1, v1.v1, v2.v1), F3(v0.p, v0.v2,v1.v2,v2.v2))

    operator fun plus(s :EndF3) = EndF3(v0+s.v0, v1+s.v1, v2+s.v2)

    operator fun unaryMinus() = EndF3(-v0, -v1, -v2)

    operator fun times(f :UInt) = EndF3(v0*f, v1*f, v2*f)

    operator fun times(v :F3) = F3(v0.p, v0.dot(v), v1.dot(v), v2.dot(v))

    operator fun times(f :EndF3) :EndF3 {
        assert(v0.p==f.v0.p)
        assert(v0.p==v1.p)
        assert(v0.p==v2.p)
        assert(f.v0.p==f.v1.p)
        assert(f.v0.p==f.v2.p)
        val t = f.transpose()
        return EndF3(
            F3(v0.p, v0.dot(t.v0), v0.dot(t.v1), v0.dot(t.v2)),
            F3(v0.p,v1.dot(t.v0),v1.dot(t.v1),v1.dot(t.v2)),
            F3(v0.p,v2.dot(t.v0),v2.dot(t.v1),v2.dot(t.v2)))
    }
}

fun main() {
    for (p in findPrimes(1000u)) {
        print("$p:  ");  System.out.flush()
        val s1 = (0u..<p).flatMap { v0 -> (0u..<p).map { v1 -> F2(p, v0, v1) } }.filter { v -> v.dot(v) == 1u }
//    println("S^1_{\\F_$p} = $s1")
//    println("|S^1| = ${s1.size}")
        val eta = EndF3.eta12(p)
        val c12 = (0u..<p).flatMap { v0 -> (0u..<p).flatMap { v1 -> (0u..<p).map { v2 -> F3(p, v0, v1, v2) } } }
            .filter { v -> (eta*v).dot(v) == 1u }
//    println("C_{F_$p}(\\eta) = $c12")
//    println("|C| = ${c12.size}")
        val c21 = (0u..<p).flatMap { v0 -> (0u..<p).flatMap { v1 -> (0u..<p).map { v2 -> F3(p, v0, v1, v2) } } }
            .filter { v -> (eta*v).dot(v) == p - 1u }
//    println("\\bar{C}_{F_$p}(\\eta) = $c21")
//    println("|\\bar{C}| = ${c21.size}")
//    println("O_{\\F_$p}(1) = { ±1 }")
        val o2 =
            s1.flatMap { v0 -> s1.filter { v1 -> v0.dot(v1) == 0u }.map { v1 -> EndF2(p, v0.v0, v0.v1, v1.v0, v1.v1) } }
//    println("|O_{\\F_$p}| = ${o2.size}")
        val o12 = c12.flatMap { v0 ->
            val v0t = eta*v0
            c21.filter { v1 -> v0t.dot(v1) == 0u }.flatMap { v1 ->
                val v1t = eta*v1
                c21.filter { v2 ->
                    v0t.dot(v2) == 0u && v1t.dot(v2) == 0u
                }.map { v2 -> EndF3(v0, v1, v2) }
            }
        }
        print("|O_{\\F_$p}(1,2)| = ${o12.size},  ")
        val n2 = o12.size/(2*o2.size)
        if (n2 < 3)
            println(" -- That does not give a plane.")
        else
            println("|H_{\\F_$p}^2| = |O(1,2)/O(1)x O(2)| = $n2 = ${factor(n2.toULong())}, ${if (o12.size == n2*2*o2.size)
                "Ok" else "fail"}")
    }
}
