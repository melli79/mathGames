package xmasaoc

import java.io.FileInputStream
import java.io.InputStream

private const val RESOURCE_NAME = "01elfFood.txt"

fun main(args :Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)
    val foods = parseFoods(input)
    val result = foods.mapIndexed { elf, food -> Pair(elf+1, food.sum()) }
        .sortedByDescending { p -> p.second }.take(3)
    if (input is FileInputStream)
        input.close()
    if (result.isNotEmpty()) {
        println("The maximum calories is carried by elf ${result.first().first} with ${result.first().second} calories.")
        println("The first 3 elves carry ${result.sumOf { it.second }} calories in total.")
    } else
        println("No food was found.")
}

private fun parseFoods(inputStream :InputStream) :MutableList<List<Int>> {
    val input = inputStream.toReader()
    val result = mutableListOf<List<Int>>()
    var food = mutableListOf<Int>()
    input.forEachLine { line ->
        if (line.isBlank() && food.isNotEmpty()) {
            result.add(food)
            food = mutableListOf()
        } else {
            val calories = line.toInt()
            food.add(calories)
        }
    }
    if (food.isNotEmpty())
        result.add(food)
    return result
}
