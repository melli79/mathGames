package numberthy

fun main() {
    print("Bitte geben Sie eine positive ganze Zahl für den Modulus ein: ")
    var n :Int?
    while (true) {
        n = readlnOrNull()?.trim()?.toIntOrNull()
        if (n==null || n<1) {
            print("Das ist keine positive Zahl größer 1.")
            continue
        }
        break
    }
    println("Potenzen modulo $n:")
    println((0..n!!).map { e -> "%2d".format(e) }.joinToString(" "))
    for (b in 0 until n) {
        var p = 1
        for (e in 0..n) {
            print("%2d ".format(p))
            p = (p*b)%n
        }
        println()
    }
}
