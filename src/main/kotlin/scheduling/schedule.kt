package scheduling
/**
 * Finding a simple scheduling solution for some items with few constraints.
 **/

import kotlin.random.Random

val random = Random(System.currentTimeMillis())

data class Item(val subject :String, val grade :UShort =1u, val section :UShort =1u, val version :Short =1, val duration :UShort =1u) {
    override fun toString() = "$grade${(section.toInt()-1+'a'.code).toChar()}) $subject" + if(duration>1u) " (${duration}h)" else ""

    fun forge(grade :UShort =this.grade, section :UShort =this.section, version :Short =random.nextInt().toShort(),
              duration :UShort =this.duration) =
        Item(subject, grade, section, version, duration)

    override fun hashCode() = 1342342+ subject.hashCode() +31*grade.hashCode() +97*section.hashCode() +137*version.hashCode() +
            197*duration.hashCode()

    override fun equals(other :Any?) :Boolean {
        if (this===other) return true
        if (other !is Item) return false
        return grade==other.grade && section==other.section && version==other.version && duration==other.duration &&
                subject==other.subject
    }
}

enum class Can {
    No, Maybe, Yes
}

data class Slot(val time :Short, val items :MutableSet<Item> =mutableSetOf()) {
    override fun toString() = "$time: " + items.sortedBy { it.section }.joinToString(",\t")
}

data class Day(val number :Short, val slots :MutableList<Slot> =mutableListOf()) {
    override fun toString() = "\n"+ toWeekday(number) +"\n"+ slots.sortedBy { it.time }.joinToString("\n")

    fun schedule(item :Item, resourceLimits :List<ResourceLimit> =emptyList(),
                 dayConstraints :List<DayConstraint> =emptyList()) :Boolean {
        var candidate :Slot? = null
        for (slot in slots) {
            if (resourceLimits.all { it.can(item, slot) != Can.No }) {
                if (item.duration>1u) {
                    if (candidate!=null) {
                        candidate.items.add(item)
                    } else {
                        candidate = slot
                        continue
                    }
                }
                slot.items.add(item)
                if (resourceLimits.all { it.isValid(slot) }) {
                    return true
                } else
                    slot.items.remove(item)
            } else
                candidate = null
        }
        val t = (slots.size +1).toShort()
        val slot = Slot(t, mutableSetOf(item))
        if (dayConstraints.all { it.can(slot, this) != Can.No}) {
            if (item.duration>1u) {
                if (candidate!=null) {
                    candidate.items.add(item)
                } else {
                    slots.add(slot)
                    candidate = Slot((t+1).toShort(), mutableSetOf(item))
                    if (dayConstraints.all { it.can(candidate, this) != Can.No}) {
                        slots.add(candidate)
                        return true
                    }
                    slots.remove(slot)
                    return false
                }
            }
            slots.add(slot)
            return true
        }
        return false
    }

    fun remove(item :Item) :Boolean {
        for (slot in slots)
            if (slot.items.remove(item)) {
                if (slot.items.isEmpty())
                    slots.remove(slot)
                return true
            }
        return false
    }
}

class Schedule {
    val days = mutableListOf<Day>()
    private val dayConstraints = mutableListOf<DayConstraint>()
    private val resourceLimits = mutableListOf<ResourceLimit>()

    override fun toString() = days.sortedBy { it.number }.joinToString("\n\n")

    fun schedule(items :List<Item>) {
        if (items.isEmpty()) return
        if (days.isEmpty()) days.add(Day(1, mutableListOf()))
        for (item in items) {
            var scheduled = false
            for (day in days)
                if (dayConstraints.all { it.can(item, day) != Can.No }) {
                    if (!day.schedule(item, resourceLimits, dayConstraints))
                        continue
                    if (dayConstraints.all { it.isValid(day) }) {
                        scheduled = true
                        break
                    } else
                        day.remove(item)
                }
            if (!scheduled) {
                val newDay = Day((days.last().number+1).toShort())
                days.add(newDay)
                if (!newDay.schedule(item, resourceLimits, dayConstraints))
                    println("Item $item could not be scheduled!")
            }
        }
    }

    fun scheduleForSection(section :UShort, items :List<Item>) = schedule(items.map { it.copy(section= section) })

    fun addConstraint(c :DayConstraint) :Boolean {
        return if (days.all { c.isValid(it) }) {
            dayConstraints.add(c)
            true
        } else
            false
    }

    fun addResourceLimit(r :ResourceLimit) :Boolean {
        return if (days.all { it.slots.all { r.isValid(it) }}) {
            resourceLimits.add(r)
            true
        } else
            false
    }
}

fun toWeekday(number :Short) :String = when (number%7) {
    0 -> "Sunday"
    1 -> "Monday"
    2 -> "Tuesday"
    3 -> "Wednesday"
    4 -> "Thursday"
    5 -> "Friday"
    else -> "Saturday"
} + if (number>=7) (number/7 +1).toString()  else ""

data class Section(val number :UShort, val level :Short, val numStudents :UInt, val courses :MutableMap<Item, UShort> =mutableMapOf()) {
    override fun toString() = "$level${(number.toInt() + 'a'.code).toChar()}: "+ courses.entries.joinToString { "${it.key} (${it.value})" }
}

data class Level(val number :Short, val numSections :UShort, val courses :MutableMap<Item, UShort> =mutableMapOf()) {
    override fun toString() = "grade $number: "+ courses.entries.joinToString { "${it.key} (${it.value})" }
}

fun Random.flipCoin(bias :Double =0.5) = nextDouble() < bias

fun <T> List<T>.poisson(random :Random) :T {
    val s = (2..size).sumOf { 1.0/it }
    val v = random.nextDouble()
    var cum = 0.0
    for (i in 0..<size) {
        cum += 1.0/(i+2)
        if (v < cum/s)
            return get(i)
    }
    return first()
}
