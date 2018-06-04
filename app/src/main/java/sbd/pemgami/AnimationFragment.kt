package sbd.pemgami


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.fragment_animation.*


class AnimationFragment : Fragment() {

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

        val usr = SharedPrefsUtils.readLastUserFromSharedPref(activity?.applicationContext)
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(activity?.applicationContext)

        secondTextView.text = resources.getString(R.string.winner_excitement2, wg?.name)
        usrTextView.text = usr?.name

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
