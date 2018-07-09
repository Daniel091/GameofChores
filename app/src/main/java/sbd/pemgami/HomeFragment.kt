package sbd.pemgami


import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(), TasksDoneFirebaseAdapter.BuildEventHandler {

    private val timeHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            progressBar5.visibility = View.INVISIBLE
            noDataLabel.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()

        activity?.title = "Home"
        val usr = SharedPrefsUtils.readLastUserFromSharedPref(activity?.applicationContext)
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(activity?.applicationContext)
        debugLabel.text = "Username: ${usr?.name}, WG: ${wg?.name}"

        val img = BitmapFactory.decodeResource(resources, R.drawable.welcome_image)
        val round = RoundedBitmapDrawableFactory.create(resources, img)
        round.isCircular = true
        welcomeImageView.setImageDrawable(round)

        usr?.let {
            wg?.let {
                progressBar5.visibility = View.VISIBLE
                doneTasksRecView.layoutManager = LinearLayoutManager(activity?.applicationContext, LinearLayout.VERTICAL, false)
                val adapter = TasksDoneFirebaseAdapter(this, usr, wg)
                doneTasksRecView.adapter = adapter
                setUpTimerTask()
            }
        }
    }

    // when doneTasks Recycler View gets filled event is being triggered
    override fun triggerBuildHappened() {
        progressBar5.visibility = View.INVISIBLE
        noDataLabel.visibility = View.INVISIBLE
    }

    private fun setUpTimerTask() {
        val runnable = Runnable {
            if (progressBar5 != null && progressBar5.visibility == View.VISIBLE) {
                timeHandler.sendEmptyMessage(1)
            }
        }
        timeHandler.postDelayed(runnable, 7000)
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }

        fun getTagName(): String = "HomeFragment"
    }
}
