package numberthy

object Multiplication {
    private val steps = mutableListOf(setOf(0u), setOf(1u))
    private val numbers = mutableListOf(0u, 0u)

    fun optimize(exponent :UInt) :Pair<Set<UInt>, UInt> {
        for (n in steps.size.toUInt()..exponent) {
            var options = mutableSetOf(n-1u);  var num = numbers[n.toInt()-1] +1u
            if (n%2u==0u) {
                val num1 = numbers[n.toInt()/2] +1u
                if (num1==num) {
                    options.add(n/2u)
                } else if (num1 < num) {
                    num = num1;  options = mutableSetOf(n/2u)
                }
            }
            for (s1 in 2u .. (n/2u)) {
                val s2 = n - s1
                val num1 = merge(s1, s2) +1u
                if (num1==num) {
                    options.add(s2)
                } else if (num1<num) {
                    num = num1;  options = mutableSetOf(s2)
                }
            }
            steps.add(options);  numbers.add(num)
        }
        return Pair(steps[exponent.toInt()], numbers[exponent.toInt()])
    }

    fun getSequences(exponent :UInt) :Set<Set<UInt>> {
        if (exponent<2u)
            return setOf(emptySet())
        val (s1s, min) = optimize(exponent)
        return s1s.flatMap { s1 ->
            val s2 = exponent - s1
            val c1s = getSequences(s1)
            val c2s = getSequences(s2)
            c1s.flatMap { c1 -> c2s.map { c2 ->
                setOf(exponent)+c1+c2
            }}.filter { s -> s.size.toUInt()<=min }
        }.toSet()
    }

    private fun merge(s1 :UInt, s2 :UInt) = getSequences(s1).flatMap { c1 ->
        getSequences(s2).map { c2 -> (c1+c2).size.toUInt() }
    }.min()

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
