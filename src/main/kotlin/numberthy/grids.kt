package numberthy

import common.math.ipow

typealias Grid = ULong

fun main() {
    val dim = 3.toUByte()
    for (digit in 0u..9u) {
        var last = listOf<Grid>()
        for (depth in 0u..30u) {
            val preorbit = preorbit(digit*111_111_111uL, dim, depth.toUByte())
            if (preorbit == last) {
                println("done.")
                break
            }
            println("pre-Orbit of $digit..$digit is: $preorbit")
            last = preorbit
        }
        if (123_456_789uL in last) {
            println("Found the starting point in the preorbit of $digit.")
            break
        }
    }
}

fun orbit(start :Grid, dim :UByte, depth :UByte) :List<Grid> {
    val result = mutableListOf<Grid>()
    val opens = mutableListOf(-start.toLong());  val considereds = mutableSetOf(start)
    var level = 0.toUByte()
    while (opens.isNotEmpty()) {
        val nextLevel = opens.first()<0
        val current = abs(opens.removeFirst())
        result.add(current)
        if (nextLevel) {
            level++
            if (level>depth)
                break
        }
        (1u..dim*dim).forEach { i ->
            val next :Grid = mutate(current, dim, i.toUByte())
            if (next !in considereds) {
                opens.add(if (i==1u && nextLevel) -next.toLong() else next.toLong())
                considereds.add(next)
            }
        }
    }
    return result
}

fun preorbit(end :Grid, dim :UByte, depth :UByte) :List<Grid> {
    val result = mutableListOf<Grid>()
    val opens = mutableListOf(-end.toLong());  val considereds = mutableSetOf(end)
    var level = 0.toUByte()
    var postponed = false
    while (opens.isNotEmpty()) {
        val nextLevel = opens.first()<=0L
        val current = abs(opens.removeFirst())
        result.add(current)
        if (nextLevel) {
            level++
            if (level>depth)
                break
        }
        val news :List<Grid> = (1u..dim*dim).flatMap { i ->
            reverse(current, dim, i.toUByte()).toList().filter { it!=0uL && it !in considereds }
        }
        if ((nextLevel||postponed) && news.isNotEmpty()) {
            val first = news.first()
            opens.add(-first.toLong())
            opens.addAll(news.drop(1).map { it.toLong() })
            postponed = false
        } else if (nextLevel) {
            postponed = true
        }
        considereds.addAll(news)
    }
    return result
}

fun mutate(current :Grid, dim :UByte, pos :UByte) :Grid {
    val v = ((current / 10uL.ipow((pos - 1u).toUByte())) % 10uL).toUByte()
    return when {
        pos.toInt()==1 -> current
            .add(pos, v).add(2u, v)
            .add((dim + 1u).toUByte(), v)

        pos==dim -> current
            .add((pos-1u).toUByte(), v).add(pos, v)
            .add((pos+dim).toUByte(), v)

        pos < dim -> current
            .add((pos-1u).toUByte(), v).add(pos, v).add((pos+1u).toUByte(), v)
            .add((pos + dim).toUByte(), v)

        pos.toUInt()==dim*(dim-1u)+1u -> current
            .add((pos-dim).toUByte(), v)
            .add(pos, v).add((pos+1u).toUByte(), v)

        pos.toUInt()==dim*dim -> current
            .add((pos-dim).toUByte(), v)
            .add((pos-1u).toUByte(), v).add(pos, v)

        dim*(dim-1u) < pos -> current
            .add((pos-dim).toUByte(), v)
            .add((pos-1u).toUByte(), v).add(pos, v).add((pos+1u).toUByte(), v)

        pos%dim==0u -> current
            .add((pos-dim).toUByte(), v)
            .add((pos-1u).toUByte(), v).add(pos, v)
            .add((pos+dim).toUByte(), v)

        pos%dim==1u -> current
            .add((pos-dim).toUByte(), v)
            .add(pos, v).add((pos+1u).toUByte(), v)
            .add((pos+dim).toUByte(), v)

        else -> current
            .add((pos-dim).toUByte(), v)
            .add((pos-1u).toUByte(), v).add(pos, v).add((pos+1u).toUByte(), v)
            .add((pos+dim).toUByte(), v)
    }
}

fun reverse(current :Grid, dim :UByte, pos :UByte) :Pair<Grid,Grid> {
    val v = ((current / 10uL.ipow((pos - 1u).toUByte())) % 10uL).toUByte()
    if (v%2u==1u)
        return Pair(0uL, 0uL)
    val s1 = (v/2u).toUByte();  val s2 = ((v+10u)/2u).toUByte()
    return when {
        pos.toInt()==1 -> Pair(current
            .subtr(pos, s1).subtr(2u, s1)
            .subtr((dim + 1u).toUByte(), s1),
            current
                .subtr(pos, s2).subtr(2u, s2)
                .subtr((dim + 1u).toUByte(), s2))

        pos==dim -> Pair(current
            .subtr((pos-1u).toUByte(), s1).subtr(pos, s1)
            .subtr((pos+dim).toUByte(), s1),
            current
                .subtr((pos-1u).toUByte(), s2).subtr(pos, s2)
                .subtr((pos+dim).toUByte(), s2)
        )

        pos < dim -> Pair(current
            .subtr((pos-1u).toUByte(), s1).subtr(pos, s1).subtr((pos+1u).toUByte(), s1)
            .subtr((pos + dim).toUByte(), s1),
            current
                .subtr((pos-1u).toUByte(), s2).subtr(pos, s2).subtr((pos+1u).toUByte(), s2)
                .subtr((pos + dim).toUByte(), s2)
        )

        pos.toUInt()==dim*(dim-1u)+1u -> Pair(current
            .subtr((pos-dim).toUByte(), s1)
            .subtr(pos, s1).subtr((pos+1u).toUByte(), s1),
            current
                .subtr((pos-dim).toUByte(), s2)
                .subtr(pos, s2).subtr((pos+1u).toUByte(), s2)
        )

        pos.toUInt()==dim*dim -> Pair(current
            .subtr((pos-dim).toUByte(), s1)
            .subtr((pos-1u).toUByte(), s1).subtr(pos, s1),
            current
                .subtr((pos-dim).toUByte(), s2)
                .subtr((pos-1u).toUByte(), s2).subtr(pos, s2)
        )

        dim*(dim-1u) < pos -> Pair(current
            .subtr((pos-dim).toUByte(), s1)
            .subtr((pos-1u).toUByte(), s1).subtr(pos, s1).subtr((pos+1u).toUByte(), s1),
            current
                .subtr((pos-dim).toUByte(), s2)
                .subtr((pos-1u).toUByte(), s2).subtr(pos, s2).subtr((pos+1u).toUByte(), s2)
        )

        pos%dim==0u -> Pair(current
            .subtr((pos-dim).toUByte(), s1)
            .subtr((pos-1u).toUByte(), s1).subtr(pos, s1)
            .subtr((pos+dim).toUByte(), s1),
            current
                .subtr((pos-dim).toUByte(), s2)
                .subtr((pos-1u).toUByte(), s2).subtr(pos, s2)
                .subtr((pos+dim).toUByte(), s2)
        )

        pos%dim==1u -> Pair(current
            .subtr((pos-dim).toUByte(), s1)
            .subtr(pos, s1).subtr((pos+1u).toUByte(), s1)
            .subtr((pos+dim).toUByte(), s1),
            current
                .subtr((pos-dim).toUByte(), s2)
                .subtr(pos, s2).subtr((pos+1u).toUByte(), s2)
                .subtr((pos+dim).toUByte(), s2)
        )

        else -> Pair(current
            .subtr((pos-dim).toUByte(), s1)
            .subtr((pos-1u).toUByte(), s1).subtr(pos, s1).subtr((pos+1u).toUByte(), s1)
            .subtr((pos+dim).toUByte(), s1),
            current
                .subtr((pos-dim).toUByte(), s1)
                .subtr((pos-1u).toUByte(), s1).subtr(pos, s1).subtr((pos+1u).toUByte(), s1)
                .subtr((pos+dim).toUByte(), s1)
        )
    }
}

fun ULong.add(pos :UByte, value :UByte) :Grid {
    val mask = 10uL.ipow((pos-1u).toUByte())
    val v = (this / mask) % 10uL
    return if (v+value>10u)
        this - (10u-value)* mask
      else
        this + value* mask
}

fun ULong.subtr(pos :UByte, value :UByte) :Grid {
    val mask = 10uL.ipow((pos-1u).toUByte())
    val v = (this / mask) % 10uL
    return if (10u+v-value<10u)
        this + (10u-value)* mask
      else
        this - value* mask
}
