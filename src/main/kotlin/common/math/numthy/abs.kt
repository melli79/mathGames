package common.math.numthy

fun abs(x :Int) :UInt = when {
  x>=0 -> x.toUInt()
  else -> (-x).toUInt()
}