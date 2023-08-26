package people

import java.time.LocalDate

data class PartialDate(val year :UShort, val month :UByte? =null, val day :UByte? =null) :Comparable<PartialDate> {

    companion object {
        fun today() : PartialDate {
            val now = LocalDate.now()
            return PartialDate(now.year.toUShort(), now.monthValue.toUByte(), now.dayOfMonth.toUByte())
        }
    }

    override fun toString() = if (month==null)
        year.toString()
    else if (day==null)
        "$year/$month"
    else
        "$year-$month-$day"

    override fun compareTo(other : PartialDate) :Int {
        if (year!=other.year)
            return year.compareTo(other.year)
        if (month==null || other.month==null)
            return 0
        if (month!=other.month)
            return month.compareTo(other.month)
        if (day==null || other.day==null)
            return 0
        return day.compareTo(other.day)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is PartialDate)
            return false
        return year==other.year && (
                month==null || other.month==null || month==other.month
            ) && (
                day==null || other.day==null || day==other.day
            )
    }

    override fun hashCode() = 3 +197* year.hashCode() +
        31*(month?.hashCode() ?: 0) + (day?.hashCode() ?: 0)
}