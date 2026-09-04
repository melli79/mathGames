package common

infix fun ClosedFloatingPointRange<Double>.step(step :Double) :Sequence<Double> {
    require(step > 0.0) { "step must be positive" }
    return generateSequence(start) { if (it < endInclusive) it + step else null }
}