package trivia

private const val fizz = 2
private const val buzz = 3

fun fizzBuzz(limit :Short) = (1..limit).map { when {
    it % (fizz*buzz) == 0 -> "fizz buzz"
    it % fizz == 0 -> "fizz"
    it % buzz == 0 -> "buzz"
    else -> it.toString()
} }
