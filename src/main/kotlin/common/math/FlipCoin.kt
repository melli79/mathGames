package common.math

import kotlin.random.Random

fun Random.flipCoin(bias :Double =0.5) = nextDouble() < bias