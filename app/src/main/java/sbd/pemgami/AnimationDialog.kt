package sbd.pemgami


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_animation.*


class AnimationDialog : DialogFragment() {

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
        progressBar5.visibility = View.VISIBLE

        // computer winner of month
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(activity?.applicationContext)
        val query = Constants.databaseUsers.orderByChild("wg_id").equalTo(wg?.uid)
        val mListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                if (snapshot == null) return
                progressBar5.visibility = View.GONE
                val users = mutableListOf<User>()
                for (child in snapshot.children) {
                    val map = child.value as HashMap<*, *>
                    val points = map["points"] as Long
                    val usr = User(map["name"] as String, map["uid"] as String, map["email"] as String, map["wg_id"] as String, points.toInt() )
                    users.add(usr)
                }
                users.sortByDescending { it.points }
                if (users.count() > 0) {
                    showWinner(users.first(), wg)
                }
            }
        }
        query.addListenerForSingleValueEvent(mListener)
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
