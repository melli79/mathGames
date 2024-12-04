package numberthy

import java.lang.System.out

fun main() {
    val N = 1000
    val isPrime = (0..N/2).map { true }.toMutableList()
    isPrime[0] = false;  isPrime[1] = false
    print("2, ")
    var p=3
    while (p*p<=N) {
        print("$p, ");  out.flush()
        var m = p*p
        while (m<=N) {
            isPrime[m/2] = false
            m += 2*p
        }
        p+=2
        while (p<=N&& !isPrime[p/2])
            p+=2
    }
    while (p<=N) {
        while (p<=N && !isPrime[p/2])
            p+=2
        if (p<=N)
            print("$p, ")
        p+=2
    }
    println("...")
}
