package common.math.statistics

fun List<Double>.cumsum() :List<Double> {
    var sum = 0.0
    val result = mutableListOf<Double>()
    for (s in this) {
        result.add(sum)
        sum += s
    }
    return result
}