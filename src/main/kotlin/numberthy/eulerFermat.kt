package numberthy

import common.math.factor
import common.math.gcd

private const val debug = true

/**
 * Asymmetric encryption and decryption via Euler-Fermat theorem:
 *  a^{\phi(n)+1} ≡ a (mod n)  when n is the product of disjoint primes
 */
fun main() {
    val n = 1073uL
    val e = 7uL
    assert(gcd(e,n-1uL)==1uL)
    val x = phi(n)
    val f = 460uL
    assert(e*f%x==1uL)
    print("Decryption or Encryption (d/e)? ")
    val choice = readlnOrNull()?.trim()
    if (choice.isNullOrEmpty())
        return
    when (choice[0]) {
        'e', 'E' -> encrypt(n, e)
        'd', 'D' -> decrypt(n, f)
        else -> println("unclear choice")
    }
}

fun decrypt(n :ULong, f :ULong) {
    if (debug)
        println("The secret key is f= $f.")
    while (true) {
        print("Please enter the encrypted message: ")
        val b = readlnOrNull()?.trim()?.toULongOrNull() ?: return
        if (b>=n) {
            println("This is not possible.")
            continue
        }
        val a = mpow(b, f, n)
        println("Decrypted message: $a")
    }
}

fun encrypt(n :ULong, e :ULong) {
    while (true) {
        print("Please enter your secret message (an int between 0 and $n): ")
        val a = readlnOrNull()?.trim()?.toULongOrNull() ?: return
        if (a>=n) {
            println("This is too big.")
            continue
        }
        val b = mpow(a, e, n)
        println("The encrypted message is $b.")
    }
}


fun phi(n :ULong) :ULong {
    val fs :Map<ULong, UByte> = factor(n)
    return fs.entries.map { (p, e) -> (p-1uL)* ipow(p, (e-1u).toUByte()) }.fold(1uL){ p, f -> p*f }
}

fun ipow(b :ULong, e :UByte) :ULong {
    if (e.toInt()==0)
        return 1uL
    if (b==0uL||b==1uL)
        return b
    var result = 1uL
    var p = b
    var r = e.toInt()
    while (r>0) {
        if (r%2>0)
            result *= p
        p *= p;  r /= 2
    }
    return result
}

fun mpow(b :ULong, e :ULong, n :ULong) :ULong {
    if (e==0uL)
        return 1uL
    if (b==0uL||b==1uL)
        return b
    var result = 1uL
    var p = b%n
    var r = e.toLong()
    while (r>0) {
        if (r%2L>0)
            result = result*p %n
        p = p*p %n;  r /= 2L
    }
    return result
}
