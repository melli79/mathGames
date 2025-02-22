package common.math.numthy

operator fun IntRange.contains(other :IntRange) = other.isEmpty() || contains(other.first) && contains(other.last)