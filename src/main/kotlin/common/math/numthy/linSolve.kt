// LU factorization over a field

@file:OptIn(ExperimentalUnsignedTypes::class)

package common.math.numthy

fun Matp.luFactor() :Pair<Matp, Matp> {
  val n = ms.size
  assert (n==ms[0].size)
  val u = copyOf()
  val l = Matp.zero(p, n, n)
  for (i in u.ms.indices) {
    l.ms[i][i] = u.ms[i][i]
    val f = divn(u.ms[i][i], p)
    u.ms[i][i] = 1u
    for (j in i+1..< u.ms[i].size)
      u.ms[i][j] = (u.ms[i][j] * f) % p
    for (k in i+1..< u.ms.size) {
      val f1 = (p-u.ms[k][i]) % p
      l.ms[k][i] = f1
      u.ms[k][i] = 0u
      for (j in i+1..< u.ms[k].size)
        u.ms[k][j] = (u.ms[k][j]+f*u.ms[i][j]) % p
    }
  }
  return Pair(l, u)
}

fun Matp.ldiv(b :Matp) :Matp {
  val n = ms.size
  assert (n==ms[0].size)
  assert (n==b.ms.size)
  val u = Matp(p, (ms zip b.ms).map { (r1, r2) ->
    (r1.toList()+r2.toList()).toTypedArray<UInt>().toUIntArray()
  }.toTypedArray())
  for (i in u.ms.indices) {
    val f = divn(u.ms[i][i], p)
    u.ms[i][i] = 1u
    for (j in i+1..< u.ms[i].size)
      u.ms[i][j] = (u.ms[i][j] * f) % p
    for (k in i+1..< u.ms.size) {
      val f1 = (p-u.ms[k][i]) % p
      u.ms[k][i] = 0u
      for (j in i+1..< u.ms[k].size)
        u.ms[k][j] = (u.ms[k][j] +f1*u.ms[i][j]) % p
    }
  }
  for (i in u.ms.indices.reversed()) {
    for (j in 0..< i) {
      val f = (p -u.ms[j][i]) % p
      u.ms[j][i] = 0u
      for (k in i+1..< u.ms[j].size)
        u.ms[j][k] = (u.ms[j][k] +f*u.ms[i][k]) % p
    }
  }
  return u.slice(ms.indices, n..< u.ms[0].size)
}

fun Matp.inv() :Matp {
  val n = ms.size
  assert(n==ms[0].size)
  return ldiv(Matp.one(p, n))
}
