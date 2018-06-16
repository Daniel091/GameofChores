package sbd.pemgami.MonthlyWinner


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.fragment_animation.*
import sbd.pemgami.R
import sbd.pemgami.SharedPrefsUtils
import sbd.pemgami.User
import sbd.pemgami.WG

class AnimationDialog : DialogFragment() {
    var winner: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animation, container, false)
    }

    override fun onStart() {
        super.onStart()
        firstTextView.visibility = View.INVISIBLE
        secondTextView.visibility = View.INVISIBLE
        usrTextView.visibility = View.INVISIBLE

        // computer winner of month
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(activity?.applicationContext)
        val cWinner = winner
        cWinner?.let { showWinner(cWinner, wg) }
    }

    private fun showWinner(winner: User, wg: WG?) {
        secondTextView.text = resources.getString(R.string.winner_excitement2, wg?.name)
        usrTextView.text = winner.name

        notifyUser()

        lottieAnimationView.setOnClickListener {
            lottieAnimationView.playAnimation()
        }
    }

    private fun notifyUser() {
        val fadInAnimationShort = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        fadInAnimationShort.duration = 1000

        val fadInAnimationLong = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        fadInAnimationLong.duration = 3000

        val fadInAnimationUsr = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        fadInAnimationLong.duration = 3000

        fadInAnimationShort.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(arg0: Animation) {}
            override fun onAnimationRepeat(arg0: Animation) {}
            override fun onAnimationEnd(arg0: Animation) {
                secondTextView.visibility = View.VISIBLE
                secondTextView.startAnimation(fadInAnimationLong)
            }
        })

        fadInAnimationLong.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(arg0: Animation) {}
            override fun onAnimationRepeat(arg0: Animation) {}
            override fun onAnimationEnd(arg0: Animation) {
                usrTextView.visibility = View.VISIBLE
                usrTextView.startAnimation(fadInAnimationUsr)
                lottieAnimationView.playAnimation()
                lottieFireworks.playAnimation()
            }
        })

        firstTextView.visibility = View.VISIBLE
        firstTextView.startAnimation(fadInAnimationShort)

    }

}
