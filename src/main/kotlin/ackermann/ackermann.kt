package ackermann

import java.math.BigInteger

fun ackermann(m :UByte, n :ULong) :BigInteger {
    if (m.toInt()==0)
        return BigInteger.valueOf(n.toLong()+1)
    if (m.toInt()==1)
        return BigInteger.valueOf(n.toLong()+2)
    if (m.toInt()==2)
        return BigInteger.valueOf(2*n.toLong()+3)
    if (n==0uL)
        return ackermann((m.toByte()-1).toUByte(), 1u)
    return ackermann((m.toByte()-1).toUByte(), ackermann(m, n-1u).toLong().toULong())
}
