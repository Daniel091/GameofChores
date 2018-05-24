package sbd.pemgami

object CurrentWG {
    var name: String = ""
    var uid: String = ""
    var admin: String = ""
    var users: List<String> = listOf()

    fun init(name: String = "", uid: String = "", admin: String = "", users: List<String> = listOf()) {
        this.admin = admin
        this.name = name
        this.uid = uid
        this.users = users
    }
}