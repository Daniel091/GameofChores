package sbd.pemgami.TasksPlanner

import java.time.LocalDateTime

class DateProgression(override val start: LocalDateTime,
                      override val endInclusive: LocalDateTime,
                      val stepDays: Long = 1) :
        Iterable<LocalDateTime>, ClosedRange<LocalDateTime> {

    override fun iterator(): Iterator<LocalDateTime> =
            DateIterator(start, endInclusive, stepDays)

    infix fun step(days: Long) = DateProgression(start, endInclusive, days)

}