package xmasaoc

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.abs

fun totalDifference(ones :List<Int>, twos :List<Int>) :Long {
    val sortedOnes = ones.sorted()
    val sortedTwos = twos.sorted()
    return (sortedOnes zip sortedTwos).sumOf { (one, two) -> abs(one-two).toLong() }
}

fun weightedSimilarity(ones :List<Int>, twos :List<Int>) :Long {
    return ones.sumOf { one -> one.toLong()*twos.count { it==one } }
}

fun readLists(fileName :String) :Pair<List<Int>, List<Int>> {
    val resOne = mutableListOf<Int>();  val resTwo = mutableListOf<Int>()
    var num = 1
    BufferedReader(InputStreamReader(getInput(null, fileName))).lines().forEach { line ->
        val numbers = line.trim().split("\\s+".toRegex())
        if (numbers.size==2) {
            resOne.add(numbers[0].toInt())
            resTwo.add(numbers[1].toInt())
        } else println("Error on line ${num}: $numbers")
    }
    return Pair(resOne, resTwo)
}

fun main() {
    val (ones, twos) = readLists("2401lists.txt")
    //val ones = listOf(3,4,2,1,3,3);  val twos = listOf(4,3,5,3,9,3)
    println("Total difference: ${totalDifference(ones, twos)}")
    println("Weighted similarity: ${weightedSimilarity(ones, twos)}") // = ||${ones.sorted()}-${twos.sorted()}|l_1||")
}
