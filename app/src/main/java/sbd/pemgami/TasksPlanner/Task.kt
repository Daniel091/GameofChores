package sbd.pemgami.TasksPlanner

data class Task(val name: String = "", val user: String = "", val time: Long = 0, val duration: Int = 0, val rotatable: Boolean = false)