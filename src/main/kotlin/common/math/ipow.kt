package common.math

fun ULong.ipow(e :UByte) :ULong = (1u..e.toUInt()).toList()
    .foldRight(1uL) { _, p :ULong -> p*this }