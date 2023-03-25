package einstein.ea

import einstein.*

private enum class Nationalities(override val type :Type = Type.NATIONALITY) :Option<Nationalities> {
    CHINESE, VIETNAMESE, MONGOLIAN, JAPANESE, KOREAN;

    override fun toString() = name[0]+ name.slice(1 until name.length).lowercase()
}

private enum class Beverages(override val type :Type = Type.BEVERAGE) :Option<Beverages> {
    YELLOW_WINE, SAKE, WHITE_SPIRIT, BEER, WATER;

    override fun toString() = name.lowercase()
}

private enum class CigaretteBrands(override val type :Type = Type.CIGARETTE_BRAND) :Option<CigaretteBrands> {
    LIQUN_BROWN, HERO, GRAVEN_A, MEVIUS, NANJING_CAPSULE;

    override fun toString() = name.lowercase()
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

private fun Solutions.permuteCigarettes(
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

private fun Solutions.permuteBeverages(
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

private fun Solutions.improve(
    nationalities: Array<Nationalities?>,
    colors: Array<Colors?>,
    beverages: Array<Beverages?>,
    cigarettes: Array<CigaretteBrands?>,
    pets: Array<Pets?>
):Can {
    while (true) {
        val can = canAccept(nationalities, colors, beverages, cigarettes, pets)
        when (can) {
            Can.YES -> {
                assert(check(nationalities, colors, beverages, cigarettes, pets) == null)
                add(
                    Pentuple(nationalities.filterNotNull().toTypedArray(),
                    colors.filterNotNull().toTypedArray(),
                    beverages.filterNotNull().toTypedArray(),
                    cigarettes.filterNotNull().toTypedArray(),
                    pets.filterNotNull().toTypedArray())
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

private fun canAccept(nationalities: Array<out Nationalities?>, colors: Array<out Colors?>, beverages: Array<out Beverages?>,
                      cigarettes: Array<out CigaretteBrands?>, pets: Array<out Pets?>) :Can {
    var candidate :Can = Can.YES
    var result = canFirst(nationalities, colors)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE

    result = canSecond(nationalities, pets)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE

    result = canThird(nationalities, beverages)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE

    result = canFourth(colors)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE

    result = canFifth(colors, beverages)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE

    result = canSixth(cigarettes, pets)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE

    result = canSeventh(colors, cigarettes)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE

    result = canEighth(beverages)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE

    result = canNinth(nationalities)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE

    result = canTenth(cigarettes, pets)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE

    result = canEleventh(pets, cigarettes)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE

    result = canTwelfth(cigarettes, beverages)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE

    result = canThirteenth(nationalities, cigarettes)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE

    result = canFourteenth(nationalities, colors)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE

    result = canFifteenth(cigarettes, beverages)
    if (result== Can.NO || result is Can.Better<*>)
        return result
    if (result== Can.MAYBE)
        candidate = Can.MAYBE
    return candidate
}

private fun check(nationalities: Array<out Nationalities?>, colors: Array<out Colors?>, beverages: Array<out Beverages?>,
                  cigarettes: Array<out CigaretteBrands?>, pets: Array<out Pets?>): Int? {
    println("Does "+ (nationalities zip colors zip beverages zip cigarettes zip pets).mapIndexed { nr, (q, p :Pets?) ->
        "the ${q.first.first.first} live in the ${nr+1}th ${q.first.first.second} house drinking ${q.first.second} smoking ${q.second} breeding $p"
    }.joinToString(",\n\t") +"?")
    if (canFirst(nationalities, colors) != Can.YES)
        return 1
    if (canSecond(nationalities, pets) != Can.YES)
        return 2
    if (canThird(nationalities, beverages) != Can.YES)
        return 3
    if (canFourth(colors) != Can.YES)
        return 4
    if (canFifth(colors, beverages) != Can.YES)
        return 5
    if (canSixth(cigarettes, pets) != Can.YES)
        return 6
    if (canSeventh(colors, cigarettes) != Can.YES)
        return 7
    if (canEighth(beverages) != Can.YES)
        return 8
    if (canNinth(nationalities) != Can.YES)
        return 9
    if (canTenth(cigarettes, pets) != Can.YES)
        return 10
    if (canEleventh(pets, cigarettes) != Can.YES)
        return 11
    if (canTwelfth(cigarettes, beverages) != Can.YES)
        return 12
    if (canThirteenth(nationalities, cigarettes) != Can.YES)
        return 13
    if (canFourteenth(nationalities, colors) != Can.YES)
        return 14
    if (canFifteenth(cigarettes, beverages) != Can.YES)
        return 15
    val nr = pets.indexOf(Pets.FISH)
    if (nr>=0)
        println("The ${nationalities[nr]} owns the ${Pets.FISH}.")
    return null
}

private fun canFirst(nationalities: Array<out Nationalities?>, colors: Array<out Colors?>) :Can {
    val nr = nationalities.indexOf(Nationalities.CHINESE)
    val nc = colors.indexOf(Colors.RED)
    if (nr<0 && nc<0)
        return Can.MAYBE
    if (nr<0)
        return if (nationalities[nc]!=null && nationalities[nc]!= Nationalities.CHINESE)
            Can.NO
        else
            Can.Better(nc, Nationalities.CHINESE)
    if (nc<0)
        return if (colors[nr]!=null && colors[nr]!= Colors.RED)
            Can.NO
        else
            Can.Better(nr, Colors.RED)
    return if (nr==nc) Can.YES else Can.NO
}

private fun canSecond(nationalities: Array<out Nationalities?>, pets: Array<out Pets?>) :Can {
    val nr = nationalities.indexOf(Nationalities.VIETNAMESE)
    val np = pets.indexOf(Pets.DOGS)
    if (nr<0 && np<0)
        return Can.MAYBE
    if (nr<0)
        return if (nationalities[np]!=null && nationalities[np]!= Nationalities.VIETNAMESE)
            Can.NO
        else
            Can.Better(np, Nationalities.VIETNAMESE)
    if (np<0)
        return if (pets[nr]!=null && pets[nr]!= Pets.DOGS)
            Can.NO
        else
            Can.Better(nr, Pets.DOGS)
    return if (nr==np) Can.YES else Can.NO
}

private fun canThird(nationalities: Array<out Nationalities?>, beverages: Array<out Beverages?>) :Can {
    val nr = nationalities.indexOf(Nationalities.MONGOLIAN)
    val nb = beverages.indexOf(Beverages.YELLOW_WINE)
    if (nr<0 && nb<0)
        return Can.MAYBE
    if (nr<0)
        return if (nationalities[nb]!=null && nationalities[nb]!= Nationalities.MONGOLIAN)
            Can.NO
        else
            Can.Better(nb, Nationalities.MONGOLIAN)
    if (nb<0)
        return if (beverages[nr]!=null && beverages[nr]!= Beverages.YELLOW_WINE)
            Can.NO
        else
            Can.Better(nr, Beverages.YELLOW_WINE)
    return if (nr==nb) Can.YES else Can.NO
}

private fun canFourth(colors: Array<out Colors?>) :Can {
    val nr = colors.indexOf(Colors.GREEN)
    val nc = colors.indexOf(Colors.WHITE)
    if (nr<0 || nc<0)
        return Can.MAYBE
    return if (nr==nc+1 || nr+1==nc) Can.YES else Can.NO
}

private fun canFifth(colors: Array<out Colors?>, beverages: Array<out Beverages?>) :Can {
    val nr = colors.indexOf(Colors.GREEN)
    val nb = beverages.indexOf(Beverages.SAKE)
    if (nr<0 && nb<0)
        return Can.MAYBE
    if (nr<0)
        return if (colors[nb]!=null && colors[nb]!= Colors.GREEN)
            Can.NO
        else
            Can.Better(nb, Colors.GREEN)
    if (nb<0)
        return if (beverages[nr]!=null && beverages[nr]!= Beverages.SAKE)
            Can.NO
        else
            Can.Better(nr, Beverages.SAKE)
    return if (nr==nb) Can.YES else Can.NO
}

private fun canSixth(cigarettes: Array<out CigaretteBrands?>, pets: Array<out Pets?>) :Can {
    val nr = cigarettes.indexOf(CigaretteBrands.LIQUN_BROWN)
    val np = pets.indexOf(Pets.BIRDS)
    if (nr<0 && np<0)
        return Can.MAYBE
    if (nr<0)
        return if (cigarettes[np]!=null && cigarettes[np]!= CigaretteBrands.LIQUN_BROWN)
            Can.NO
        else
            Can.Better(np, CigaretteBrands.LIQUN_BROWN)
    if (np<0)
        return if (pets[nr]!=null && pets[nr]!= Pets.BIRDS)
            Can.NO
        else
            Can.Better(nr, Pets.BIRDS)
    return if (nr==np) Can.YES else Can.NO
}

private fun canSeventh(colors: Array<out Colors?>, cigarettes: Array<out CigaretteBrands?>) :Can {
    val nr = colors.indexOf(Colors.YELLOW)
    val nc = cigarettes.indexOf(CigaretteBrands.HERO)
    if (nr<0 && nc<0)
        return Can.MAYBE
    if (nr<0)
        return if (colors[nc]!=null && colors[nc]!= Colors.YELLOW)
            Can.NO
        else
            Can.Better(nc, Colors.YELLOW)
    if (nc<0)
        return if (cigarettes[nr]!=null && cigarettes[nr]!= CigaretteBrands.HERO)
            Can.NO
        else
            Can.Better(nr, CigaretteBrands.HERO)
    return if (nr==nc) Can.YES else Can.NO
}

private fun canEighth(beverages: Array<out Beverages?>) :Can {
    if (beverages.getOrNull(2)== Beverages.WHITE_SPIRIT)
        return Can.YES
    if (beverages.getOrNull(2)==null)
        return Can.Better(2, Beverages.WHITE_SPIRIT)
    return Can.NO
}

private fun canNinth(nationalities: Array<out Nationalities?>) :Can {
    if (nationalities.getOrNull(0)==null)
        return Can.Better(0, Nationalities.KOREAN)
    return if (nationalities.getOrNull(0)== Nationalities.KOREAN)
        Can.YES
    else Can.NO
}

private fun canTenth(cigarettes: Array<out CigaretteBrands?>, pets: Array<out Pets?>) :Can {
    val nr = cigarettes.indexOf(CigaretteBrands.GRAVEN_A)
    val np = pets.indexOf(Pets.CATS)
    if (nr<0 || np<0)
        return Can.MAYBE
    return if (nr==np+1 || nr+1==np) Can.YES else Can.NO
}

private fun canEleventh(pets: Array<out Pets?>, cigarettes: Array<out CigaretteBrands?>) :Can {
    val nr = pets.indexOf(Pets.HORSES)
    val nc = cigarettes.indexOf(CigaretteBrands.HERO)
    if (nr<0 || nc<0)
        return Can.MAYBE
    return if (nr==nc+1 || nr+1==nc) Can.YES else Can.NO
}

private fun canTwelfth(cigarettes: Array<out CigaretteBrands?>, beverages: Array<out Beverages?>) :Can {
    val nr = cigarettes.indexOf(CigaretteBrands.MEVIUS)
    val nb = beverages.indexOf(Beverages.BEER)
    if (nr<0 && nb<0)
        return Can.MAYBE
    if (nr<0)
        return if (cigarettes[nb]!=null && cigarettes[nb]!= CigaretteBrands.MEVIUS)
            Can.NO
        else
            Can.Better(nb, CigaretteBrands.MEVIUS)
    if (nb<0)
        return if (beverages[nr]!=null && beverages[nr]!= Beverages.BEER)
            Can.NO
        else
            Can.Better(nr, Beverages.BEER)
    return if (nr==nb) Can.YES else Can.NO
}

private fun canThirteenth(nationalities: Array<out Nationalities?>, cigarettes: Array<out CigaretteBrands?>) :Can {
    val nr = nationalities.indexOf(Nationalities.JAPANESE)
    val nc = cigarettes.indexOf(CigaretteBrands.NANJING_CAPSULE)
    if (nr<0 && nc<0)
        return Can.MAYBE
    if (nr<0)
        return if (nationalities[nc]!=null && nationalities[nc]!= Nationalities.JAPANESE)
            Can.NO
        else
            Can.Better(nc, Nationalities.JAPANESE)
    if (nc<0)
        return if (cigarettes[nr]!=null && cigarettes[nr]!= CigaretteBrands.NANJING_CAPSULE)
            Can.NO
        else
            Can.Better(nr, CigaretteBrands.NANJING_CAPSULE)
    return if (nr==nc) Can.YES else Can.NO
}

private fun canFourteenth(nationalities: Array<out Nationalities?>, colors: Array<out Colors?>) :Can {
    val nr = nationalities.indexOf(Nationalities.KOREAN)
    val nc = colors.indexOf(Colors.BLUE)
    if (nr<0 || nc<0)
        return Can.MAYBE
    return if (nr==nc+1 || nr+1==nc) Can.YES else Can.NO
}

private fun canFifteenth(cigarettes: Array<out CigaretteBrands?>, beverages: Array<out Beverages?>) :Can {
    val nr = cigarettes.indexOf(CigaretteBrands.GRAVEN_A)
    val nb = beverages.indexOf(Beverages.WATER)
    if (nr<0 || nb<0)
        return Can.MAYBE
    return if (nr==nb+1 || nr+1==nb) Can.YES else Can.NO
}
