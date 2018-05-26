package sbd.pemgami

// nice import of layout no need for findById anymore
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

        if (fbAuth.currentUser != null) {
            textView.text = fbAuth.currentUser?.email
            val usr = SharedPrefsUtils.readLastUserFromSharedPref(applicationContext)
            if (usr == null || usr.wg_id == "") {
                checkUserInDB(fbAuth.currentUser)
            } else {
                moveToNextActivity(usr.wg_id)
            }
        } else {
            startFirebaseUI()
        }

        textView.text = fbAuth.currentUser?.email

        loginBtn.setOnClickListener {
            startFirebaseUI()
        }

        logoutBtn.setOnClickListener {
            if (fbAuth.currentUser != null) {
                Log.d(TAG, "Signed User Out")
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            // user completly signed out
                            textView.text = ""
                        }
            }
        }
    }

    private fun moveToNextActivity(usr_wg_id: String) {
        if (usr_wg_id != "" && fbAuth.currentUser != null) {
            Log.d(TAG, "User has already a WG -> To Home Screen")

            // TODO comment in
            //val intent = Intent(this, HomeActivity::class.java)
            //startActivity(intent)

            // TODO remove
            val intent = Intent(this, WGFormActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.d(TAG, "User has no WG -> WGForm")

            val intent = Intent(this, WGFormActivity::class.java)
            startActivity(intent)
            finish()
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
            textView.text = fbAuth.currentUser?.email
            checkUserInDB(fbAuth.currentUser)
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
                if (dataSnapshot.childrenCount > 0) {
                    Log.d(TAG, "User exists")
                    getUserData(user)
                } else {
                    // add user data
                    writeUserData(user)
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
                moveToNextActivity(usr.wg_id)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "ErrorCode ${databaseError.code}, DatabaseDetails: ${databaseError.details}")
                progressBar.visibility = View.INVISIBLE
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
                    moveToNextActivity(usr.wg_id)
                }
    }

}
