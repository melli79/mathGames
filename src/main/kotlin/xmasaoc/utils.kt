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

open class Matrix<F>(values0 :List<List<F>> =emptyList(), val zero :F) {

    private val values = values0.map { row -> row.toMutableList() }.toMutableList()

    val numRows :UInt
        get() = values.size.toUInt()

    val numCols :UInt
        get() = values[0].size.toUInt()

    init {
        for (row in getRows()) {
            assert(numCols == row.size.toUInt())
        }
    }

    override fun toString() = values.joinToString("]\n [", prefix= " [", postfix= "]") { row -> row.joinToString(" ") { v -> "$v" } }

    fun getRows() :List<List<F>> = values

    fun copy() = Matrix(values, zero)

    fun add(row :List<F>) {
        values.add(row.toMutableList())
    }

    fun getRow(i :UInt) :List<F> = values[i.toInt()]
    fun getColumn(j :UInt) = values.map { row -> row[j.toInt()] }

    operator fun get(i :UInt, j :UInt) = values[i.toInt()][j.toInt()]
    open operator fun get(i :Int, j :Int) = values[i][j]

    operator fun set(i :UInt, j :UInt, value :F) {
        values[i.toInt()][j.toInt()] = value
    }

    open operator fun set(i :Int, j :Int, value :F) {
        values[i][j] = value
    }

    fun set(newValues :Matrix<F>) {
        values.clear()
        newValues.getRows().forEach { row ->
            values.add(row.toMutableList())
        }
    }

    open fun replace(r :Int, c :Int, part :List<F>) {
        val oldRow = values[r]
        values[r] = (oldRow.subList(0, c) +part +oldRow.subList(c+part.size, numCols.toInt())).toMutableList()
    }
}
