package crypto

import java.io.File
import java.util.*

private const val fileName = "sample.txt"

private val nonce = """Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
    | incididunt ut labore et dolore magna aliqua. Mi bibendum neque egestas congue quisque egestas.
    | Potenti nullam ac tortor vitae purus faucibus ornare suspendisse sed. Morbi tristique senectus
    | et netus et. Eu tincidunt tortor aliquam nulla facilisi cras. Sapien pellentesque habitant
    | morbi tristique senectus et. Feugiat in fermentum posuere urna nec tincidunt praesent semper 
    | feugiat. Praesent tristique magna sit amet. In iaculis nunc sed augue lacus viverra vitae 
    | congue eu. Consectetur adipiscing elit duis tristique. Arcu bibendum at varius vel. Vestibulum 
    | rhoncus est pellentesque elit ullamcorper dignissim. Molestie nunc non blandit massa enim nec 
    | dui nunc mattis. Sociis natoque penatibus et magnis. Mauris a diam maecenas sed enim. Orci a scelerisque purus semper. Et odio pellentesque diam volutpat commodo sed egestas egestas 
    | fringilla. In iaculis nunc sed augue lacus viverra vitae. Etiam non quam lacus suspendisse 
    | faucibus interdum posuere lorem. Eget mauris pharetra et ultrices neque ornare aenean 
    | euismod.""".trimMargin()
    .uppercase(Locale.ITALIAN).filter { it in 'A'..'Z' }

fun main() {
    print("Polychiffe\n  Bitte geben Sie den zu verschlüsselnden Text ein: ")
    val message = File("src/main/resources/${fileName}").readLines()// readlnOrNull()?.trim()
//    if (message.isNullOrEmpty())
//        return
    val encrypted = polycrypt(message.joinToString("\n"), nonce)
    println("Die verschlüsselte Nachricht lautet: $encrypted")
}

fun polycrypt(message :String, nonce :String) = message
    .uppercase(Locale.getDefault())
    .filter { it in 'A'..'Z' || it=='\n' }
    .mapIndexed { i, c -> if (c=='\n') c  else
        letters[(c-'A'+(nonce[i%nonce.length]-'A'))%26] }
    .joinToString("")
