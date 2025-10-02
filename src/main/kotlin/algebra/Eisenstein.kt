package algebra

import kotlin.math.sqrt

fun isPrime(n :Int) :Boolean {
    if (n in -1..1) return false
    if (n==2) return true
    if (n%2==0) return false
    var i = 3
    while (i*i<=n) {
        if (n%i==0) return false
        i += 2
    }
    return true
}

data class Eisenstein(val a :Int, val b :Int) {
    companion object {
        val ZERO = Eisenstein(0, 0)
        val ONE = Eisenstein(1, 0)
        val w = Eisenstein(0, 1)
        val wRe = -0.5
        val wIm = sqrt(3.0)/2
        val w2 = Eisenstein(-1, -1)
        val lambda = Eisenstein(1, -1)

        val units = listOf(ONE, -w2, w, -ONE, w2, -w)
    }

    override fun toString() = when {
        b==0 -> "$a"
        a==0 && b==1 -> "w"
        a==0 && b==-1 -> "-w"
        a==0 -> "%dw".format(b)
        b==1 -> "${a}+w"
        b==-1 -> "${a}-w"
        else -> "${a}%+dw".format(b)
    }

    override fun equals(other :Any?) = other is Eisenstein && a==other.a && b==other.b

    val re :Double get() = a.toDouble() + wRe*b
    val im :Double get() = wIm*b

    operator fun plus(s :Eisenstein) = Eisenstein(a+s.a, b+s.b)
    operator fun unaryMinus() = Eisenstein(-a, -b)
    operator fun minus(s :Eisenstein) = Eisenstein(a-s.a, b-s.b)
    operator fun times(f :Int) = Eisenstein(a*f, b*f)

    operator fun times(f :Eisenstein) = Eisenstein(a*f.a +w2.a*b*f.b, a*f.b + b*f.a +w2.b*b*f.b)

    fun conjugate() = Eisenstein(a+w2.a*b, w2.b*b)
    fun norm() = a*a -a*b + b*b

    fun isPrime() :Boolean? = when {
        this==ZERO || this in units -> false
        this==lambda || this==w*lambda || this==w2*lambda -> true
        isPrime(norm()) -> true
        else -> null
    }

    override fun hashCode() = b.hashCode() + 31*a.hashCode()
}
