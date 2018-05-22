package sbd.pemgami

// nice import of layout no need for findById anymore
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_start.*
import java.util.*


class StartActivity : AppCompatActivity() {
    private val TAG: String = "StartActivity"
    private val fbAuth = FirebaseAuth.getInstance()
    private val RC_SIGN_IN: Int = 123
    private val fbDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        if (fbAuth.currentUser != null) {
            textView.text = fbAuth.currentUser?.email
            //TODO
        } else {
            loginUser()
        }


        if (fbAuth.currentUser != null) {
            textView.text = fbAuth.currentUser?.email
        }

        // simple trigger for debugging, TODO remove
        button3.setOnClickListener {
            loginUser()
        }

        // set up Logout Button for debugging
        button.setOnClickListener {
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

    private fun loginUser() {
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
            checkUserInDb(fbAuth.currentUser)
        }

    }

    private fun checkUserInDb(user: FirebaseUser?) {
        if (user == null) return

        // check if username exists in database
        val checkUser: Query = fbDatabase.child("users").orderByChild("uid").equalTo(user.uid)

        // set up listener
        val checkListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Log.d(TAG, dataSnapshot.childrenCount.toString())
                if (dataSnapshot.childrenCount > 0) {
                    Log.d(TAG, "User exists")
                    getUserData(user)
                } else {
                    // add user data
                    addUserData(user)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "ErrorCode ${databaseError.code}, DatabaseDetails: ${databaseError.details}")
            }
        }
        checkUser.addListenerForSingleValueEvent(checkListener)
    }

    private fun getUserData(user: FirebaseUser) {
        val getUser: Query = fbDatabase.child("users").child(user.uid)

        val getListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usr = dataSnapshot.getValue(User::class.java)
                Log.d(TAG, "Got User: ${usr?.email}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "ErrorCode ${databaseError.code}, DatabaseDetails: ${databaseError.details}")
            }
        }
        getUser.addListenerForSingleValueEvent(getListener)
    }

    private fun addUserData(user: FirebaseUser) {
        val usr = User(user.email, user.uid, user.email)

        // add user to firebase
        fbDatabase.child("users").child(user.uid).setValue(usr)
                .addOnSuccessListener { Log.d(TAG, "Upload Successful") }
    }

}
