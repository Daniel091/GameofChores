package sbd.pemgami

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_wgform.*


class WGFormActivity : AppCompatActivity(), FindWGFragment.CardButtonListener{

    private val TAG = "WGFormAcitivty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wgform)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragments_cont,FindWGFragment.newInstance(),"FrontSide")
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

