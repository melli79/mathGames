package scheduling

/**
 * types of constraints:
 * Resource: no more than c items per slot (for every slot)
 * day limit: no ItemType more than once per day (for every day every ItemType)
 * double lecture: a double lecture must be replicated in one neighboring slot (every day)
 */
sealed interface Constraint {
    val name :String
}

abstract class ResourceLimit(override val name :String) :Constraint {
    open fun can(item :Item, slot :Slot) :Can = Can.Maybe
    abstract fun isValid(slot :Slot) :Boolean
}

abstract class DayConstraint(override val name :String) :Constraint {
    open fun can(item :Item, day :Day) :Can = Can.Maybe
    open fun can(slot :Slot, day :Day) :Can = Can.Maybe
    abstract fun isValid(day :Day) :Boolean
}

class ShortDay :DayConstraint("kurzer Tag") {
    override fun can(slot :Slot, day :Day) = if (day.slots.size<4) Can.Yes  else Can.No
    override fun isValid(day :Day) :Boolean = day.slots.size <= 4
}

class MidlongDays :DayConstraint("mittellanger Tag") {
    override fun can(slot :Slot, day :Day) = if (day.slots.size<6 && (day.slots.size<4
        || day.number==2.toShort() || day.number==4.toShort())) Can.Yes  else Can.No
    override fun isValid(day :Day) :Boolean = day.slots.size <= 6 && (day.slots.size<=4
        || day.number==2.toShort() || day.number==4.toShort())
}

class SingleSubject :ResourceLimit("einzelne Klasse") {
    override fun can(item :Item, slot :Slot) = if (slot.items.isEmpty()) Can.Maybe  else Can.No
    override fun isValid(slot :Slot) = slot.items.size <= 1
}

class SingleSubjectPerSection :ResourceLimit("ein Thema pro Parallelklasse") {
    override fun can(item :Item, slot :Slot) = if (slot.items.all { it.section!=item.section })
      Can.Maybe  else Can.No
    override fun isValid(slot :Slot) = slot.items.all { c -> slot.items.all { it===c || it.section!=c.section }}
}

class SingleTeacher(val subject :String) :ResourceLimit("nur 1 Lehrer für $subject") {
    override fun can(item :Item, slot :Slot) = if (item.subject!=subject || slot.items.none { it.subject==subject }) Can.Maybe  else Can.No
    override fun isValid(slot :Slot) = slot.items.count { it.subject==subject } <= 1
}

class SharableTeachers(val subject :String) :ResourceLimit("gemeinsame Lehrer für $subject") {
    override fun can(item :Item, slot :Slot) = if (item.subject!=subject || slot.items.none { it.subject==subject }
            || slot.items.all { (it.subject==subject) == (it.grade==item.grade) }) Can.Maybe  else Can.No
    override fun isValid(slot :Slot) = slot.items.filter { it.subject==subject }.groupBy { it.grade }.size <= 1
}

class DoubleLecture :DayConstraint("Doppelstunde") {
    override fun isValid(day :Day) :Boolean {
        var reqs = mutableListOf<Item>()
        for (slot in day.slots) {
            for (required in reqs)
                if (slot.items.none { it==required })
                    return false
            val newReqs = mutableListOf<Item>()
            for (item in slot.items) if (item.duration>1u)
                if (reqs.none { it==item })
                    newReqs.add(item)
            reqs = newReqs
        }
        return reqs.isEmpty()
    }
}