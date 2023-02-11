package crypto

import java.io.File
import java.io.FileWriter
import java.util.*

private val encoder = "XZACEGIKMOQSUWYBDFHJLNPRTV".mapIndexed { i, c -> Pair('A'+i, c) }.toMap()

fun main() {
    print("Substitution Cipher")
    val fileName = "sample.txt"
    val message = File("src/main/resources/$fileName").readLines().joinToString("")
        // readlnOrNull()?.trim()
//    if (message.isNullOrEmpty())
//        return
    println("Message: $message")
    val encrypted = message.uppercase(Locale.getDefault())
        .filter { it in 'A'..'Z' }
        .mapIndexed { i, c -> Pair(i, encoder[c]?:'.') }
        .groupBy { it.first/40 }.entries.sortedBy { it.key }
        .map { it.value.joinToString(""){it.second.toString()} }
        .joinToString("\n")
    println("The encrypted message is: $encrypted")
    with(FileWriter("encrypted.txt")) {
        write(encrypted)
        close()
    }
    println("done.")
}
