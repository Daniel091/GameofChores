package sbd.pemgami

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_start.*
import java.util.*


class StartActivity : AppCompatActivity() {
    private val TAG: String = "StartActivity"
    private val RC_SIGN_IN: Int = 123

    private val fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        /*
            Routing:
            1. check usr
            2. if no usr -> FirebaseUI
            3. if new usr or unknown usr -> checkUserIn Firebase
            doRouting method:
            4. if usr and no wg -> WG Form
            5. if usr and wg -> HomeActivity
         */
        if (fbAuth.currentUser != null) {
            val usr = SharedPrefsUtils.readLastUserFromSharedPref(applicationContext)

            when {
                usr == null -> checkUserInDB(fbAuth.currentUser)
                fbAuth.currentUser?.uid != usr.uid -> checkUserInDB(fbAuth.currentUser)
                else -> doRouting(usr.wg_id)
            }
        } else {
            startFirebaseUI()
        }

    }

    private fun startFirebaseUI() {
        // add google, and email & pw provider
        val selectedProviders = ArrayList<AuthUI.IdpConfig>()
        selectedProviders.add(AuthUI.IdpConfig.EmailBuilder().build())
        selectedProviders.add(AuthUI.IdpConfig.GoogleBuilder().build())

        // start Firebase UI activity
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(selectedProviders)
                        .build(),
                RC_SIGN_IN)
    }

    // result from Firebase UI
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "ResultCode $resultCode, requestCode: $requestCode")

        if (resultCode == Activity.RESULT_OK && requestCode == RC_SIGN_IN) {
            val lastKnownUsr = SharedPrefsUtils.readLastUserFromSharedPref(applicationContext)
            if (fbAuth.currentUser?.uid == lastKnownUsr?.uid && lastKnownUsr != null) {
                doRouting(lastKnownUsr.wg_id)
            } else {
                checkUserInDB(fbAuth.currentUser)
            }

        } else {
            // e.g on back oder Sign In canceld
            finish()
        }
    }

    private fun checkUserInDB(user: FirebaseUser?) {
        if (user == null) return
        progressBar.visibility = View.VISIBLE

        // check if username exists in database
        val checkUser: Query = Constants.databaseUsers.orderByChild("uid").equalTo(user.uid)

        // set up listener
        val checkListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, dataSnapshot.childrenCount.toString())

                val len = dataSnapshot.childrenCount
                when {
                    len > 0 -> getUserData(user)
                    else -> writeUserData(user)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "ErrorCode ${databaseError.code}, DatabaseDetails: ${databaseError.details}")
                progressBar.visibility = View.INVISIBLE
            }
        }
        checkUser.addListenerForSingleValueEvent(checkListener)
    }


    private fun getUserData(user: FirebaseUser) {
        val getUser: Query = Constants.databaseUsers.child(user.uid)

        val getListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usr = dataSnapshot.getValue(User::class.java)
                Log.d(TAG, "Got User: ${usr?.email}")

                if (usr == null) return
                SharedPrefsUtils.writeUserToSharedPref(applicationContext, usr)
                progressBar.visibility = View.INVISIBLE
                doRouting(usr.wg_id)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "ErrorCode ${databaseError.code}, DatabaseDetails: ${databaseError.details}")
            }
        }
        getUser.addListenerForSingleValueEvent(getListener)
    }

    private fun writeUserData(user: FirebaseUser) {
        val usr = User(user.email ?: "", user.uid, user.email ?: "")

        // add user to firebase, if successful continue to HomeScreen
        Constants.databaseUsers.child(user.uid).setValue(usr)
                .addOnSuccessListener {
                    Log.d(TAG, "Upload Successful")
                    SharedPrefsUtils.writeUserToSharedPref(applicationContext, usr)
                    doRouting(usr.wg_id)
                }
    }

    /*
        1. no wg id -> WGForm
        2. wg_id same as in SharedPref -> HomeActivity
        3. wg_id different as in SharedPref should not happen -> WGForm
     */
    private fun doRouting(wg_id: String) {
        progressBar.visibility = View.INVISIBLE

        val lastKnownWG = SharedPrefsUtils.readLastWGFromSharedPref(applicationContext)

        when (wg_id) {
            "" -> _moveToWGForm()
            lastKnownWG?.uid -> _moveToWGForm()
            else -> _moveToHomeActivity()
        }
    }

    private fun _moveToHomeActivity() {
        Log.d(TAG, "User has WG -> HomeActivity")
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun _moveToWGForm() {
        Log.d(TAG, "User has no WG -> WGForm")

        val intent = Intent(this, WGFormActivity::class.java)
        startActivity(intent)
        finish()
    }
}
