package xmasaoc

import java.io.BufferedReader

private const val RESOURCE_NAME = "07lsR.txt"

private const val debug = false

fun main(args: Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME).toReader()
    if (!input.ready())
        throw IllegalStateException("Cannot read input.")

    val line = input.readLine().trim().split("\\s+".toRegex())
    assert(line[0]=="$"&&line[1]=="cd")
    val fileTree :Dir = parseDir(input, line[2])
    if (debug)
        fileTree.lsR()
    else
        println("Parsed $numDirs directories.")
    val smallDirs = fileTree.findSmallDirs()
    println("There are ${smallDirs.size} small directories of a total (doubly counted) size of ${smallDirs.sumOf { it.totalSize }} Bytes.")
    val dirs = fileTree.findDirs().sortedBy { it.totalSize }
    val diskSize = 70_000_000u
    val requiredSpace = 30_000_000u
    val currentFree = diskSize -fileTree.totalSize
    val threshold = requiredSpace -currentFree
    val last = dirs.last { it.totalSize < threshold }
    println(dirs)
    val first = dirs.firstOrNull { it.totalSize >= threshold }
    println("The cut happens between $last and $first of size ${first?.totalSize} Bytes.")
}

private fun Dir.findSmallDirs() :List<Dir> = (if (isSmall) listOf(this)  else emptyList()) +
        getContent().filterIsInstance<Dir>().flatMap { it.findSmallDirs() }

private fun Dir.findDirs() :List<Dir> = listOf(this) +
        getContent().filterIsInstance<Dir>().flatMap { it.findDirs() }

private var numDirs = 0

private fun parseDir(input :BufferedReader, name :String) :Dir {
    val result = Dir(name)
    numDirs++
    println("parsing directory '$name'...")
    while (input.ready()) {
        val line = input.readLine().trim().split("\\s+".toRegex())
        when (line[0]) {
            "$" -> when (line[1]) {
                "cd" -> if (line[2]=="..") {
                    return result
                } else {
                    val subDir = parseDir(input, line[2])
                    if (subDir!=null)
                        result.add(subDir)
                    else
                        println("Error parsing subdirectory")
                }
            }
            "dir" -> if (debug) println("expecting directory '${line[1]}'")
            else -> if (line[0][0] in '1'..'9') {
                val size = line[0].toUInt()
                result.add(File(line[1], size))
                if (debug)
                    println("added file '${line[1]}'.")
            } else if (debug) {
                println("unknown line '${line.joinToString(" ")}'")
            }
        }
    }
    return result
}

private sealed interface DirItem {
    val name :String
    val totalSize :UInt
}

private data class File(override val name :String, override val totalSize :UInt) :DirItem {
    override fun toString() = "File: $name, $totalSize Bytes"
}

private class Dir(override val name :String) :DirItem {

    companion object {
        const val SMALL = 100_000u
    }

    override var totalSize = 0u
        private set

    val isSmall :Boolean
        get() = totalSize <= SMALL

    private var parent :Dir? =null

    private val content = mutableListOf<DirItem>()
    fun getContent() :List<DirItem> = content

    // W! each directory can be added only to one other directory
    fun add(item : DirItem) {
        totalSize += item.totalSize
        content.add(item)
        if (item is Dir)
            item.parent = this
        parent?.addSize(item.totalSize)
    }

    private fun addSize(size :UInt) {
        totalSize += size
        parent?.addSize(size)
    }

    override fun toString() = "Dir: '$name', $totalSize Bytes"
    fun lsR(indent :String ="") {
        println(indent+"- "+toString()+ if (isSmall) " (small)"  else " (large)")
        val newIndent = indent+"  "
        content.forEach { item -> when (item) {
            is File -> println("$newIndent- $item")
            is Dir -> item.lsR(newIndent)
        } }
    }
}
