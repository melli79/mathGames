@file:OptIn(ExperimentalUnsignedTypes::class)

import common.math.numthy.Matp
import common.math.numthy.Vecp
import common.math.numthy.inv
import common.math.numthy.matp
import kotlin.random.Random

fun caesar(text :String, key :Byte) = text.map { when (it) {
    in 'A'..'Z' -> 'A'+ (it-'A'+26+key)%26
    in 'a'..'z' -> 'a'+ (it-'a'+26+key)%26
    else -> it
}}.joinToString("")

val pad = "               "

fun mapp(text :String, m :Matp) :String {
  return text.chunked(m.ms.size).map { word ->
    val v = Vecp(m.p, (word+pad).take(m.ms.size).map {
      (it-' '+1).toUInt()
    }.toTypedArray<UInt>().toUIntArray());
    (m*v).vs.take(m.size.first-1).joinToString("") { (' '+it.toInt()-1).toString() }
  }.joinToString("")
}

fun map1(text :String, m :Matp) :String {
  val n = m.ms.size
  return text.chunked(n-1).map { word ->
    val v = Vecp(m.p, (word+pad).take(n).map {
      (it-' '+1).toUInt()
    }.toTypedArray<UInt>().toUIntArray());
    (m*v).vs.take(n-1).joinToString("") { (' '+it.toInt()-1).toString() }
  }.joinToString("")
}

fun main(args :Array<String>) {
  val random = Random(System.currentTimeMillis())
  val p = 97u
  val n = 3
  val src = "Hello World!"
  while (true) {
    val m0 = random.matp(p, n+1,n+1)
    for (c in 0..< n) m0.ms[n][c] = 0u
    m0.ms[n][n] = 1u
    try {
      m0.inv()
    } catch (e :AssertionError) {
      print(".");  System.out.flush()
      continue
    }
    println(m0)
    val encrypted = map1(src, m0)
    println(encrypted)
    val bs = Matp(p, encrypted.chunked(n).take(n+1).map { row ->
      (row + pad).take(n+1).map { (it-' '+1).toUInt() }.toTypedArray<UInt>().toUIntArray()
    }.toTypedArray()).transp()
    val xs = Matp(p, src.chunked(n).take(n+1).map { row ->
      (row + pad).take(n+1).map { (it-' '+1).toUInt() }.toTypedArray<UInt>().toUIntArray()
    }.toTypedArray()).transp()
    println(xs)
    val m = bs*xs.inv()
    assert(m.size == Pair(n+1, n+1))
    for (c in 0..< n) m.ms[n][c] = 0u
    m.ms[n][n] = 1u
    assert(m == m0)
    val encrypted2 = map1(src, m)
    println(encrypted2)
    assert(encrypted == encrypted2)
    val m1 = m.inv()
    println(m1)
    assert(m1.size == Pair(n+1, n+1))
    println(map1(encrypted, m1))
    break
  }
}
