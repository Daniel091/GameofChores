package sbd.pemgami

// nice import of layout no need for findById anymore
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_start.*
import java.util.*


class StartActivity : AppCompatActivity() {
    private val TAG: String = "StartActivity"
    private val fbAuth = FirebaseAuth.getInstance()
    private val RC_SIGN_IN: Int = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        if (fbAuth.currentUser != null) {
            // user is there proceed to next
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "ResultCode $resultCode, requestCode: $requestCode")

        if (resultCode == Activity.RESULT_OK && requestCode == RC_SIGN_IN) {
            // TODO Proceed to next activity
            textView.text = fbAuth.currentUser?.email
        }
    }

}
