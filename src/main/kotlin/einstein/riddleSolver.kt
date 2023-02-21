package einstein

interface Option<T> :Comparable<T> {
    val type :Type
}

enum class Type {
    COLOR, NATIONALITY, BEVERAGE, CIGARETTE_BRAND, PET;

    fun toVarName() = name.lowercase()+"s"
}
enum class Colors(override val type :Type =Type.COLOR) :Option<Colors> {
    RED, GREEN, WHITE, YELLOW, BLUE;

    override fun toString() = name.lowercase()
}

enum class Nationalities(override val type :Type =Type.NATIONALITY) :Option<Nationalities> {
    BRITISH, SWEDISH, DANISH, GERMAN, NORWEGIAN;

    override fun toString() = name[0]+ name.slice(1 until name.length).lowercase()
}

enum class Beverages(override val type :Type =Type.BEVERAGE) :Option<Beverages> {
    TEA, COFFEE, MILK, BEER, WATER;

    override fun toString() = name.lowercase()
}

enum class CigaretteBrands(override val type :Type =Type.CIGARETTE_BRAND) :Option<CigaretteBrands> {
    PALL_MALL, DUNHILL, BLENDS, BLUE_MASTER, PRINCE;

    override fun toString() = name.lowercase()
}

enum class Pets(override val type :Type =Type.PET) :Option<Pets> {
    DOGS, BIRDS, CATS, HORSES, FISH;

    override fun toString() = name.lowercase()
}

sealed interface Can {
    object YES :Can {}
    object NO :Can {}

    data class Better<T :Option<T>>(val index :Int, val value :T) :Can {
        override fun toString() = "${value.type.toVarName()}[$index] = $value"
    }

    object MAYBE :Can {}
}

data class Pentuple<F, S, T, Fo, Fi>(val first :F, val second :S, val third :T,
                                     val fourth :Fo, val fifth :Fi) {}

typealias Solutions = MutableList<Pentuple<Array<Nationalities>, Array<Colors>, Array<Beverages>, Array<CigaretteBrands>, Array<Pets>>>

fun main() {
    val solutions :Solutions = mutableListOf()
    val colors = arrayOf<Colors?>(null, null, null, null, null)
    val nationalities = arrayOf<Nationalities?>(null, null, null, null, null)
    val beverages = arrayOf<Beverages?>(null, null, null, null, null)
    val cigarettes = arrayOf<CigaretteBrands?>(null, null, null, null, null)
    val pets = arrayOf<Pets?>(null, null, null, null, null)

    if (solutions.improve(nationalities, colors, beverages, cigarettes, pets) == Can.NO)
        return
    describe(nationalities, colors, beverages, cigarettes, pets)
    solutions.permuteNationalities(nationalities, colors, beverages, cigarettes, pets) { n, c, b, z, p ->
        permuteBeverages(n, c, b, z, p) { n1, c1, b1, z1, p1 ->
            permuteCigarettes(n1, c1, b1, z1, p1) { n2, c2, b2, z2, p2 ->
                permuteColors(n2, c2, b2, z2, p2) { n3, c3, b3, z3, p3 ->
                    permutePets(n3, c3, b3, z3, p3) { n4, c4, b4, z4, p4 ->
                        throw IllegalStateException("cannot be solved")
                    }
                }
            }
        }
    }
    println("\nSolutions:")
    solutions.forEach { s ->
        check(s.first, s.second, s.third, s.fourth, s.fifth)
    }
}

private fun Solutions.permutePets(
    nationalities :Array<Nationalities?>,
    colors :Array<Colors?>,
    beverages :Array<Beverages?>,
    cigarettes :Array<CigaretteBrands?>,
    pets :Array<Pets?>,
    walker :Solutions.(Array<Nationalities?>, Array<Colors?>, Array<Beverages?>, Array<CigaretteBrands?>, Array<Pets?>)->Unit
) {
    val petPermutations = permute(Pets.values(), pets)
    for (pets1 in petPermutations) {
        val nationalities1 = nationalities.clone()
        val colors1 = colors.clone()
        val beverages1 = beverages.clone()
        val cigarettes1 = cigarettes.clone()
        when (improve(nationalities1, colors1, beverages1, cigarettes1, pets1)) {
            Can.YES, Can.NO -> continue
            else -> {
                describe(nationalities1, colors1, beverages1, cigarettes1, pets1)
                walker(nationalities1, colors1, beverages1, cigarettes1, pets1)
            }
        }
    }
}

private fun Solutions.permuteColors(
    nationalities: Array<Nationalities?>,
    colors: Array<Colors?>,
    beverages: Array<Beverages?>,
    cigarettes: Array<CigaretteBrands?>,
    pets: Array<Pets?>,
    walker :Solutions.(Array<Nationalities?>, Array<Colors?>, Array<Beverages?>, Array<CigaretteBrands?>, Array<Pets?>)->Unit
) {
    val colorPermutations = permute(Colors.values(), colors)
    for (colors1 in colorPermutations) {
        val nationalities1 = nationalities.clone()
        val beverages1 = beverages.clone()
        val cigarettes1 = cigarettes.clone()
        val pets1 = pets.clone()
        when (improve(nationalities1, colors1, beverages1, cigarettes1, pets1)) {
            Can.YES, Can.NO -> continue
            else -> {
                describe(nationalities1, colors1, beverages1, cigarettes1, pets1)
                walker(nationalities1, colors1, beverages1, cigarettes1, pets1)
            }
        }
    }
}

fun Solutions.permuteCigarettes(
    nationalities: Array<Nationalities?>,
    colors: Array<Colors?>,
    beverages: Array<Beverages?>,
    cigarettes: Array<CigaretteBrands?>,
    pets: Array<Pets?>,
    walker :Solutions.(Array<Nationalities?>, Array<Colors?>, Array<Beverages?>, Array<CigaretteBrands?>, Array<Pets?>)->Unit
) {
    val cigarettePermutations = permute(CigaretteBrands.values(), cigarettes)
    for (cigarettes1 in cigarettePermutations) {
        val nationalities1 = nationalities.clone()
        val colors1 = colors.clone()
        val beverages1 = beverages.clone()
        val pets1 = pets.clone()
        when (improve(nationalities1, colors1, beverages1, cigarettes1, pets1)) {
            Can.YES, Can.NO -> continue
            else -> {
                describe(nationalities1, colors1, beverages1, cigarettes1, pets1)
                walker(nationalities1, colors1, beverages1, cigarettes1, pets1)
            }
        }
    }
}

fun Solutions.permuteBeverages(
    nationalities: Array<Nationalities?>,
    colors: Array<Colors?>,
    beverages: Array<Beverages?>,
    cigarettes: Array<CigaretteBrands?>,
    pets: Array<Pets?>,
    walker :Solutions.(Array<Nationalities?>, Array<Colors?>, Array<Beverages?>, Array<CigaretteBrands?>, Array<Pets?>)->Unit
) {
    val beveragePermutations = permute(Beverages.values(), beverages)
    for (beverages1 in beveragePermutations) {
        val nationalities1 = nationalities.clone()
        val colors1 = colors.clone()
        val cigarettes1 = cigarettes.clone()
        val pets1 = pets.clone()
        when (improve(nationalities1, colors1, beverages1, cigarettes1, pets1)) {
            Can.YES, Can.NO -> continue
            else -> {
                describe(nationalities1, colors1, beverages1, cigarettes1, pets1)
                walker(nationalities1, colors1, beverages1, cigarettes1, pets1)
            }
        }
    }
}

private fun Solutions.permuteNationalities(
    nationalities: Array<Nationalities?>,
    colors: Array<Colors?>,
    beverages: Array<Beverages?>,
    cigarettes: Array<CigaretteBrands?>,
    pets: Array<Pets?>,
    walker :Solutions.(Array<Nationalities?>, Array<Colors?>, Array<Beverages?>, Array<CigaretteBrands?>, Array<Pets?>)->Unit
) {
    val nationalityPermutations = permute(Nationalities.values(), nationalities)
    for (nationalities1 in nationalityPermutations) {
        val colors1 = colors.clone()
        val beverages1 = beverages.clone()
        val cigarettes1 = cigarettes.clone()
        val pets1 = pets.clone()
        when (improve(nationalities1, colors1, beverages1, cigarettes1, pets1)) {
            Can.YES, Can.NO -> continue
            else -> {
                describe(nationalities1, colors1, beverages1, cigarettes1, pets1)
                walker(nationalities1, colors1, beverages1, cigarettes1, pets1)
            }
        }
    }
}

private fun describe(nationalities :Array<out Nationalities?>, colors :Array<out Colors?>,
        beverages: Array<out Beverages?>, cigarettes :Array<out CigaretteBrands?>, pets :Array<out Pets?>) {
    println("We know: Nationalities: ${nationalities.toList()},\n  Colors: ${colors.toList()},\n" +
            "  Beverages: ${beverages.toList()},\n  Cigarettes: ${cigarettes.toList()},\n" +
            "  Pets: ${pets.toList()}"
    )
}

inline fun <reified T :Option<T>> permute(options: Array<out T>, prefilled: Array<out T?>): List<Array<T?>> {
    val result = mutableListOf<Array<T?>>()
    val remainders :MutableSet<T> = options.toMutableSet()
    for (option in prefilled) if (option!=null) {
        if (option !in remainders) {
            println("Repeated elements in prefill: $prefilled")
            return emptyList()
        } else {
            remainders.remove(option)
        }
    }
    val permutation = mutableListOf<T?>()
    var i = 0
    while (i>=0) {
        while (i<options.size) {
            if (prefilled[i] != null) {
                permutation.add(prefilled[i])
            } else {
                val candidate = remainders.removeFirst()
                if (candidate!=null)
                    permutation.add(candidate)
                else {
                    println("insufficient number of options, missing ${options.size - i}")
                    break
                }
            }
            i++
        }
        if (i==options.size) {
            assert(permutation.all { it!=null })
            result.add(permutation.toTypedArray())
        }
        i--
        while (i>=0) {
            if (prefilled[i]==null) {
                val candidate = remainders.next(permutation[i]!!)
                if (candidate!=null) {
                    permutation[i++] = candidate
                    break
                } else {
                    permutation.removeAt(i--)
                }
            } else {
                permutation.removeAt(i--)
            }
        }
    }
    return result
}

fun <T :Comparable<T>> MutableSet<T>.removeFirst(): T? {
    val result = minOrNull()
    if (result!=null)
        remove(result)
    return result
}

fun <T :Comparable<T>> MutableSet<T>.next(current: T): T? {
    add(current)
    val list = this.sorted()
    var index = list.indexOf(current)
    while (index>=0&&index<list.size) {
        if (list[index]==current)
            index++
        else
            break
    }
    val result = list.getOrNull(index)
    if (result!=null)
        remove(result)
    return result
}

private fun Solutions.improve(
    nationalities: Array<Nationalities?>,
    colors: Array<Colors?>,
    beverages: Array<Beverages?>,
    cigarettes: Array<CigaretteBrands?>,
    pets: Array<Pets?>
): Can {
    while (true) {
        val can = canAccept(nationalities, colors, beverages, cigarettes, pets)
        when (can) {
            Can.YES -> {
                assert(check(nationalities, colors, beverages, cigarettes, pets) == null)
                add(Pentuple(nationalities.filterNotNull().toTypedArray(),
                    colors.filterNotNull().toTypedArray(),
                    beverages.filterNotNull().toTypedArray(),
                    cigarettes.filterNotNull().toTypedArray(),
                    pets.filterNotNull().toTypedArray()))
                return Can.YES
            }

            Can.NO -> {
                println("Contradiction: ${check(nationalities, colors, beverages, cigarettes, pets)}")
                return Can.NO
            }

            is Can.Better<*> -> {
                println("apply: $can")
                can.improve(nationalities, colors, beverages, cigarettes, pets)
            }

            Can.MAYBE -> {
                return Can.MAYBE
            }
        }
    }
}

private fun <T :Option<T>> Can.Better<T>.improve(
    nationalities: Array<Nationalities?>,
    colors: Array<Colors?>,
    beverages: Array<Beverages?>,
    cigarettes: Array<CigaretteBrands?>,
    pets: Array<Pets?>
) = when (value.type) {
    Type.NATIONALITY -> nationalities[index] = value as Nationalities
    Type.COLOR -> colors[index] = value as Colors
    Type.BEVERAGE -> beverages[index] = value as Beverages
    Type.CIGARETTE_BRAND -> cigarettes[index] = value as CigaretteBrands
    Type.PET -> pets[index] = value as Pets
}

fun canAccept(nationalities: Array<out Nationalities?>, colors: Array<out Colors?>, beverages: Array<out Beverages?>,
              cigarettes: Array<out CigaretteBrands?>, pets: Array<out Pets?>) :Can {
    var candidate :Can = Can.YES
    var result = canFirst(nationalities, colors)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE

    result = canSecond(nationalities, pets)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE

    result = canThird(nationalities, beverages)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE

    result = canFourth(colors)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE

    result = canFifth(colors, beverages)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE

    result = canSixth(cigarettes, pets)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE

    result = canSeventh(colors, cigarettes)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE

    result = canEighth(beverages)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE

    result = canNinth(nationalities)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE

    result = canTenth(cigarettes, pets)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE

    result = canEleventh(pets, cigarettes)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE

    result = canTwelfth(cigarettes, beverages)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE

    result = canThirteenth(nationalities, cigarettes)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE

    result = canFourteenth(nationalities, colors)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE

    result = canFifteenth(cigarettes, beverages)
    if (result==Can.NO || result is Can.Better<*>)
        return result
    if (result==Can.MAYBE)
        candidate = Can.MAYBE
    return candidate
}

fun check(nationalities: Array<out Nationalities?>, colors: Array<out Colors?>, beverages: Array<out Beverages?>,
          cigarettes: Array<out CigaretteBrands?>, pets: Array<out Pets?>): Int? {
    println("Does "+ (nationalities zip colors zip beverages zip cigarettes zip pets).mapIndexed { nr, (q, p :Pets?) ->
        "the ${q.first.first.first} live in the ${nr+1}th ${q.first.first.second} house drinking ${q.first.second} smoking ${q.second} breeding $p"
    }.joinToString(",\n\t") +"?")
    if (canFirst(nationalities, colors)!=Can.YES)
        return 1
    if (canSecond(nationalities, pets)!=Can.YES)
        return 2
    if (canThird(nationalities, beverages)!=Can.YES)
        return 3
    if (canFourth(colors)!=Can.YES)
        return 4
    if (canFifth(colors, beverages)!=Can.YES)
        return 5
    if (canSixth(cigarettes, pets)!=Can.YES)
        return 6
    if (canSeventh(colors, cigarettes)!=Can.YES)
        return 7
    if (canEighth(beverages)!=Can.YES)
        return 8
    if (canNinth(nationalities)!=Can.YES)
        return 9
    if (canTenth(cigarettes, pets)!=Can.YES)
        return 10
    if (canEleventh(pets, cigarettes)!=Can.YES)
        return 11
    if (canTwelfth(cigarettes, beverages)!=Can.YES)
        return 12
    if (canThirteenth(nationalities, cigarettes)!=Can.YES)
        return 13
    if (canFourteenth(nationalities, colors)!=Can.YES)
        return 14
    if (canFifteenth(cigarettes, beverages)!=Can.YES)
        return 15
    val nr = pets.indexOf(Pets.FISH)
    if (nr>=0)
        println("The ${nationalities[nr]} owns the ${Pets.FISH}.")
    return null
}

fun canFirst(nationalities: Array<out Nationalities?>, colors: Array<out Colors?>) :Can {
    val nr = nationalities.indexOf(Nationalities.BRITISH)
    val nc = colors.indexOf(Colors.RED)
    if (nr<0 && nc<0)
        return Can.MAYBE
    if (nr<0)
        return if (nationalities[nc]!=null && nationalities[nc]!=Nationalities.BRITISH)
            Can.NO
        else
            Can.Better(nc, Nationalities.BRITISH)
    if (nc<0)
        return if (colors[nr]!=null && colors[nr]!=Colors.RED)
            Can.NO
        else
            Can.Better(nr, Colors.RED)
    return if (nr==nc) Can.YES  else Can.NO
}

fun canSecond(nationalities: Array<out Nationalities?>, pets: Array<out Pets?>) :Can {
    val nr = nationalities.indexOf(Nationalities.SWEDISH)
    val np = pets.indexOf(Pets.DOGS)
    if (nr<0 && np<0)
        return Can.MAYBE
    if (nr<0)
        return if (nationalities[np]!=null && nationalities[np]!=Nationalities.SWEDISH)
            Can.NO
        else
            Can.Better(np, Nationalities.SWEDISH)
    if (np<0)
        return if (pets[nr]!=null && pets[nr]!=Pets.DOGS)
            Can.NO
        else
            Can.Better(nr, Pets.DOGS)
    return if (nr==np) Can.YES  else Can.NO
}

fun canThird(nationalities: Array<out Nationalities?>, beverages: Array<out Beverages?>) :Can {
    val nr = nationalities.indexOf(Nationalities.DANISH)
    val nb = beverages.indexOf(Beverages.TEA)
    if (nr<0 && nb<0)
        return Can.MAYBE
    if (nr<0)
        return if (nationalities[nb]!=null && nationalities[nb]!=Nationalities.DANISH)
            Can.NO
        else
            Can.Better(nb, Nationalities.DANISH)
    if (nb<0)
        return if (beverages[nr]!=null && beverages[nr]!=Beverages.TEA)
            Can.NO
        else
            Can.Better(nr, Beverages.TEA)
    return if (nr==nb) Can.YES  else Can.NO
}

fun canFourth(colors: Array<out Colors?>) :Can {
    val nr = colors.indexOf(Colors.GREEN)
    val nc = colors.indexOf(Colors.WHITE)
    if (nr<0 || nc<0)
        return Can.MAYBE
    return if (nr==nc+1 || nr+1==nc) Can.YES  else Can.NO
}

fun canFifth(colors: Array<out Colors?>, beverages: Array<out Beverages?>) :Can {
    val nr = colors.indexOf(Colors.GREEN)
    val nb = beverages.indexOf(Beverages.COFFEE)
    if (nr<0 && nb<0)
        return Can.MAYBE
    if (nr<0)
        return if (colors[nb]!=null && colors[nb]!=Colors.GREEN)
            Can.NO
        else
            Can.Better(nb, Colors.GREEN)
    if (nb<0)
        return if (beverages[nr]!=null && beverages[nr]!=Beverages.COFFEE)
            Can.NO
        else
            Can.Better(nr, Beverages.COFFEE)
    return if (nr==nb) Can.YES  else Can.NO
}

fun canSixth(cigarettes: Array<out CigaretteBrands?>, pets: Array<out Pets?>): Can {
    val nr = cigarettes.indexOf(CigaretteBrands.PALL_MALL)
    val np = pets.indexOf(Pets.BIRDS)
    if (nr<0 && np<0)
        return Can.MAYBE
    if (nr<0)
        return if (cigarettes[np]!=null && cigarettes[np]!=CigaretteBrands.PALL_MALL)
            Can.NO
        else
            Can.Better(np, CigaretteBrands.PALL_MALL)
    if (np<0)
        return if (pets[nr]!=null && pets[nr]!=Pets.BIRDS)
            Can.NO
        else
            Can.Better(nr, Pets.BIRDS)
    return if (nr==np) Can.YES  else Can.NO
}

fun canSeventh(colors: Array<out Colors?>, cigarettes: Array<out CigaretteBrands?>) :Can {
    val nr = colors.indexOf(Colors.YELLOW)
    val nc = cigarettes.indexOf(CigaretteBrands.DUNHILL)
    if (nr<0 && nc<0)
        return Can.MAYBE
    if (nr<0)
        return if (colors[nc]!=null && colors[nc]!=Colors.YELLOW)
            Can.NO
        else
            Can.Better(nc, Colors.YELLOW)
    if (nc<0)
        return if (cigarettes[nr]!=null && cigarettes[nr]!=CigaretteBrands.DUNHILL)
            Can.NO
        else
            Can.Better(nr, CigaretteBrands.DUNHILL)
    return if (nr==nc) Can.YES  else Can.NO
}

fun canEighth(beverages: Array<out Beverages?>) :Can {
    if (beverages.getOrNull(2)==Beverages.MILK)
        return Can.YES
    if (beverages.getOrNull(2)==null)
        return Can.Better(2, Beverages.MILK)
    return Can.NO
}

fun canNinth(nationalities: Array<out Nationalities?>) :Can {
    if (nationalities.getOrNull(0)==null)
        return Can.Better(0, Nationalities.NORWEGIAN)
    return if (nationalities.getOrNull(0)==Nationalities.NORWEGIAN)
        Can.YES
    else Can.NO
}

fun canTenth(cigarettes: Array<out CigaretteBrands?>, pets: Array<out Pets?>) :Can {
    val nr = cigarettes.indexOf(CigaretteBrands.BLENDS)
    val np = pets.indexOf(Pets.CATS)
    if (nr<0 || np<0)
        return Can.MAYBE
    return if (nr==np+1 || nr+1==np) Can.YES  else Can.NO
}

fun canEleventh(pets: Array<out Pets?>, cigarettes: Array<out CigaretteBrands?>) :Can {
    val nr = pets.indexOf(Pets.HORSES)
    val nc = cigarettes.indexOf(CigaretteBrands.DUNHILL)
    if (nr<0 || nc<0)
        return Can.MAYBE
    return if (nr==nc+1 || nr+1==nc) Can.YES  else Can.NO
}

fun canTwelfth(cigarettes: Array<out CigaretteBrands?>, beverages: Array<out Beverages?>) :Can {
    val nr = cigarettes.indexOf(CigaretteBrands.BLUE_MASTER)
    val nb = beverages.indexOf(Beverages.BEER)
    if (nr<0 && nb<0)
        return Can.MAYBE
    if (nr<0)
        return if (cigarettes[nb]!=null && cigarettes[nb]!=CigaretteBrands.BLUE_MASTER)
            Can.NO
        else
            Can.Better(nb, CigaretteBrands.BLUE_MASTER)
    if (nb<0)
        return if (beverages[nr]!=null && beverages[nr]!=Beverages.BEER)
            Can.NO
        else
            Can.Better(nr, Beverages.BEER)
    return if (nr==nb) Can.YES  else Can.NO
}

fun canThirteenth(nationalities: Array<out Nationalities?>, cigarettes: Array<out CigaretteBrands?>) :Can {
    val nr = nationalities.indexOf(Nationalities.GERMAN)
    val nc = cigarettes.indexOf(CigaretteBrands.PRINCE)
    if (nr<0 && nc<0)
        return Can.MAYBE
    if (nr<0)
        return if (nationalities[nc]!=null && nationalities[nc]!=Nationalities.GERMAN)
            Can.NO
        else
            Can.Better(nc, Nationalities.GERMAN)
    if (nc<0)
        return if (cigarettes[nr]!=null && cigarettes[nr]!=CigaretteBrands.PRINCE)
            Can.NO
        else
            Can.Better(nr, CigaretteBrands.PRINCE)
    return if (nr==nc) Can.YES else Can.NO
}

fun canFourteenth(nationalities: Array<out Nationalities?>, colors: Array<out Colors?>) :Can {
    val nr = nationalities.indexOf(Nationalities.NORWEGIAN)
    val nc = colors.indexOf(Colors.BLUE)
    if (nr<0 || nc<0)
        return Can.MAYBE
    return if (nr==nc+1 || nr+1==nc) Can.YES  else Can.NO
}

fun canFifteenth(cigarettes: Array<out CigaretteBrands?>, beverages: Array<out Beverages?>) :Can {
    val nr = cigarettes.indexOf(CigaretteBrands.BLENDS)
    val nb = beverages.indexOf(Beverages.WATER)
    if (nr<0 || nb<0)
        return Can.MAYBE
    return if (nr==nb+1 || nr+1==nb) Can.YES  else Can.NO
}
