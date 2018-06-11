package sbd.pemgami.TasksPlanner

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.android.synthetic.main.fragment_task_view_.*
import sbd.pemgami.Constants
import sbd.pemgami.R
import sbd.pemgami.SharedPrefsUtils
import sbd.pemgami.TaskFirebaseAdapter

class TaskViewFragment : Fragment(), TaskFirebaseAdapter.BuildEventHandler {
    private val TAG = "TaskFragment"
    private var adapter: TaskFirebaseAdapter? = null

    // list needs to be mutable, and var to maybe make it changeable
    var taskList = mutableListOf<Task>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)

        return inflater.inflate(R.layout.fragment_task_view_, container, false)
    }

    override fun onStart() {
        super.onStart()

        progressBarRecycler.visibility = View.VISIBLE

        my_recycler_view.layoutManager = LinearLayoutManager(activity?.applicationContext, LinearLayout.VERTICAL, false)
        // could load these from HomeActivity, would be nicer than to always read them
        val usr = SharedPrefsUtils.readLastUserFromSharedPref(activity?.applicationContext)
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(activity?.applicationContext)

        // Start TaskCreation Activity
        addTaskBtn.setOnClickListener {
            val intent = Intent(activity?.applicationContext, TaskCreationActivity::class.java)
            startActivity(intent)
        }

        // Using firebaseUI
        // https://github.com/firebase/FirebaseUI-Android/tree/master/database#a-note-on-ordering
        if (wg == null) return

        // orders childs by user field, get task only user equal to current user id and shows only 10
        val query = Constants.getTasksWGRef(wg.uid)?.orderByChild("user")?.equalTo(usr?.uid)
                ?: return

        val options = FirebaseRecyclerOptions.Builder<Task>()
                .setQuery(query, Task::class.java)
                .build()
        adapter = TaskFirebaseAdapter(options, this)

        // add adapter
        my_recycler_view.adapter = adapter
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    // get triggered by TaskFirebaseAdapter, when data arrives
    override fun triggerBuildHappened() {
        progressBarRecycler.visibility = View.INVISIBLE
    }
}
