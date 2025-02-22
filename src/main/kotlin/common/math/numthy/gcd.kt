package common.math.numthy

fun gcd(a :UInt, b :UInt) :UInt {
  var a0 = a;  var a1 = b
  while (a1>0u) {
    val a2 = a0 % a1
    a0 = a1;  a1 = a2
  }
  return a0
}

fun divn(a :UInt, n :UInt) :UInt {
  assert (a>0u)
  var a0 = n;  var a1 = a
  var f0 = 1;  var f1 = 0
  var g0 = 0;  var g1 = 1
  while (a1>0u) {
    val q = a0 / a1
    val a2 = (a0 -q*a1).toUInt()
    val h0 = f0 -q.toInt()*g0
    val h1 = f1 -q.toInt()*g1
    a0 = a1;  f0 = g0;  f1 = g1
    a1 = a2;  g0 = h0;  g1 = h1
  }
  assert (a0==1u)
  return if (f1>=0) f1.toUInt()  else (n.toInt()+f1).toUInt()
}