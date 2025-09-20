package scheduling

fun Collection<Student>.collectDistributionNormalizePreferences(
    subjects :List<Subject>,
    dm :List<Subject>,
    sciences :List<String>,
    humanities :List<String>,
    languages :List<String>,
    am :List<String>
) :Map<Subject, MutableSet<Student>> {
    val distribution :Map<Subject, MutableSet<Student>> = subjects.associateWith { mutableSetOf() }
    for (student in this) {
        for (i in student.preferences.indices) {
            val s = student.preferences[i]
            val s1s = distribution[s]
            if (s1s != null) s1s.add(student)
            else when (s.name) {
                in sciences -> student.replaceSubject(distribution, sciences, dm, s, subjects, i)

                in humanities -> student.replaceSubject(distribution, humanities, dm, s, subjects, i)

                in languages -> student.replaceSubject(distribution, languages, dm, s, subjects, i)

                else -> {
                    if (s.importance == Importance.Major) {
                        val options = student.preferences.drop(3).map { it.name }.toSet()
                        val alternative :Subject = subjects.firstOrNull {
                            it.importance== Importance.Major &&it.name in options
                        } ?.copy(importance= Importance.Major)
                            ?: subjects.filter { it.importance == Importance.Major }.random(random)
                        distribution[alternative]!!.add(student)
                        student.preferences[i] = alternative
                        student.preferences.remove(alternative.copy(importance= Importance.Minor))
                        student.preferences.add(s.copy(importance= Importance.Minor))
                    } else { // replace s with music or art
                        val alternative =
                            distribution.entries.first { it.key.importance == Importance.Minor && it.key.name in am }
                        alternative.value.add(student)
                        student.preferences[i] = alternative.key
                    }
                }
            }
        }
    }
    return distribution
}

private fun Student.replaceSubject(
    distribution :Map<Subject, MutableSet<Student>>,
    equals :List<String>,
    excluded :List<Subject>,
    s :Subject,
    subjects :List<Subject>,
    i :Int
) {
    val alternative = distribution.entries.firstOrNull { it.key.name in equals && it.key.importance == s.importance }
    if (alternative != null) {
        alternative.value.add(this)
        preferences[preferences.indexOf(s)] = alternative.key
    } else {
        val altS = subjects.filter { it.importance == s.importance && it !in excluded }.random(random)
        distribution[altS]!!.add(this)
        preferences[i] = altS
    }
}

enum class Importance(val number :UShort) {
    Minor(3u), Major(5u)
}

data class Subject(val name :String, val importance :Importance) {
    override fun toString() = if (importance==Importance.Major) "HF $name"
        else "NF $name"

    override fun hashCode() = 2453453+ importance.hashCode() +3* name.hashCode()

    override fun equals(other :Any?) :Boolean {
        if (this===other) return true
        if (other !is Subject) return false
        return importance==other.importance && name==other.name
    }
}

data class Student(val name :String, val level :Short =11, val preferences :MutableList<Subject> =mutableListOf()) {
    override fun toString() = "${level}. $name: "+ preferences.joinToString { it.name }
}

fun Collection<Student>.groupBySubject() :Map<Subject, UInt> {
    val result = mutableMapOf<Subject, UInt>()
    for (student in this) {
        for (subject in student.preferences.sortedByDescending { it.importance }) {
            val count :UInt = result[subject] ?: 0u
            result[subject] = count+1u
        }
    }
    return result
}

fun Collection<Student>.correlations() :Map<Subject, Map<Subject, UInt>> {
    val subjects = groupBySubject().entries
    val result = subjects.associate { (s, _) ->
       Pair(s, subjects.associate { (s2, c) ->
           Pair(s2, if (s==s2) c  else 0u)
       }.toMutableMap())
    }.toMutableMap()

    for (student in this)
        for (s in student.preferences) for (s2 in student.preferences)
            if (s!=s2) {
                val row = result[s]!!
                row[s2] = row[s2]!! + 1u
            }

    return result
}