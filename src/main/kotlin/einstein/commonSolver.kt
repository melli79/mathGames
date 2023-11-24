package einstein

interface Option<T> : Comparable<T> {
    val type :Type
}

enum class Type {
    COLOR, NATIONALITY, BEVERAGE, CIGARETTE_BRAND, PET;

    fun toVarName() = name.lowercase() + "s"
}

enum class Colors(override val type :Type = Type.COLOR) : Option<Colors> {
    RED, GREEN, WHITE, YELLOW, BLUE;

    override fun toString() = name.lowercase()
}

enum class Pets(override val type :Type = Type.PET) : Option<Pets> {
    DOGS, BIRDS, CATS, HORSES, FISH;

    override fun toString() = name.lowercase()
}

sealed interface Can {
    data object YES : Can
    data object NO : Can

    data class Better<T : Option<T>>(val index :Int, val value :T) : Can {
        override fun toString() = "${value.type.toVarName()}[$index] = $value"
    }

    data object MAYBE : Can
}

data class Pentuple<F, S, T, Fo, Fi>(
    val first :F, val second :S, val third :T,
    val fourth :Fo, val fifth :Fi
) {}

inline fun <reified T : Option<T>> permute(options :List<T>, prefilled :Array<out T?>) :List<Array<T?>> {
    val result = mutableListOf<Array<T?>>()
    val remainders :MutableSet<T> = options.toMutableSet()
    for (option in prefilled) if (option != null) {
        if (option !in remainders) {
            println("Repeated elements in prefill: $prefilled")
            return emptyList()
        } else {
            remainders.remove(option)
        }
    }
    val permutation = mutableListOf<T?>()
    var i = 0
    while (i >= 0) {
        while (i < options.size) {
            if (prefilled[i] != null) {
                permutation.add(prefilled[i])
            } else {
                val candidate = remainders.removeFirst()
                if (candidate != null)
                    permutation.add(candidate)
                else {
                    println("insufficient number of options, missing ${options.size - i}")
                    break
                }
            }
            i++
        }
        if (i == options.size) {
            assert(permutation.all { it != null })
            result.add(permutation.toTypedArray())
        }
        i--
        while (i >= 0) {
            if (prefilled[i] == null) {
                val candidate = remainders.next(permutation[i]!!)
                if (candidate != null) {
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

fun <T : Comparable<T>> MutableSet<T>.removeFirst() :T? {
    val result = minOrNull()
    if (result != null)
        remove(result)
    return result
}

fun <T : Comparable<T>> MutableSet<T>.next(current :T) :T? {
    add(current)
    val list = this.sorted()
    var index = list.indexOf(current)
    while (index >= 0 && index < list.size) {
        if (list[index] == current)
            index++
        else
            break
    }
    val result = list.getOrNull(index)
    if (result != null)
        remove(result)
    return result
}
