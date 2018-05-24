package sbd.pemgami

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object Constants {
    private val databaseRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    val databaseUsers: DatabaseReference = databaseRoot.child("users")
    val databaseWGs: DatabaseReference = databaseRoot.child("wgs")

    fun getCurrentUserWGRef(): DatabaseReference? {
        return databaseUsers.child(CurrentUser.uid).child("wg_id")
    }
}