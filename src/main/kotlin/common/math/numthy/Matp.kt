package common.math.numthy

@OptIn(ExperimentalUnsignedTypes::class)
data class Matp(val p :UInt, val ms :Array<UIntArray>) {
  companion object {
    fun zero(p :UInt, r :Int, c :Int) = Matp(p, Array(r) { UIntArray(c) {0u} })

    fun one(p :UInt, n :Int) = Matp(p, Array<UIntArray>(n) { r ->
      UIntArray(n) { if(it==r) 1u  else 0u }
    })
  }

  override fun toString() = ms.joinToString(")\n(", prefix="(", postfix=") mod $p") {
    row -> row.joinToString() { it.toString() }
  }

  fun copyOf() = Matp(p, ms.map { it.copyOf() }.toTypedArray())

  fun slice(rs :IntRange, cs :IntRange) :Matp {
    assert(rs in ms.indices)
    assert(cs in ms[0].indices)
    return Matp(p, ms.slice(rs).map { it.slice(cs).toTypedArray<UInt>().toUIntArray() }.toTypedArray())
  }

  val size :Pair<Int, Int>
    get() = Pair(ms.size, ms[0].size)

  override fun equals(other :Any?) :Boolean {
    return other is Matp && p==other.p && size==other.size && (ms zip other.ms).all { (r1, r2) -> r1 contentEquals r2 }
  }

  fun transp() = Matp(p, ms[0].indices.map { r ->
    ms.indices.map { c -> ms[c][r] }.toTypedArray<UInt>().toUIntArray()
  }.toTypedArray())

  operator fun plus(s :Matp) = Matp(p, (ms zip s.ms).map { (s1, s2) ->
    (s1 zip s2).map { (u, v) -> (u+v) % p }.toTypedArray<UInt>().toUIntArray()
  }.toTypedArray())

  operator fun times(f :UInt) = Matp(p, ms.map { row ->
    row.map { (it*f) % p }.toTypedArray<UInt>().toUIntArray()
  }.toTypedArray())

  operator fun times(v :Vecp) = Vecp(p, ms.map { row ->
    (row zip v.vs).map { (s,t) -> (s*t) % p }.fold(0u) { s, t -> (s+t) % p  }
      }.toTypedArray<UInt>().toUIntArray())

  operator fun times(m :Matp) = Matp(p, ms.map { row ->
    m.ms[0].indices.map { c ->
      row.mapIndexed { k, mm :UInt -> (mm*m.ms[k][c]) %p }
        .fold(0u) { s, t -> (s+t) % p }
    }.toTypedArray<UInt>().toUIntArray()
  }.toTypedArray())

  override fun hashCode() = p.hashCode() +31*size.hashCode() +97*ms.contentHashCode()
}
