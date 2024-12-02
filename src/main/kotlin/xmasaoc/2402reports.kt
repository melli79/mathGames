package xmasaoc

import java.io.BufferedReader
import java.io.InputStreamReader

fun isSave(report :List<Int>) :Boolean {
    if (report.size < 2) return true
    val first = report.first();  val second = report[1]
    if (first == second) return false
    return if (first < second) isSaveAscending(report)  else isSaveDescending(report)
}

val steps = listOf(1, 2, 3)

fun isSaveAscending(report :List<Int>) :Boolean {
    return report.windowed(2).all { (last, next) -> next - last in steps }
}

fun isSaveDescending(report :List<Int>) :Boolean {
    return report.windowed(2).all { (prev, next) -> prev-next in steps }
}

fun isQuiteSave(report :List<Int>) :Boolean = isQuiteSaveAscending(report) || isQuiteSaveDescending(report)

fun isQuiteSaveAscending(report :List<Int>) :Boolean {
    var index = 0
    for ((prev, next) in report.windowed(2)) {
        if (next-prev !in steps) return (if (index>0) isSave(listOf(report[index-1]) + report.drop(index+1))
                else isSave(report.drop(index+1))) ||
            isSaveAscending(listOf(report[index]) + report.drop(index+2))
        ++index
    }
    return true
}

fun isQuiteSaveDescending(report :List<Int>) :Boolean {
    var index = 0
    for ((prev, next) in report.windowed(2)) {
        if (prev-next !in steps) return (if (index>0) isSave(listOf(report[index-1]) + report.drop(index+1))
                else isSave(report.drop(index+1))) ||
            isSaveDescending(listOf(report[index]) + report.drop(index+2))
        ++index
    }
    return true
}

fun readReports(fileName :String) :List<List<Int>> = BufferedReader(InputStreamReader(getInput(null, fileName))).lines()
    .map { report ->
        val numbers = report.trim().split("\\s+".toRegex())
        numbers.map { it.toInt() }
    }.toList()

fun main() {
    // val reports = listOf(listOf(7,6,4,2,1), listOf(1,2,7,8,9), listOf(9,7,6,2,1), listOf(1,3,2,4,5), listOf(8,6,4,4,1), listOf(1,3,6,7,9))
    val reports = readReports("2402reports.txt")
    println("From ${reports.size} there are ${reports.count { isSave(it) }} save reports.")
    println(" ${reports.count { isQuiteSave(it) }} are quite save.")
}
