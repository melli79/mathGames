package xmasaoc

fun <T> List<String>.options() = listOf<Function1<Triple<Int, Int, T>, Boolean>>({ t -> t.second+3<first().length &&
        get(t.first).slice(t.second+1 .. t.second+3) == "MAS"
    }, { t -> t.first+3 < size &&
        get(t.first+1)[t.second] == 'M' && get(t.first+2)[t.second] == 'A' && get(t.first+3)[t.second] == 'S'
    }, { t -> t.first+3 < size && t.second+3 < first().length &&
        get(t.first+1)[t.second+1] == 'M' && get(t.first+2)[t.second+2] == 'A' && get(t.first+3)[t.second+3] == 'S'
    }, { t -> t.first+3<size && t.second-3>=0 &&
        get(t.first+1)[t.second-1] == 'M' && get(t.first+2)[t.second-2] == 'A' && get(t.first+3)[t.second-3] == 'S'
    }, { t -> t.second-3>=0 &&
        get(t.first).slice(t.second-3 .. t.second-1) == "SAM"
    }, { t -> t.first-3>=0 && t.second-3>=0 &&
        get(t.first-1)[t.second-1] == 'M' && get(t.first-2)[t.second-2] == 'A' && get(t.first-3)[t.second-3] == 'S'
    }, { t -> t.first-3>=0 &&
        get(t.first-1)[t.second] == 'M' && get(t.first-2)[t.second] == 'A' && get(t.first-3)[t.second] == 'S'
    }, { t -> t.first-3>=0 && t.second+3<first().length &&
        get(t.first-1)[t.second+1] == 'M' && get(t.first-2)[t.second+2] == 'A' && get(t.first-3)[t.second+3] == 'S'
    }
)

fun List<String>.countXmas() = asSequence().flatMapIndexed { r :Int, row :String ->
    row.mapIndexed { c, ch -> Triple(r,c,ch) }
    .filter { it.third=='X' }
}.sumOf { t -> options<Char>().count { it(t) }.toLong() }

fun <T> List<String>.options2() = listOf<Function1<Triple<Int, Int, T>, Boolean>>({ t ->
        setOf(get(t.first-1)[t.second-1], get(t.first+1)[t.second+1]) == setOf('M', 'S') &&
        setOf(get(t.first-1)[t.second+1], get(t.first+1)[t.second-1]) == setOf('M', 'S')
    }
)

fun List<String>.countMasmas() = asSequence().flatMapIndexed { r :Int, row :String ->
    row.mapIndexed { c :Int, ch :Char -> Triple(r,c,ch) }
    .filter { it.third == 'A' && it.first>=1 && it.first+1<size && it.second>=1 && it.second+1<first().length }
}.sumOf { t -> options2<Char>().count { it(t) }.toLong() }

fun main() {
//    val input = listOf(
//        "..X...",
//        ".SAMX.",
//        ".A..A.",
//        "XMAS.S",
//        ".X....", )
    val input = listOf(
        "MMMSXXMASM",
        "MSAMXMSMSA",
        "AMXSXMAAMM",
        "MSAMASMSMX",
        "XMASAMXAMM",
        "XXAMMXXAMA",
        "SMSMSASXSS",
        "SAXAMASAAA",
        "MAMMMXMMMM",
        "MXMXAXMASX"
    )
//    val input = getInput(null, "2404xmas.txt").toReader().lines().toList()
//    val input = listOf(
//        "M.S",
//        ".A.",
//        "M.S"
//    )
    val count = input.countXmas()
    println("Found $count times xmas in the input.")
    val count2 = input.countMasmas()
    println("Found $count2 times mAsmAs in the input.")
}
