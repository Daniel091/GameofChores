package sbd.pemgami

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
// nice import of layout no need for findById anymore
import kotlinx.android.synthetic.main.activity_start.*
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    var fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        if (fbAuth.currentUser == null) {
            lbl1.text = "Hello to Unknown User!"
        } else {
            lbl1.text = "Hello World to known User!"
        }

    }
}
