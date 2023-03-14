package numberthy

import kotlin.math.log10

private val reflexiveDigits = setOf(0u, 1u, 2u, 5u, 8u)
private val mappingDigits = mapOf(Pair(6u, 9u), Pair(9u, 6u)) + reflexiveDigits.map { Pair(it, it) }.toMap()

@OptIn(ExperimentalUnsignedTypes::class)
fun generateReflectives(low :ULong, high :ULong) :List<String> {
    val lowMid = numDigits(low);  val highMid = numDigits(high)
    if (highMid<=lowMid+1u)
        return (low until high).map { it.toDigits() }
            .filter { isReflective(it) }
            .map { it.joinToString("") }
    return (low.. 10uL.ipow(lowMid)).map { it.toDigits() }
            .filter { isReflective(it) }
            .map { it.joinToString("") }  +
        (lowMid+1u until highMid.toUInt()).flatMap { d -> generateReflectives(d.toUByte()) }  +
        (10uL.ipow(highMid) until high).map { it.toDigits() }
            .filter { isReflective(it) }
            .map { it.joinToString("") }
}

fun numDigits(value :ULong) = if (value < 1uL) 1u  else (log10(value.toDouble()).toInt() +1).toUByte()

private fun ULong.ipow(e :UByte) :ULong = (1u..e.toUInt()).toList()
    .foldRight(1uL) { _, p :ULong -> p*this }

@OptIn(ExperimentalUnsignedTypes::class)
fun isReflective(number :UByteArray) :Boolean {
    for (i in 0 until number.size/2) {
        if (number[number.size-1-i].toUInt() != mappingDigits[number[i].toUInt()])
            return false
    }
    return number.size%2==0 || number[number.size/2].toUInt() in reflexiveDigits
}

@OptIn(ExperimentalUnsignedTypes::class)
private fun ULong.toDigits() :UByteArray {
    if (this==0uL)
        return ubyteArrayOf(0u)
    val result = mutableListOf<UByte>()
    var remainder = this
    while (remainder>0uL) {
        result.add(0, (remainder%10u).toUByte())
        remainder /= 10u
    }
    return result.toUByteArray()
}

@OptIn(ExperimentalUnsignedTypes::class)
fun generateReflectives(numDigits :UByte) :List<String> {
    if (numDigits==0.toUByte())
        return listOf("")
    if (numDigits==1.toUByte())
        return reflexiveDigits.map { it.toString() }
    val leftHalves = mutableListOf<UIntArray>()
    val digits = (1..numDigits.toInt()/2).map { 1u }.toUIntArray()
    var i = 1
    while (i>0) {
        while (i<numDigits.toInt()/2) {
            digits[i++] = 0u
        }
        if (i==numDigits.toInt()/2)
            leftHalves.add(digits.toUIntArray())
        i--
        while (i>=0) {
            val next = mappingDigits.keys.sorted().next(digits[i])
            if (next!=null) {
                digits[i++] = next
                break
            }
            i--
        }
    }
    return if (numDigits%2u==0u)
        leftHalves.map { left -> left.joinToString("")+reflect(left).joinToString("")}
    else
        leftHalves.flatMap { l ->
            val left = l.joinToString("");  val right = reflect(l).joinToString("")
            reflexiveDigits.map { d -> left+d+right }
        }
}

private fun <E> List<E>.next(value :E) :E? {
    val index = indexOf(value)
    return if (index>=0 && index+1<size)
        get(index+1)
      else
        null
}

@OptIn(ExperimentalUnsignedTypes::class)
fun reflect(number :UIntArray) = number.reversed().map { mappingDigits[it] }
