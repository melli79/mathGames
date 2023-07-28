package xmascalendar

import common.math.Rational

private const val N = 24
private const val n = 10

fun main() {
    var pa = Rational.ONE
    for (k in 0 until 4) {
        pa *= Rational.of((n-k).toLong(), (N-k).toLong())
    }
    println("a) p = $pa ≈ %.2g".format(pa.toDouble()))
    val pb = Rational.of(((N-6)*2*2*2 +2*2*2*1 +2*2*1*1 +2*1*1*1 + 1*1*1*1).toLong(),(N*(N-1)*(N-2)*(N-3)).toLong())
    println("b) p = $pb ≈ %.2g".format(pb.toDouble()))
    val pc = Rational.of((4*2).toLong(), (4*3*2*1).toLong())
    println("c) p = $pc ≈ %.2g".format(pc.toDouble()))
    val fix = Rational.of((1*4 +0*3 +6*2 +4*2*1 +9*0).toLong(), (4*3*2*1).toLong())
    println("d) E fix = $fix ≈ %.2f".format(fix.toDouble()))
    val loss = (Rational.of(4L, 1L)-fix)*Rational.of(1L, 5L)
    println("e) E loss = $loss ≈ %.2g".format(loss.toDouble()))
}
