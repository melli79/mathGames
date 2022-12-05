package xmasaoc

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader

internal class Handle {}

const val RESOURCE_DIR = "src/main/resources"

fun InputStream.toReader() = BufferedReader(InputStreamReader(this))

fun getInput(fileName :String?, fallbackResource :String) = if (fileName!=null)
    FileInputStream(fileName)
  else
    Handle::class.java.getResourceAsStream(fallbackResource) ?: FileInputStream(
        File(
            File(RESOURCE_DIR),
            fallbackResource
        )
    )