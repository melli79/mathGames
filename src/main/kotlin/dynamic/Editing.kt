package dynamic

fun computeHammingDistance(src :String, target :String) :UByte {
    return computeEditSteps(src, target).size.toUByte()
}

fun computeEditSteps(src :String, target :String, si0 :Int =0, di0 :Int =0) :List<Edit> {
    var si = 0;  var di = 0
    while (si<src.length && di<target.length) {
        if (src[si]==target[di]) {
            si++; di++
            continue
        }
        val ins = listOf(Edit.Insert((si+si0).toUInt(), target[di])) +
                computeEditSteps(src.substring(si), target.substring(di+1), si0+si+1, si0+di+1)
        val dels = listOf(Edit.Delete((si+si0).toUInt())) +
                computeEditSteps(src.substring(si+1), target.substring(di), si0+si, di0+di)
        val repls = listOf(Edit.Replace((si+si0).toUInt(), target[di])) +
                computeEditSteps(src.substring(si+1), target.substring(di+1), si0+si+1, di0+di+1)
        return when {
            ins.size<=dels.size && ins.size<=repls.size -> ins
            dels.size<=repls.size -> dels
            else -> repls
        }
    }
    return if (si<src.length)
        src.substring(si).map { Edit.Delete((si0+si).toUInt()) }
    else if (di<target.length)
        target.substring(di).mapIndexed() { i, c -> Edit.Insert((di0+di+i).toUInt(), c) }
    else
        emptyList()
}

fun patch(src :String, steps :List<Edit>) :String {
    var next = src
    for (step in steps)
        next = next.substring(0 until step.pos.toInt()) +when (step) {
            is Edit.Insert -> step.c+""+next[step.pos.toInt()]
            is Edit.Delete -> ""
            is Edit.Replace -> step.c
    }+ next.substring(step.pos.toInt()+1)
    return next
}

sealed interface Edit {
    val pos :UInt

    data class Insert(override val pos :UInt, val c :Char) :Edit {}

    data class Delete(override val pos :UInt) :Edit {}

    data class Replace(override val pos :UInt, val c :Char) :Edit {}
}
