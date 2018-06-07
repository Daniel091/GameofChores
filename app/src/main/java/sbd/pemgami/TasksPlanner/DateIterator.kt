package sbd.pemgami.TasksPlanner

import java.time.LocalDateTime

class DateIterator(val startDate: LocalDateTime,
                   val endDate: LocalDateTime,
                   val stepDays: Long) : Iterator<LocalDateTime> {
    private var currentDate = startDate

    override fun hasNext() = currentDate <= endDate

    override fun next(): LocalDateTime {
        val next = currentDate
        currentDate = currentDate.plusDays(stepDays)
        return next
    }

}