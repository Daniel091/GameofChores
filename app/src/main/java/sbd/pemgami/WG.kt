package sbd.pemgami

import java.util.*

data class WG(val name: String = "", val uid: String = "", val admin: String = "", val users: MutableList<String> = mutableListOf(), val time_cleared: Long = Date().time, var jackpot: Int = 1000)