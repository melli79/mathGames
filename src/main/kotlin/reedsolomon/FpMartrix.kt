package reedsolomon

@OptIn(ExperimentalUnsignedTypes::class)
data class FpMartrix internal constructor(val p :ULong, val n :UInt, val entries :ULongArray) {
    companion object {
        fun of(p :ULong, n :UInt, entries :List<ULong>) :FpMartrix {
            assert(p>1uL && (p==2uL || p%2uL>0uL))
            assert(entries.size.toUInt()%n == 0u)
            val m = (entries.size.toUInt()+n-1u)/n
            return FpMartrix(p, n, entries.map { c -> c%p }.toULongArray())
        }
    }

    val m = entries.size.toUInt()/n

    override fun toString() = entries.mapIndexed { i: Int, c: ULong -> Pair(i.toUInt(), c) }
        .groupBy { p -> p.first/n }.entries
        .joinToString("\n") { (_, row) -> row.joinToString { (_, c :ULong) -> c.toString() } }

    fun det() :Fp {
        assert(m==n)
        return when (n) {
            1u -> Fp(p, entries[0])
            2u -> Fp.of(p, (entries[0]*entries[3]).toLong() -(entries[1]*entries[2]).toLong())
            3u -> {
                val a0 = entries[0];  val a1=entries[1];  val a2 = entries[2]
                val b0 = entries[3];  val b1=entries[4];  val b2 = entries[5]
                val c0 = entries[6];  val c1=entries[7];  val c2 = entries[8]
                Fp.of(p, ((a0*b1*c2)%p +(a1*b2*c0)%p +(a2*b0*c1)%p).toLong() -
                        ((a2*b1*c0)%p +(a1*b0*c2)%p +(a0*b2*c1)%p).toLong())
            }

            else -> TODO("unimplemented")
        }
    }
}
