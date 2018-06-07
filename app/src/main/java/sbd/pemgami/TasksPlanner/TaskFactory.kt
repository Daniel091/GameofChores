package sbd.pemgami.TasksPlanner

import android.content.Context
import android.util.Log
import sbd.pemgami.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


object TaskFactory {

    // for custom date operator
    operator fun LocalDateTime.rangeTo(other: LocalDateTime) = DateProgression(this, other)

    fun createTasks(name: String, user: String, start_time: Date, end_time: Date, duration: Int, rotatable: Boolean, taskTime: String, users: List<String>, context: Context): List<Task>? {
        val taskList = mutableListOf<Task>()
        var rStart = 0

        val lStartTime = convertToLocalDate(start_time)
        val lEndTime = convertToLocalDate(end_time)

        // get days e.g one time is 0; daily is 1, ... etc.
        // month is a the moment always 30 days long ...
        val days = getTaskTimes(context, taskTime)

        // if One Time was selected
        if (days.toInt() == 0) {
            val timestamp = lStartTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val task = Task(name, user, timestamp, duration, rotatable)
            taskList.add(task)
            return taskList
        }

        
        var taskOwner = user
        // fancy step through by x amount of days
        for (date in lStartTime..lEndTime step days) {
            Log.d("Date", date.toString())

            // rotate taskOwner
            if (rotatable) {
                taskOwner = users[rStart]
                rStart++
                if (rStart == users.count()) {
                    rStart = 0
                }
            }

            val timestamp = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val task = Task(name, taskOwner, timestamp, duration, rotatable)
            taskList.add(task)
        }

        return taskList
    }

    private fun convertToLocalDate(date: Date): LocalDateTime {
        val instant = Instant.ofEpochMilli(date.time)
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    private fun getTaskTimes(context: Context, taskTime: String): Long {
        val res = context.resources
        val daysTaskTimes = res.getIntArray(R.array.mappedDays)
        val taskTimes = res.getStringArray(R.array.taskTimes)
        return daysTaskTimes[taskTimes.indexOf(taskTime)].toLong()
    }
}