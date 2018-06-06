package sbd.pemgami.TasksPlanner


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sbd.pemgami.R
import sbd.pemgami.SharedPrefsUtils
import kotlinx.android.synthetic.main.fragment_task_view_.*

class TaskViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_view_, container, false)
    }

    override fun onStart() {
        super.onStart()

        // TODO could load these from HomeActivity, would be nicer than to always read them
        val usr = SharedPrefsUtils.readLastUserFromSharedPref(activity?.applicationContext)
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(activity?.applicationContext)

        // Start Add Activity
        addTaskBtn.setOnClickListener {

        }

    }

}
