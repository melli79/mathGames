package xmasaoc

fun run(input :String) :Long {
    var result = 0L
    for (match in "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex().findAll(input)) {
        result += mul(match)
    }
    return result
}

private fun mul(match :MatchResult) :Int {
    val first = match.groups[1]!!.value;  val second = match.groups[2]!!.value
    try {
        return first.toInt()*second.toInt()
    } catch (_ :NumberFormatException) {
        println("format error in: '$first', '$second'")
    }
    return 0
}

fun doMulDont(input :String) :Long {
    var result = 0L
    var enabled = true
    for (match in "do\\(\\)|mul\\((\\d{1,3}),(\\d{1,3})\\)|don't\\(\\)".toRegex().findAll(input)) {
        when (match.groups[0]!!.value.slice(0..< 4)) {
            "do()" -> enabled = true
            "don'" -> enabled = false
            else -> if (enabled) result += mul(match)
        }
    }
    return result
}

fun main() {
    // val input = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"
    // val input :String = getInput(null, "2403mul.txt").toReader().use{ it.readText() }
    val input = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"
    val result = run(input)
    println("The result is $result.")
    val res2 = doMulDont(input)
    println("The do mul result is $res2.")
}
