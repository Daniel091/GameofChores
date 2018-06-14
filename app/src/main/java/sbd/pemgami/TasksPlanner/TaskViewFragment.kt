package sbd.pemgami.TasksPlanner

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_task_view_.*
import sbd.pemgami.*

class TaskViewFragment : Fragment(), TaskFirebaseAdapter.BuildEventHandler {
    private val TAG = "TaskFragment"
    private var adapter: TaskFirebaseAdapter? = null

    // list needs to be mutable, and var to maybe make it changeable
    var taskList = mutableListOf<Task>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_task_view_, container, false)
    }

    override fun onStart() {
        super.onStart()

        progressBarRecycler.visibility = View.VISIBLE

        // could load these from HomeActivity, would be nicer than to always read them
        val usr = SharedPrefsUtils.readLastUserFromSharedPref(activity?.applicationContext)
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(activity?.applicationContext)

        // Start TaskCreation Activity
        addTaskBtn.setOnClickListener {
            val intent = Intent(activity?.applicationContext, TaskCreationActivity::class.java)
            startActivity(intent)
        }

        my_recycler_view.layoutManager = LinearLayoutManager(activity?.applicationContext, LinearLayout.VERTICAL, false)

        // not so fancy guard statement
        if (wg == null) return
        if (usr == null) return
        val context = context ?: return

        adapter = TaskFirebaseAdapter(this, usr, wg)
        my_recycler_view.adapter = adapter

        val swipeController = SwipeController(object : SwipeControllerActions {
            override fun onLeftSwipe(position: Int?) {
                val adapter = my_recycler_view.adapter as TaskFirebaseAdapter
                position?.let { adapter.removeAt(position) }
            }

            override fun onRightSwipe(position: Int?) {
                val adapter = my_recycler_view.adapter as TaskFirebaseAdapter
                position?.let { adapter.removeAt(position) }
            }

        }, context)
        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(my_recycler_view)
    }

    override fun onStop() {
        super.onStop()
        adapter?.removeQueryListener()
    }

    // get triggered by TaskFirebaseAdapter, when data arrives
    override fun triggerBuildHappened() {
        progressBarRecycler.visibility = View.INVISIBLE
    }
}
