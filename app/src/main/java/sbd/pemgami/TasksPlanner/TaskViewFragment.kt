package sbd.pemgami.TasksPlanner

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import sbd.pemgami.R
import sbd.pemgami.SharedPrefsUtils
import kotlinx.android.synthetic.main.fragment_task_view_.*
import kotlinx.android.synthetic.main.row_layout.*
import sbd.pemgami.CustomAdapter
import sbd.pemgami.R.string.task

class TaskViewFragment : Fragment() {

    private val TAG = "TaskActivity"
    private val REQUIRED = "Required"
    private var user: FirebaseUser? = null
    private var mDatabase: DatabaseReference? = null
    private var mTaskReference: DatabaseReference? = null
    private var mTaskListener: ChildEventListener? = null

    val taskList = ArrayList<Task>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)

        // TODO: Find out why setContentView and findViewById don't work
        setContentView(R.layout.fragment_task_view_)

        val rv = findViewById<RecyclerView>(R.id.my_recycler_view)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val tasks = ArrayList<Task>()
        tasks.add(Task("wash dishes", "Paul"))
        tasks.add(Task("grocery shopping", "Jane"))

        var adapter = CustomAdapter(tasks)
        rv.adapter = adapter
        return inflater.inflate(R.layout.fragment_task_view_, container, false)
    }

    override fun onStart() {
        super.onStart()

        // could load these from HomeActivity, would be nicer than to always read them
        val usr = SharedPrefsUtils.readLastUserFromSharedPref(activity?.applicationContext)
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(activity?.applicationContext)

        // Start TaskCreation Activity
        addTaskBtn.setOnClickListener {
            val intent = Intent(activity?.applicationContext, TaskCreationActivity::class.java)
            startActivity(intent)
        }

    }


    // First try to catch data from Firebase
    // TODO: fix error "object is not abstract..."
    /*
    private fun firebaseListenerInit() {
        val childEventListener = object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                val task = dataSnapshot!!.getValue(Task::class.java)
                taskList.add(task!!)

                Log.e(TAG, "onChildAdded:" + task.name)

                val latest = taskList[taskList.size - 1]

                firstLine.text = latest.name
                secondLine.text = latest.user
            }
            /*
            override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                Log.e(TAG, "onChildChanged:" + dataSnapshot!!.key)
                val task = dataSnapshot.getValue(Task::class.java)
                Toast.makeText(this, "onChildChanged: " + task!!.name, Toast.LENGTH_SHORT).show()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                Log.e(TAG, "onChildRemoved:" + dataSnapshot!!.key)
                val message = dataSnapshot.getValue(Task::class.java)
                Toast.makeText(this, "onChildRemoved: " + task!!.name, Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                Log.e(TAG, "onChildMoved:" + dataSnapshot!!.key)
                val message = dataSnapshot.getValue(Task::class.java)
                Toast.makeText(this, "onChildMoved: " + task!!., Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError?) {
                Log.e(TAG, "postMessages:onCancelled", databaseError!!.toException())
                Toast.makeText(this, "Failed to load task.", Toast.LENGTH_SHORT).show()
            }
        }*/


    }
    }*/
}
