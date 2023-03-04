package numberthy

import kotlin.math.pow

fun generateDoubleLinear(limit :Long =1000L, a1 :UShort =2u, a2 :UShort =3u) :List<Long> {
    val generators = mutableSetOf(1L)
    val result = mutableListOf<Long>()
    while (generators.isNotEmpty()) {
        val x = generators.min(); generators.remove(x)
        result.add(x)
        val y = x* a1.toInt() +1L;  val z = x * a2.toInt() +1L
        if (y>limit||z>limit)
            break
        generators.add(y);  generators.add(z)
    }
    result.addAll(generators.filter { it<=limit }.sorted())
    return result
}

fun getElement23(index :Int) :Long {
    val order = 1.336*index.toDouble().pow(1.269220905243565) +10
    assert(order<Long.MAX_VALUE)
    return generateDoubleLinear(order.toLong())[index]
}
