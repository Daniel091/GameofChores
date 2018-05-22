package sbd.pemgami

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.activity_wgform.*


class WGFormActivity : AppCompatActivity() {
    private val TAG = "WGFormAcitivty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wgform)

        input_usrname.setText(CurrentUser.name)
        btnFind.setOnClickListener { findWG(input_wg_id.text.toString()) }
        link_createWG.setOnClickListener { showWGCreateActivity() }
    }

    private fun showWGCreateActivity() {

    }

    private fun findWG(id: String) {
        progressBar2.visibility = View.VISIBLE

        val getWG: Query = Constants.databaseWGs.child("id").equalTo(id)

        val getListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //val usr = dataSnapshot.getValue(User::class.java)
                //Log.d(TAG, "Got User: ${usr?.email}")

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "ErrorCode ${databaseError.code}, DatabaseDetails: ${databaseError.details}")
                //progressBar2.visibility = View.INVISIBLE
            }
        }
        getWG.addListenerForSingleValueEvent(getListener)
    }
}

