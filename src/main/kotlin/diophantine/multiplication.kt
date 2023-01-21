package diophantine

fun main() {
    print("Bitte geben Sie den Modulus ein: ")
    var n :Int?
    while (true) {
        n = readlnOrNull()?.trim()?.toIntOrNull()
        if (n==null)
            return
        if (n<1) {
            println("Der Modulus muss positiv sein")
            continue
        }
        break
    }
    println("Multiplikation modulo $n:")
    for (y in 0 until n!!) {
        for (x in 0 until n)
            print("%2d ".format(x*y%n))
        println()
    }
}
