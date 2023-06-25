package complexity

import rational.ipow

fun ackermann(m :UByte, n :ULong) :ULong {
    if (m.toInt()==0)
        return n+1u
    if (n==0uL)
        return ackermann((m-1u).toUByte(), 1u)
    if (m.toInt()==1)
        return n+2uL
    if (m.toInt()==2)
        return 2u*n+3uL
    if (m.toInt()==3)
        return 2uL.ipow((n+3u).toUByte())-3uL
    return ackermann((m-1u).toUByte(), ackermann(m, n-1uL))
}

fun t(m :UByte, x :ULong) :ULong {
    if (m.toInt()==0)
        return x+2uL
    if (x<=1uL)
        return 2uL
    if (m.toInt()==1)
        return 2u*x
    if (m.toInt()==2)
        return 2uL.ipow(x.toUByte())
    return t((m-1u).toUByte(), t(m, x-1uL))
}
