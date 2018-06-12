package sbd.pemgami

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object Constants {
    val databaseRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    val databaseUsers: DatabaseReference = databaseRoot.child("users")
    val databaseWGs: DatabaseReference = databaseRoot.child("wgs")
    val databaseTasks: DatabaseReference = databaseRoot.child("wg_tasks")


    fun getCurrentUserWGRef(uid: String): DatabaseReference? {
        return databaseUsers.child(uid).child("wg_id")
    }

    fun getUserNameRef(uid: String): DatabaseReference? {
        return databaseUsers.child(uid).child("name")
    }

    fun getTasksWGRef(uid: String) : DatabaseReference? {
        return databaseTasks.child(uid)
    }
}