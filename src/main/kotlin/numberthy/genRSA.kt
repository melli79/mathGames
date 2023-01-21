package numberthy

/**
 * Generating a public/private key pair for the RSA algorithm.
 *
 * n := p1*p2
 * x := phi(n) = (p1-1)*(p2-1)
 * choose e such that gcd(x, e) = 1
 * f = euclid(x, e).second
 *
 * Then: (a^e)^f ≡ a (mod n)
 */
fun main() {
    print("Generating an RSA key pair\n  Please enter 2 distinct primes: ")
    val input = readln().trim().split("[\\s,]+".toRegex())
    assert(input.size==2){ "2 numbers are required." }
    val p1 = input[0].toULongOrNull();  val p2 = input[1].toULongOrNull()
    assert(p1!=null && p1>1uL && p1%2uL>0u) { "The first number is not a prime." }
    assert(p2!=null && p2>1uL && p2%2uL>0u && p2%p1!!>0u) { "The second number is not a prime." }
    assert(p1!=p2) { "The primes have to be different." }
    assert(p1!!%p2!!>0u) { "The first number is not a prime." }
    var e = 7uL
    while (e<p1&&e<p2) {
        try {
            val (n, f) = genPrivateKey(p1, p2, e)
            println("Der öffentliche Schlüssel ist n= $n, e= $e.  Der private Schlüssel ist f= $f")
            return
        } catch (_ :AssertionError) {
            e += 2uL
        }
    }
    println("Did not find any modular inverses.  Maybe the numbers are not prime?")
}

fun genPrivateKey(p1 :ULong, p2 :ULong, e :ULong) :Pair<ULong,ULong> {
    val n = p1*p2
    val x = (p1-1uL)*(p2-1uL)
    val fs = euclid(n.toLong(), e.toLong())
    assert(fs.third==1L)
    val f = fs.second.toULong()
    return Pair(n, f)
}

fun euclid(a :Long, b :Long) :Triple<Long, Long, Long> {
    var a0 = a;  var f0 = 1L;  var f1 = 0L
    var a1 = b;  var g0 = 0L;  var g1 = 1L
    while (a1!=0L) {
        val d = a0/a1;  val a2 = a0 -d*a1
        val h0 = f0 -d*g0;  val h1 = f1 -d*g1
        a0 = a1;  f0 = g0;  f1 = g1
        a1 = a2;  g0 = h0;  g1 = h1
    }
    return Triple(f0, f1, a0)
}
