package sequences

fun fib(n :UByte) :ULong {
    var f_1 = 0uL;  var fk = 1uL
    if (n.toInt()==0)
        return f_1
    for (k in 2u..n.toUInt()) {
        val fk1 = fk +f_1
        f_1 = fk;  fk = fk1
    }
    return fk
}

fun trib(n :UByte) :ULong {
    var fk = 1uL;  var f_1 = 0uL;  var f_2 = 0uL
    if (n.toInt()<1)
        return 0uL
    for (k in 2u..n.toUInt()) {
        val fk1 = fk +f_1 +f_2
        f_2 = f_1; f_1 = fk;  fk = fk1
    }
    return fk
}

fun polyb(n :UByte, deg :UByte =2u) :ULong {
    if (n.toInt()<=1)
        return n.toULong()
    val fs = (2u..deg.toUInt()).map { 0uL }.toMutableList();  fs.add(1uL)
    for (k in 2u..n.toUInt()) {
        fs.add(fs.sum())
        fs.removeAt(0)
    }
    return fs.last()
}
