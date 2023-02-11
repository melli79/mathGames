package reedsolomon

import kotlin.math.abs
import common.math.epsilon

class SingularityException(msg :String ="Singularity") :Exception(msg) {}

fun rEliminate(m :Matrix, d :UShort) :Matrix {
    val m1 = m.clone()
    for (r in (0u..d.toUInt()).reversed()) {
        val f = m1[r, r]
        if (abs(f-1.0)>epsilon)
            throw SingularityException("Matrix is (almost) singular.")
        for (r1 in 0u until r) {
            m1.add(r, r1, -m1[r1, r])
        }
    }
    return m1.slice(0u until m1.m, (d+1u) until m1.n)
}

fun gaussEliminate(m :Matrix, d :UShort, limit :Boolean =false) :Matrix {
    val m1 = m.clone()
    var r = 0u
    for (c in 0u..d.toUInt()) {
        val i = argmax(m1, c, if (limit) d.toUInt()  else m1.m){ v -> abs(v) }
        val f = m1[i, c]
        if (abs(f)<epsilon) {
            println("column $c has all 0s.")
            continue
        }
        m1.swap(i, r)
        m1.scale(r, 1/f)
        for (r1 in r+1u until m1.m) {
            m1.add(r, r1, -m1[r1, c])
        }
        r++
        if (r>=m1.m)
            break
    }
    return m1
}

fun argmax(m :Matrix, c :UInt, limit :UInt, f :(Double)->Double) :UInt {
    var result = c
    var v = f(m[result, c])
    for (r in c+1u until limit) {
        val v1 = f(m[r, c])
        if (v1>v) {
            result = r;  v = v1
        }
    }
    return result
}
