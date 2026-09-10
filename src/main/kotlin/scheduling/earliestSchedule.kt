package scheduling

/**
 * The tasks consist of a duration and a deadline (when they need to be finished)
 * The goal is to produce as little total slack as possible.
 */

import common.math.sqr
import kotlin.math.abs
import kotlin.math.max

data class Task(val duration :UInt, val deadline :Int) {
}

fun schedule(tasks :Collection<Task>) :List<Task> {
    val comparator = compareBy<Task> { it.deadline }.thenBy { it.duration }
    return tasks.sortedWith(comparator)
}

enum class Norm {
    Counting {
        override fun norm(x :Int, n :ULong) = n+1uL
    }, L1 {
        override fun norm(x :Int, n :ULong) = n + abs(x).toULong()
    }, L2 {
        override fun norm(x :Int, n :ULong) = n + sqr(x)
    }, Sup {
        override fun norm(x :Int, n :ULong) = max(n, abs(x).toULong())
    };

    abstract fun norm(x :Int, n :ULong=0uL) :ULong
}

fun computeSlack(schedule :List<Task>, p :Norm =Norm.Sup) :ULong {
    var time = 0
    var slack = 0uL
    for (t in schedule) {
        time += t.duration.toInt()
        val s1 = time -t.deadline
        if (s1>0) slack = p.norm(s1, slack)
    }
    return slack
}
