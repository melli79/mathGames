package einstein

fun String.titlecase() = split(' ', '\t', '\n').map { it.singleWordTitlecase() }.joinToString(" ")
fun String.singleWordTitlecase() = this[0] + slice(1 until length).lowercase()

private enum class Nationalities(override val type :Type = Type.NATIONALITY) : Option<Nationalities> {
    BRITISH, SWEDISH, DANISH, GERMAN, NORWEGIAN;

    override fun toString() = name.singleWordTitlecase()

}

private enum class Beverages(override val type :Type = Type.BEVERAGE) : Option<Beverages> {
    TEA, COFFEE, MILK, BEER, WATER;

    override fun toString() = name.lowercase()
}

private enum class CigaretteBrands(override val type :Type = Type.CIGARETTE_BRAND) : Option<CigaretteBrands> {
    PALL_MALL, DUNHILL, BLENDS, BLUE_MASTER, PRINCE;

    override fun toString() = name.replace('_', ' ').titlecase()
}

private typealias Solutions = MutableList<Pentuple<Array<Nationalities>, Array<Colors>, Array<Beverages>, Array<CigaretteBrands>, Array<Pets>>>

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
                    permutePets(n3, c3, b3, z3, p3) { _, _, _, _, _ ->
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
    walker :Solutions.(Array<Nationalities?>, Array<Colors?>, Array<Beverages?>, Array<CigaretteBrands?>, Array<Pets?>) -> Unit
) {
    val petPermutations = permute(Pets.entries, pets)
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
    nationalities :Array<Nationalities?>,
    colors :Array<Colors?>,
    beverages :Array<Beverages?>,
    cigarettes :Array<CigaretteBrands?>,
    pets :Array<Pets?>,
    walker :Solutions.(Array<Nationalities?>, Array<Colors?>, Array<Beverages?>, Array<CigaretteBrands?>, Array<Pets?>) -> Unit
) {
    val colorPermutations = permute(Colors.entries, colors)
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

private fun Solutions.permuteCigarettes(
    nationalities :Array<Nationalities?>,
    colors :Array<Colors?>,
    beverages :Array<Beverages?>,
    cigarettes :Array<CigaretteBrands?>,
    pets :Array<Pets?>,
    walker :Solutions.(Array<Nationalities?>, Array<Colors?>, Array<Beverages?>, Array<CigaretteBrands?>, Array<Pets?>) -> Unit
) {
    val cigarettePermutations = permute(CigaretteBrands.entries, cigarettes)
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

private fun Solutions.permuteBeverages(
    nationalities :Array<Nationalities?>,
    colors :Array<Colors?>,
    beverages :Array<Beverages?>,
    cigarettes :Array<CigaretteBrands?>,
    pets :Array<Pets?>,
    walker :Solutions.(Array<Nationalities?>, Array<Colors?>, Array<Beverages?>, Array<CigaretteBrands?>, Array<Pets?>) -> Unit
) {
    val beveragePermutations = permute(Beverages.entries, beverages)
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
    nationalities :Array<Nationalities?>,
    colors :Array<Colors?>,
    beverages :Array<Beverages?>,
    cigarettes :Array<CigaretteBrands?>,
    pets :Array<Pets?>,
    walker :Solutions.(Array<Nationalities?>, Array<Colors?>, Array<Beverages?>, Array<CigaretteBrands?>, Array<Pets?>) -> Unit
) {
    val nationalityPermutations = permute(Nationalities.entries, nationalities)
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

private fun describe(
    nationalities :Array<out Nationalities?>, colors :Array<out Colors?>,
    beverages :Array<out Beverages?>, cigarettes :Array<out CigaretteBrands?>, pets :Array<out Pets?>
) {
    println(
        "We know: Nationalities: ${nationalities.toList()},\n  Colors: ${colors.toList()},\n" +
                "  Beverages: ${beverages.toList()},\n  Cigarettes: ${cigarettes.toList()},\n" +
                "  Pets: ${pets.toList()}"
    )
}

private fun Solutions.improve(
    nationalities :Array<Nationalities?>,
    colors :Array<Colors?>,
    beverages :Array<Beverages?>,
    cigarettes :Array<CigaretteBrands?>,
    pets :Array<Pets?>
) :Can {
    while (true) {
        val can = canAccept(nationalities, colors, beverages, cigarettes, pets)
        when (can) {
            Can.YES -> {
                assert(check(nationalities, colors, beverages, cigarettes, pets) == null)
                add(
                    Pentuple(
                        nationalities.filterNotNull().toTypedArray(),
                        colors.filterNotNull().toTypedArray(),
                        beverages.filterNotNull().toTypedArray(),
                        cigarettes.filterNotNull().toTypedArray(),
                        pets.filterNotNull().toTypedArray()
                    )
                )
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

private fun <T : Option<T>> Can.Better<T>.improve(
    nationalities :Array<Nationalities?>,
    colors :Array<Colors?>,
    beverages :Array<Beverages?>,
    cigarettes :Array<CigaretteBrands?>,
    pets :Array<Pets?>
) = when (value.type) {
    Type.NATIONALITY -> nationalities[index] = value as Nationalities
    Type.COLOR -> colors[index] = value as Colors
    Type.BEVERAGE -> beverages[index] = value as Beverages
    Type.CIGARETTE_BRAND -> cigarettes[index] = value as CigaretteBrands
    Type.PET -> pets[index] = value as Pets
}

private fun canAccept(
    nationalities :Array<out Nationalities?>, colors :Array<out Colors?>, beverages :Array<out Beverages?>,
    cigarettes :Array<out CigaretteBrands?>, pets :Array<out Pets?>
) :Can {
    var candidate :Can = Can.YES
    return listOf({ canFirst(nationalities, colors) },
        { canSecond(nationalities, pets) },
        { canThird(nationalities, beverages) },
        { canFourth(colors) },
        { canFifth(colors, beverages) },
        { canSixth(cigarettes, pets) },
        { canSeventh(colors, cigarettes) },
        { canEighth(beverages) },
        { canNinth(nationalities) },
        { canTenth(cigarettes, pets) },
        { canEleventh(pets, cigarettes) },
        { canTwelfth(cigarettes, beverages) },
        { canThirteenth(nationalities, cigarettes) },
        { canFourteenth(nationalities, colors) },
        { canFifteenth(cigarettes, beverages) },
        { candidate })
        .foldRight(Can.YES) { rule, pre :Can ->
            if (pre == Can.NO || pre is Can.Better<*>)
                pre
            else {
                val result :Can = rule()
                if (result == Can.NO || result is Can.Better<*>)
                    result
                else {
                    if (result == Can.MAYBE)
                        candidate = Can.MAYBE
                    pre
                }
            }
        }
}

private fun check(
    nationalities :Array<out Nationalities?>, colors :Array<out Colors?>, beverages :Array<out Beverages?>,
    cigarettes :Array<out CigaretteBrands?>, pets :Array<out Pets?>
) :Int? {
    println("Does " + (nationalities zip colors zip beverages zip cigarettes zip pets).mapIndexed { nr, (q, p :Pets?) ->
        "the ${q.first.first.first} live in the ${nr + 1}th ${q.first.first.second} house drinking ${q.first.second}" +
                " smoking ${q.second} breeding $p"
    }.joinToString(",\n\t") + "?")
    val error = listOf({ canFirst(nationalities, colors) },
        { canSecond(nationalities, pets) },
        { canThird(nationalities, beverages) },
        { canFourth(colors) },
        { canFifth(colors, beverages) },
        { canSixth(cigarettes, pets) },
        { canSeventh(colors, cigarettes) },
        { canEighth(beverages) },
        { canNinth(nationalities) },
        { canTenth(cigarettes, pets) },
        { canEleventh(pets, cigarettes) },
        { canTwelfth(cigarettes, beverages) },
        { canThirteenth(nationalities, cigarettes) },
        { canFourteenth(nationalities, colors) },
        { canFifteenth(cigarettes, beverages) })
        .indexOfFirst { it() != Can.YES }
    if (error >= 0)
        return error + 1

    val nr = pets.indexOf(Pets.FISH)
    if (nr >= 0)
        println("The ${nationalities[nr]} owns the ${Pets.FISH}.")
    return null
}

private fun canFirst(nationalities :Array<out Nationalities?>, colors :Array<out Colors?>) :Can {
    val nr = nationalities.indexOf(Nationalities.BRITISH)
    val nc = colors.indexOf(Colors.RED)
    if (nr < 0 && nc < 0)
        return Can.MAYBE
    if (nr < 0)
        return if (nationalities[nc] != null && nationalities[nc] != Nationalities.BRITISH)
            Can.NO
        else
            Can.Better(nc, Nationalities.BRITISH)
    if (nc < 0)
        return if (colors[nr] != null && colors[nr] != Colors.RED)
            Can.NO
        else
            Can.Better(nr, Colors.RED)
    return if (nr == nc) Can.YES else Can.NO
}

private fun canSecond(nationalities :Array<out Nationalities?>, pets :Array<out Pets?>) :Can {
    val nr = nationalities.indexOf(Nationalities.SWEDISH)
    val np = pets.indexOf(Pets.DOGS)
    if (nr < 0 && np < 0)
        return Can.MAYBE
    if (nr < 0)
        return if (nationalities[np] != null && nationalities[np] != Nationalities.SWEDISH)
            Can.NO
        else
            Can.Better(np, Nationalities.SWEDISH)
    if (np < 0)
        return if (pets[nr] != null && pets[nr] != Pets.DOGS)
            Can.NO
        else
            Can.Better(nr, Pets.DOGS)
    return if (nr == np) Can.YES else Can.NO
}

private fun canThird(nationalities :Array<out Nationalities?>, beverages :Array<out Beverages?>) :Can {
    val nr = nationalities.indexOf(Nationalities.DANISH)
    val nb = beverages.indexOf(Beverages.TEA)
    if (nr < 0 && nb < 0)
        return Can.MAYBE
    if (nr < 0)
        return if (nationalities[nb] != null && nationalities[nb] != Nationalities.DANISH)
            Can.NO
        else
            Can.Better(nb, Nationalities.DANISH)
    if (nb < 0)
        return if (beverages[nr] != null && beverages[nr] != Beverages.TEA)
            Can.NO
        else
            Can.Better(nr, Beverages.TEA)
    return if (nr == nb) Can.YES else Can.NO
}

private fun canFourth(colors :Array<out Colors?>) :Can {
    val nr = colors.indexOf(Colors.GREEN)
    val nc = colors.indexOf(Colors.WHITE)
    if (nr < 0 || nc < 0)
        return Can.MAYBE
    return if (nr == nc + 1 || nr + 1 == nc) Can.YES else Can.NO
}

private fun canFifth(colors :Array<out Colors?>, beverages :Array<out Beverages?>) :Can {
    val nr = colors.indexOf(Colors.GREEN)
    val nb = beverages.indexOf(Beverages.COFFEE)
    if (nr < 0 && nb < 0)
        return Can.MAYBE
    if (nr < 0)
        return if (colors[nb] != null && colors[nb] != Colors.GREEN)
            Can.NO
        else
            Can.Better(nb, Colors.GREEN)
    if (nb < 0)
        return if (beverages[nr] != null && beverages[nr] != Beverages.COFFEE)
            Can.NO
        else
            Can.Better(nr, Beverages.COFFEE)
    return if (nr == nb) Can.YES else Can.NO
}

private fun canSixth(cigarettes :Array<out CigaretteBrands?>, pets :Array<out Pets?>) :Can {
    val nr = cigarettes.indexOf(CigaretteBrands.PALL_MALL)
    val np = pets.indexOf(Pets.BIRDS)
    if (nr < 0 && np < 0)
        return Can.MAYBE
    if (nr < 0)
        return if (cigarettes[np] != null && cigarettes[np] != CigaretteBrands.PALL_MALL)
            Can.NO
        else
            Can.Better(np, CigaretteBrands.PALL_MALL)
    if (np < 0)
        return if (pets[nr] != null && pets[nr] != Pets.BIRDS)
            Can.NO
        else
            Can.Better(nr, Pets.BIRDS)
    return if (nr == np) Can.YES else Can.NO
}

private fun canSeventh(colors :Array<out Colors?>, cigarettes :Array<out CigaretteBrands?>) :Can {
    val nr = colors.indexOf(Colors.YELLOW)
    val nc = cigarettes.indexOf(CigaretteBrands.DUNHILL)
    if (nr < 0 && nc < 0)
        return Can.MAYBE
    if (nr < 0)
        return if (colors[nc] != null && colors[nc] != Colors.YELLOW)
            Can.NO
        else
            Can.Better(nc, Colors.YELLOW)
    if (nc < 0)
        return if (cigarettes[nr] != null && cigarettes[nr] != CigaretteBrands.DUNHILL)
            Can.NO
        else
            Can.Better(nr, CigaretteBrands.DUNHILL)
    return if (nr == nc) Can.YES else Can.NO
}

private fun canEighth(beverages :Array<out Beverages?>) :Can {
    if (beverages.getOrNull(2) == Beverages.MILK)
        return Can.YES
    if (beverages.getOrNull(2) == null)
        return Can.Better(2, Beverages.MILK)
    return Can.NO
}

private fun canNinth(nationalities :Array<out Nationalities?>) :Can {
    if (nationalities.getOrNull(0) == null)
        return Can.Better(0, Nationalities.NORWEGIAN)
    return if (nationalities.getOrNull(0) == Nationalities.NORWEGIAN)
        Can.YES
    else Can.NO
}

private fun canTenth(cigarettes :Array<out CigaretteBrands?>, pets :Array<out Pets?>) :Can {
    val nr = cigarettes.indexOf(CigaretteBrands.BLENDS)
    val np = pets.indexOf(Pets.CATS)
    if (nr < 0 || np < 0)
        return Can.MAYBE
    return if (nr == np + 1 || nr + 1 == np) Can.YES else Can.NO
}

private fun canEleventh(pets :Array<out Pets?>, cigarettes :Array<out CigaretteBrands?>) :Can {
    val nr = pets.indexOf(Pets.HORSES)
    val nc = cigarettes.indexOf(CigaretteBrands.DUNHILL)
    if (nr < 0 || nc < 0)
        return Can.MAYBE
    return if (nr == nc + 1 || nr + 1 == nc) Can.YES else Can.NO
}

private fun canTwelfth(cigarettes :Array<out CigaretteBrands?>, beverages :Array<out Beverages?>) :Can {
    val nr = cigarettes.indexOf(CigaretteBrands.BLUE_MASTER)
    val nb = beverages.indexOf(Beverages.BEER)
    if (nr < 0 && nb < 0)
        return Can.MAYBE
    if (nr < 0)
        return if (cigarettes[nb] != null && cigarettes[nb] != CigaretteBrands.BLUE_MASTER)
            Can.NO
        else
            Can.Better(nb, CigaretteBrands.BLUE_MASTER)
    if (nb < 0)
        return if (beverages[nr] != null && beverages[nr] != Beverages.BEER)
            Can.NO
        else
            Can.Better(nr, Beverages.BEER)
    return if (nr == nb) Can.YES else Can.NO
}

private fun canThirteenth(nationalities :Array<out Nationalities?>, cigarettes :Array<out CigaretteBrands?>) :Can {
    val nr = nationalities.indexOf(Nationalities.GERMAN)
    val nc = cigarettes.indexOf(CigaretteBrands.PRINCE)
    if (nr < 0 && nc < 0)
        return Can.MAYBE
    if (nr < 0)
        return if (nationalities[nc] != null && nationalities[nc] != Nationalities.GERMAN)
            Can.NO
        else
            Can.Better(nc, Nationalities.GERMAN)
    if (nc < 0)
        return if (cigarettes[nr] != null && cigarettes[nr] != CigaretteBrands.PRINCE)
            Can.NO
        else
            Can.Better(nr, CigaretteBrands.PRINCE)
    return if (nr == nc) Can.YES else Can.NO
}

private fun canFourteenth(nationalities :Array<out Nationalities?>, colors :Array<out Colors?>) :Can {
    val nr = nationalities.indexOf(Nationalities.NORWEGIAN)
    val nc = colors.indexOf(Colors.BLUE)
    if (nr < 0 || nc < 0)
        return Can.MAYBE
    return if (nr == nc + 1 || nr + 1 == nc) Can.YES else Can.NO
}

private fun canFifteenth(cigarettes :Array<out CigaretteBrands?>, beverages :Array<out Beverages?>) :Can {
    val nr = cigarettes.indexOf(CigaretteBrands.BLENDS)
    val nb = beverages.indexOf(Beverages.WATER)
    if (nr < 0 || nb < 0)
        return Can.MAYBE
    return if (nr == nb + 1 || nr + 1 == nb) Can.YES else Can.NO
}
