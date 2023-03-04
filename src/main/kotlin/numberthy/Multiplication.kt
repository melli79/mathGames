package numberthy

object Multiplication {
    private val steps = mutableListOf<UInt>(0u, 1u)
    private val numbers = mutableListOf<UInt>(0u, 0u)

    fun optimize(exponent :UInt) :Pair<UInt, UInt> {
        for (n in steps.size.toUInt()..exponent) {
            var step = n-1u;  var num = numbers[n.toInt()-1] +1u
            if (n%2u==0u) {
                val num1 = numbers[n.toInt()/2] +1u
                if (num1 < num) {
                    num = num1;  step = n/2u
                }
            }
            for (s1 in 2u .. (n/2u)) {
                val s2 = n - s1
                val num1 = merge(s1, s2) +1u
                if (num1<num) {
                    num = num1;  step = s2
                }
            }
            steps.add(step);  numbers.add(num)
        }
        return Pair(steps[exponent.toInt()], numbers[exponent.toInt()])
    }

    fun getSequence(exponent :UInt) :Set<UInt> {
        val result = mutableSetOf<UInt>()
        val opens = mutableSetOf(exponent)
        while (opens.isNotEmpty()) {
            val n = opens.max()
            val s1 = optimize(n).first;  val s2 = n-s1
            if (s1>1u)
                opens.add(s1)
            if (s2>1u)
                opens.add(s2)
            result.add(n)
            opens.remove(n)
        }
        val expected = optimize(exponent).second
        if (result.size.toUInt() != expected)
            println("Deviation for exponent $exponent: expected length $expected,  actual length: ${result.size}")
        return result
    }

    private fun merge(s1 :UInt, s2 :UInt) = (getSequence(s1) + getSequence(s2)).size.toUInt()

    fun quickPower(exponent :UInt) :Set<UInt> {
        var p = 0u;  var f = 1u
        var rem = exponent
        val result = mutableSetOf<UInt>()
        while (rem>0u) {
            if (rem%2u>0u) {
                if (p>0u) {
                    p += f; result.add(p)
                } else
                    p = f
            }
            rem /= 2u
            if (rem==0u)
                break
            f *= 2u;  result.add(f)
        }
        return result
    }
}
