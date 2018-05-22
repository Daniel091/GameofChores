package sbd.pemgami

object CurrentUser {
    private var name: String = ""
    private var email: String = ""
    private var uid: String = ""

    fun init(name: String = "", email: String = "", uid: String = "") {
        this.name = name
        this.email = email
        this.uid = uid
    }
}