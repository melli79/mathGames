package search

import java.time.LocalDate
import java.util.UUID
import java.util.stream.Stream

class LoyalCustomers {
    private val customerId2pageId2Date = mutableMapOf<UUID, MutableMap<UUID, MutableList<LocalDate>>>()

    fun parseDay(entries :Stream<String>, date :LocalDate) = entries.forEach { entry ->
        val fields = entry.trim().split(',', limit= 3)
        if (fields.size>=2) {
            val customerId = UUID.fromString(fields[0].trim())
            val pageId = UUID.fromString(fields[1].trim())
            customerId2pageId2Date.compute(customerId) { _ :UUID, pageId2dates :MutableMap<UUID, MutableList<LocalDate>>? ->
                pageId2dates?.compute(pageId) { _ :UUID, dates :MutableList<LocalDate>? ->
                    dates?.add(date)
                    dates ?: mutableListOf(date)
                }
                pageId2dates ?: mutableMapOf(Pair(pageId, mutableListOf(date)))
            }
        }
    }

    fun findLoyalCustomers(threshold :Int =2) = customerId2pageId2Date.filter { it.value.size>=threshold }.map { it.key }.toSet()
}
