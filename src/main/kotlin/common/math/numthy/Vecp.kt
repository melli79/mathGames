package common.math.numthy

@OptIn(ExperimentalUnsignedTypes::class)
data class Vecp(val p :UInt, val vs :UIntArray) {
  companion object {
    fun zero(p :UInt, n :Int) = Vecp(p, UIntArray(n) { 0u })
    fun e(p :UInt, k :Int, n :Int) = Vecp(p, UIntArray(n) { if (it == k) 1u else 0u })
  }

  override fun toString() = vs.joinToString(prefix="[", postfix="] mod $p") {
    it.toString()
  }

  fun copyOf() = Vecp(p, vs.copyOf())

  val size :Int
    get() = vs.size

  override fun equals(other :Any?) :Boolean {
    return other is Vecp && p==other.p && vs contentEquals other.vs
  }

  operator fun plus(s :Vecp) = Vecp(p, (vs zip s.vs).map { (s, t) -> (s + t)%p }
      .toTypedArray<UInt>().toUIntArray())

  operator fun times(f :UInt) = Vecp(p, vs.map { (it*f)%p }
      .toTypedArray<UInt>().toUIntArray())

  override fun hashCode() = p.hashCode() +31*size.hashCode() +97*vs.contentHashCode()
}
