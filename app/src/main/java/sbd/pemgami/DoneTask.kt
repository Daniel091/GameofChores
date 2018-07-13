package sbd.pemgami

import sbd.pemgami.TasksPlanner.Task

data class DoneTask(val name: String = "", val uid: String = "", val user: String = "", val time: Long = 0, val duration: Int = 0, val rotatable: Boolean = false, val doneBy: String = "", val doneAt: Long = 0) {

    // second constructor makes mapping task doneTask easier
    constructor(task: Task, doneBy: String = "", doneAt: Long = 0) : this(task.name, task.uid, task.user, task.time, task.duration, task.rotatable, doneBy, doneAt)

}