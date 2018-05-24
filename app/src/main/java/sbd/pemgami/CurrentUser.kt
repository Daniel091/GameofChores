package sbd.pemgami

object CurrentUser {
    var name: String = ""
    var email: String = ""
    var uid: String = ""
    var wg_id: String = ""

    fun init(name: String = "", email: String = "", uid: String = "", wg_id: String = "") {
        this.name = name
        this.email = email
        this.uid = uid
        this.wg_id = wg_id
    }
}