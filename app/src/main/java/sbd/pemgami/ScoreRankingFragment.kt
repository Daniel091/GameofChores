package sbd.pemgami

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.score_layout.*
import java.text.SimpleDateFormat
import java.util.*

class ScoreRankingFragment : Fragment(), ScoreFirebaseAdapter.BuildEventHandler {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.score_layout, container, false)
    }

    override fun onStart() {
        super.onStart()
        progressBar4.visibility = View.VISIBLE

        my_recycler_view.layoutManager = LinearLayoutManager(activity?.applicationContext, LinearLayout.VERTICAL, false)

        val usr = SharedPrefsUtils.readLastUserFromSharedPref(activity?.applicationContext)
                ?: return
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(activity?.applicationContext) ?: return

        val adapter = ScoreFirebaseAdapter(this, usr, wg)
        my_recycler_view.adapter = adapter


        setCurrentMonth()
    }

    override fun triggerBuildHappened() {
        progressBar4.visibility = View.INVISIBLE
    }

    @SuppressLint("SimpleDateFormat")
    private fun setCurrentMonth() {
        val format = SimpleDateFormat("MMMM")
        val month = activity?.applicationContext?.resources?.getString(R.string.highscore, format.format(Date()))
        highscoreTextView.text = month
    }


}