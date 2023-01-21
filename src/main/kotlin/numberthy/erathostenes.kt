package numberthy

import java.lang.System.out

fun main() {
    val N = 100
    val isPrime = (0..N).map { true }.toMutableList()
    isPrime[0] = false;  isPrime[1] = false
    var p=2
    while (p*p<=N) {
        print("$p, ");  out.flush()
        var m = 2*p
        while (m<=N) {
            isPrime[m] = false
            m +=p
        }
        p++
        while (p<=N&& !isPrime[p])
            p++
    }
    while (p<=N) {
        while (p<=N && !isPrime[p])
            p++
        if (p<=N)
            print("$p, ")
        p++
    }
    println("...")
}
