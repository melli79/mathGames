package crypto

import java.util.*

private const val fileName = "encrypted.txt"

fun main() {
    println("Analyzing a substitution cipher")
    val encrypted = // File("src/main/resources/${fileName}").readLines()
        readlnOrNull()?.trim()?.uppercase(Locale.getDefault())
    if (encrypted.isNullOrEmpty())
        return
//    println("Read ${encrypted.size} lines.")
    val sanitized = encrypted.filter { it in 'A'..'Z' || it=='\n' }
    val statistics = computeStatistics(sanitized)
    println("The letter distribution is: "+ statistics.joinToString { it.first+": %.1f%%".format(it.second*100) })
    val decoder = mapOf(Pair(statistics[0].first, 'E'), Pair(statistics[1].first, 'N'),
        Pair(statistics[2].first, 'I')).toMutableMap()
    decoder['\n'] = '\n'
    while (true) {
        val decrypted = sanitized
            .map { decoder[it] ?: '.' }
            .joinToString("")
        println("Encrypted message:          $sanitized")
        println("Partially decryped message: $decrypted")
        print("Guessed some letter [encrypted decrypted]: ")
        val pair = readlnOrNull()?.trim()?.split("\\s+".toRegex())
        if (pair==null||pair.size!=2)
            return
        decoder[pair[0][0]] = pair[1][0]
    }
}

private fun deCaesar(
    statistics :List<Pair<Char, Int>>,
    encrypted :String
) {
    val shift = 26 + ('E' - statistics.first().first)
    val decrypted = encrypt(shift.toByte(), encrypted)
    println("guessed shift: $shift,  decrypted message: $decrypted")
}

private fun computeStatistics(encrypted :String) = encrypted
    .filter { it in 'A'..'Z' }
    .groupBy { it }
    .map { Pair(it.key, it.value.size) }
    .sortedByDescending { it.second }
    .normalize()
    .take(7)

private fun <E> List<Pair<E, Int>>.normalize() :List<Pair<E, Double>> {
    val total = sumOf { it.second }
    println("in total $total letters.")
    val f = if (total>0) 1.0/total  else 1.0
    return this.map { Pair(it.first, f*it.second) }
}

private fun bruteForce(encrypted :String) {
    for (shift in 1..25) {
        val decrypted = encrypt(shift.toByte(), encrypted)
        println("Shift $shift, decrypted message: $decrypted")
    }
}
