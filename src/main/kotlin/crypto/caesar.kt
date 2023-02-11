package crypto

import java.util.*

fun main() {
    print("Caesar cipher\n  Please enter the shift (0-25): ")
    val shift = readlnOrNull()?.trim()?.toByteOrNull()
    if (shift==null) {
        println("That's not a valid shift")
        return
    }
    while (true) {
        print("Please enter your message: ")
        val message = readlnOrNull()?.trim()?.uppercase(Locale.getDefault())
        if (message.isNullOrEmpty())
            return
        val encrypted = encrypt((26 + shift.toInt() % 26).toByte(), message)
        println("Your encrypted message is: $encrypted")
    }
}

const val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

fun encrypt(shift :Byte, message :String) = message
    .filter { it in 'A'..'Z' }
    .map { c -> letters[(c-'A'+shift)%26] }
    .joinToString("")
