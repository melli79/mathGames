package crypto

import java.util.*

fun main() {
    print("Polychiffre analysieren\n  Bitten geben Sie den verschlüsselten Text ein: ")
    val encrypted = readlnOrNull()?.trim()
    if (encrypted.isNullOrEmpty())
        return
    print("Bitte geben Sie den Anfang vom vermuteten Klartext ein: ")
    val fragment = readlnOrNull()?.trim()
    if (fragment.isNullOrEmpty())
        return
    val nonce = polyAnalyze(encrypted, fragment)
    println("Der Anfang vom Hilfstext lautet: $nonce")
}

fun polyAnalyze(encrypted :String, fragment :String) = encrypted
    .uppercase(Locale.getDefault())
    .filter { it in 'A'..'Z' }
    .zip(fragment.uppercase(Locale.getDefault()).filter { it in 'A'..'Z' })
    .map { (e, f) -> letters[(e-f+26)%26] }
    .joinToString("")
