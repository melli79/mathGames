package common.math

import kotlin.math.*

data class Complex(var re :Double, var im :Double) {
    companion object {
        val ZERO = Complex(0.0, 0.0)
        val ONE = Complex(1.0, 0.0)
        val I = Complex(0.0, 1.0)
        val uminusI = Complex(0.0, -1.0)
        val TWO = Complex(2.0, 0.0)
        val TEN = Complex(10.0, 0.0)
    }

    operator fun plus(s :Complex) = Complex(re+s.re, im+s.im)
    operator fun unaryMinus() = Complex(-re, -im)
    operator fun minus(s :Complex) = Complex(re-s.re, im-s.im)
    fun conj() = Complex(re, -im)

    operator fun times(f :Double) = Complex(re*f, im*f)
    operator fun times(f :Complex) = Complex(re*f.re-im*f.im, re*f.im+im*f.re)
    fun abs2() = re*re+im*im
    operator fun div(d :Complex) = times(d.conj())*(1/d.abs2())
    fun isnan() = re.isNaN() || im.isNaN() || re.isInfinite() || im.isInfinite()
}

fun exp(z :Complex) :Complex {
    val r = exp(z.re)
    return Complex(cos(z.im)*r, sin(z.im)*r)
}

fun cosh(z :Complex) = Complex(cosh(z.re)*cos(z.im), sinh(z.re)*sin(z.im))

fun sinh(z :Complex) = Complex(sinh(z.re)*cos(z.im), cosh(z.re)*sin(z.im))

fun cos(z :Complex) = cosh(z*Complex.I)
fun sin(z :Complex) = sinh(z* Complex.I) * Complex.uminusI

fun log(z :Complex) = Complex(ln(z.abs2())/2, atan2(z.im, z.re))

fun tanh(z :Complex) :Complex {
    val th = tanh(z.re)
    val t = tan(z.im)
    return Complex(th, t)/Complex(1.0,-th*t)
}

fun tan(z :Complex) = sin(z)/cos(z)
