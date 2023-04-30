package probability

import kotlin.random.Random

private val random = Random(System.currentTimeMillis())

fun estimateMultiBirthdayP(n0 :UInt, n :UShort, reps :UShort) :Double {
    var number = 0
    var positives = 0
    for (i in 1..10_000) {
        val nums = (1..n.toInt()).map { random.nextInt(n0.toInt()) }
        if (nums.hasMultiple(reps))
            positives++
        number++
    }
    return positives.toDouble()/number
}

private fun <E> Collection<E>.hasMultiple(reps: UShort): Boolean {
    val repetitions = mutableMapOf<E, UShort>()
    for (e in this) {
        val num = ((repetitions[e] ?: 0u) +1u).toUShort()
        if (num >= reps)
            return true
        repetitions[e] = num
    }
    return false
}

fun estimateRepetitionsOfMultiBirthday(n0: UInt, n: UShort): Double {
    var number = 0u
    var reps = 0u
    for (i in 1..10_000) {
        val nums = (1..n.toInt()).map { random.nextInt(n0.toInt()) }
        val repetitions = repetitions(nums)
        reps += repetitions.entries.sumOf { it.key.toUInt()*it.value }
        number += repetitions.values.sum()
    }
    return reps.toDouble() / number.toInt()
}

fun <E> repetitions(nums :Collection<E>) :Map<UShort, UInt> {
    val reps = nums.groupBy { it }.map { Pair(it.value.size, it.key) }
    return reps.groupBy { it.first }.map { Pair(it.key.toUShort(), it.value.size.toUInt()) }.toMap()
}

fun estimateHighMutliRepetitionsP(n0: UInt, n: UShort): Double {
    var number = 0u
    var manyReps = 0u
    for (i in 1..10_000) {
        val nums = (1..n.toInt()).map { random.nextInt(n0.toInt()) }
        val highReps = repetitions(nums).entries.filter { it.key>2u }.sumOf { it.value }
        if (highReps>1u)
            manyReps++
        number++
    }
    return manyReps.toDouble() / number.toInt()
}
