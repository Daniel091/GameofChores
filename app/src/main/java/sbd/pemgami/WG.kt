package sbd.pemgami

data class WG(val name: String = "", val uid: String = "", val admin: String = "", val users: MutableList<String> = mutableListOf())