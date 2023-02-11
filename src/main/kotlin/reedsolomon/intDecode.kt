package reedsolomon

import common.math.epsilon
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

private const val debug = false
val random = Random(System.currentTimeMillis())

fun main() {
    println("Reed-Solomon de & encoding")
    val d = 3u
    val xs = (1u..(d+3u)).map { i -> if (i%2u==0u) -(i/2u).toDouble()  else (i/2u).toDouble() }
    println("Interpolation points: $xs")
    print("  Please enter a message: ")
    val msg = readlnOrNull()?.trim() ?: return
    if (msg.isEmpty())
        return
    val dMsg = msg.encodeToByteArray().map { c -> c.toDouble() }
    if (debug)
        println("pre encode: ${dMsg.map { it.roundToInt() }}")
    val k = (dMsg.size.toUInt() + d) / (d + 1u)
    val cs = Matrix(dMsg.pad(d+1u).toDoubleArray(), k)
    val encoded = encode(xs, d.toUShort(), cs)
    val eMsg = encoded.toList().map { y -> y.roundToInt() }
    println("Encoded message: $eMsg")
    for (p in listOf(0.0, 0.05, 0.1, 0.15, 0.2)) {
        println("%.0f%% noise:".format(p*100))
        val ys = Matrix(eMsg.map { y -> (y + noise(p)).toDouble() }.toDoubleArray(), k)
        val decoded = decode(xs, d.toUShort(), ys)
        println("Decoding status: ${decoded.second}")
        println("raw decoded message: ${decoded.first}")
        val recMsg = decoded.first.toList().map { y :Double -> y.roundToInt().toByte() }
            .toByteArray().toString(Charsets.UTF_8)
        println("Received message: $recMsg\n")
    }
}

private fun noise(p :Double) = if (random.nextDouble() < p) random.nextInt(-10, 10) else 0

private fun List<Double>.pad(d :UInt) :List<Double> {
    val excess = size % d.toInt()
    if (excess == 0)
        return this
    return this + (excess until d.toInt()).map { 0.0 }
}

// This happens in the sender.
fun encode(xs :List<Double>, d :UShort, cs :Matrix) :Matrix {
    val result = Matrix(DoubleArray(xs.size*cs.n.toInt()), cs.n)
    for (c in 0u until cs.n) {
        xs.forEachIndexed { r, x ->
            var s = 0.0
            for (k in (0u..d.toUInt()).reversed())
                s = s*x +cs[k, c]
            result[r.toUInt(), c] = s
        }
    }
    return result
}

// This is what the receiver needs to do.
fun decode(xs :List<Double>, d :UShort, ys :Matrix) :Pair<Matrix, List<Boolean>> {
    assert (xs.size.toUInt() == ys.m)
    val m = vanDerMonde(xs, d, ys.n)
    m.paste(0u, d+1u, ys, 0u until ys.m, 0u until ys.n)
    val result = Matrix(DoubleArray(((d+1u)*ys.n).toInt()), ys.n)
    val solved = (0u until ys.n).map { false }.toMutableList()
    for (swapRow in (0u..(d+1u)).reversed()) {
        m.swap(d+1u, swapRow)
        if (trySolve(result, solved, m,d))
            return Pair(result, solved)
        m.swap(d+1u, swapRow)
        if (swapRow>d+1u)
            continue
        m.swap(d+2u, swapRow)
        if (trySolve(result, solved, m,d))
            return Pair(result, solved)
        m.swap(d+2u, swapRow)
    }
    return Pair(result, solved)
}

private fun vanDerMonde(xs :List<Double>, d :UShort, pad :UInt =0u) :Matrix {
    val result = Matrix(DoubleArray(xs.size * (d+1u +pad).toInt()), d+1u +pad)
    xs.forEachIndexed { r, x ->
        var p = 1.0
        for (c in 0u..d.toUInt()) {
            result[r.toUInt(), c] = p
            p *= x
        }
    }
    return result
}

private fun trySolve(result :Matrix, solved :MutableList<Boolean>, m :Matrix, d :UShort) :Boolean {
    try {
        if (debug)
            println("Equations:\n$m")
        val u = gaussEliminate(m, d, limit= true)
        if (debug)
            println("Upper triangular form:\n$u")
        val guess = rEliminate(u, d)
        if (debug)
            println("guess: $guess")
        for (c in 0u until result.n)  if (!solved[c.toInt()]) {
            if (abs(guess[d+1u, c])<epsilon || abs(guess[d+2u, c])<epsilon) {
                result.paste(0u, c, guess, 0u..d.toUInt(), c..c)
                solved[c.toInt()] = true
            }
        }
        return solved.all { it }
    } catch(e :SingularityException) {
        println("Exception solving linear system!")
        // fall through
    }
    return false
}
