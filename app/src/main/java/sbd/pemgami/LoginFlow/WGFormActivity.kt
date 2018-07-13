package sbd.pemgami.LoginFlow

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import sbd.pemgami.HomeActivity
import sbd.pemgami.R
import sbd.pemgami.SharedPrefsUtils


class WGFormActivity : AppCompatActivity(), FindWGFragment.CardButtonListener {

    private val TAG = "WGFormAcitivty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wgform)

        // check for wg again, because user may created wg and then exited the app
        val user = SharedPrefsUtils.readLastUserFromSharedPref(applicationContext)
        if (user?.wg_id != "") {
            Log.d(TAG, "User has WG -> HomeActivity")
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragments_cont, FindWGFragment.newInstance(), "FrontSide")
                    .commit()
        }
    }

    override fun triggerCardFlip() {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.fragments_cont, CreateWGFragment.newInstance())
                .addToBackStack("BackSide")
                .commit()
    }
}

