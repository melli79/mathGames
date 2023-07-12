import numberthy.abs

fun grayDecode(input :String) :ULong {
  var result = 0uL
  var i = 0
  val len = input.length-1
  for ((k, d) in input.mapIndexed { k, d -> Pair(len-k, d) }) {
    if (d=='1') {
      val value = 2uL.ipow((k+1).toUByte())-1uL
      if (i%2==0)
        result += value
      else
        result -= value
      i++
    }
  }
  return result
}

fun grayEncode(n :ULong) :String {
  if (n==0uL)
    return "0"
  var r = n.toLong()
  var sgn = 1
  var lastK = 0.toByte()
  var result = ""
  while (r!=0L) {
    val k = ilog(abs(r))
    for (pos in k+1 until lastK) {
      result += "0"
    }
    result += "1"
    val value = 2uL.ipow((k+1).toUByte()) -1uL
    r -= sgn * value.toLong()
    sgn = -sgn
    lastK = k
  }
  for (pos in 0 until lastK) {
    result += "0"
  }
  return result
}

fun ilog(x :ULong) :Byte {
  if (x==0uL)
    return -1
  var result = 0.toByte()
  var r = x
  while (r>1uL) {
    result++
    r /= 2uL
  }
  return result
}

fun ULong.ipow(e :UByte) :ULong {
  var r = e.toInt()
  if (r==0)
    return 1uL
  if (this<=1uL)
    return this
  var result = 1uL
  var p = this
  while (r>0) {
    if (r%2>0) {
      result *= p
    }
    p *= p
    r /= 2
  }
  return result
}
