package sbd.pemgami.TasksPlanner

import android.content.Context
import android.util.Log
import sbd.pemgami.R
import java.util.*
import java.util.UUID

object TaskFactory {

    fun createTasks(name: String, user: String, start_time: Date, end_time: Date, duration: Int, rotatable: Boolean, taskTime: String, users: List<String>, context: Context): Map<String, Task>? {
        val taskMap = mutableMapOf<String, Task>()
        var rStart = 0

        // get list of dates where new event should occure
        val calSpecific = getTaskTimes(context, taskTime)
        val dates = getDatesInRange(start_time, end_time, calSpecific)

        // create tasks
        var taskOwner = user
        for (date in dates) {
            Log.d("Date", date.toString())

            // rotate taskOwner
            if (rotatable) {
                taskOwner = users[rStart]
                rStart++
                if (rStart == users.count()) {
                    rStart = 0
                }
            }

            val uniqueID = UUID.randomUUID().toString()
            val task = Task(name, uniqueID, taskOwner, date.time, duration, rotatable)
            taskMap[uniqueID] = task
        }

        return taskMap
    }

    private fun getTaskTimes(context: Context, taskTime: String): Int {
        val res = context.resources
        val taskTimes = res.getStringArray(R.array.taskTimes)
        val calList = listOf(0, Calendar.DATE, Calendar.WEEK_OF_MONTH, Calendar.MONTH, Calendar.YEAR)
        return calList[taskTimes.indexOf(taskTime)]
    }


    /*
        whatTime can have values from 0, Calendar.DATE, Calendar.WEEK_OF_MONTH, Calendar.MONTH, Calendar.YEAR
     */
    private fun getDatesInRange(t1: Date, t2: Date, whatTime: Int): List<Date> {
        val mList = mutableListOf<Date>()

        val calendarSt = Calendar.getInstance()
        calendarSt.time = t1

        val calendarEnd = Calendar.getInstance()
        calendarEnd.time = t2

        // whatTime is 0, when Task just has to arise once
        if (whatTime != 0) {
            // before is exclusive, so add 1 day unit to calendarEnd
            calendarEnd.add(Calendar.DATE, 1)

            while (calendarSt.before(calendarEnd)) {
                val result = calendarSt.time
                mList.add(result)
                calendarSt.add(whatTime, 1)
            }
        } else {
            mList.add(t1)
        }

        return mList
    }
}