package sbd.pemgami

import com.google.firebase.database.FirebaseDatabase

object Constants {
    val databaseRoot = FirebaseDatabase.getInstance().reference
    val databaseUsers = databaseRoot.child("users")
    val databaseWGs = databaseRoot.child("wgs")
}