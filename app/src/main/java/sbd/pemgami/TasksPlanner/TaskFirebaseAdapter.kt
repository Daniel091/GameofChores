package sbd.pemgami.TasksPlanner

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.row_layout.view.*
import sbd.pemgami.*
import java.text.SimpleDateFormat
import java.util.*


class TaskFirebaseAdapter(frag: TaskViewFragment, usr: User, wg: WG, context: Context) : RecyclerView.Adapter<TaskFirebaseAdapter.TaskHolder>() {
    private var mListener: BuildEventHandler? = frag
    private val mUsr = usr
    private val mWg = wg
    private val tasks = mutableListOf<Task>()
    private var childListener: ChildEventListener? = null
    private var query: Query? = null
    private val context = context

    init {
        query = Constants.getTasksWGRef(wg.uid)?.orderByChild("user")?.equalTo(mUsr.uid)?.limitToFirst(20)

        childListener = object : ChildEventListener {
            override fun onCancelled(snapshot: DatabaseError?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot?, preKey: String?) {
                // we do not plan to move childs
            }

            override fun onChildChanged(snapshot: DataSnapshot?, preKey: String?) {
                val index = tasks.indexOfFirst { it.uid == preKey }
                if (index != -1) {
                    val task = snapshot?.getValue(Task::class.java)
                    task?.let {
                        tasks[index] = task
                        notifyItemChanged(index)
                    }
                }
            }

            override fun onChildAdded(snapshot: DataSnapshot?, preKey: String?) {
                val task = snapshot?.getValue(Task::class.java)

                val index = if (tasks.count() != 0) tasks.count() - 1 else 0
                task?.let {
                    tasks.add(index, task)
                    tasks.sortBy { it.time }
                    notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot?) {
                val task = snapshot?.getValue(Task::class.java)
                task?.let {
                    val index = tasks.indexOfFirst { it.uid == task.uid }
                    if (index != -1) {
                        tasks.remove(task)
                        notifyItemRemoved(index)
                    }
                }
            }

        }
        query?.addChildEventListener(childListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_layout, parent, false)

        // trigger data is there, because layout inflation happens
        mListener?.triggerBuildHappened()

        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.setTask(tasks[position])
    }

    override fun getItemCount(): Int {
        return tasks.count()
    }

    fun removeQueryListener() {
        query?.removeEventListener(childListener)
    }

    // Remove item after swiping,
    // taskDone means: User did that Task, false if he just deleted Task
    fun removeAt(position: Int, taskDone: Boolean) {
        val task = tasks.removeAt(position)
        notifyItemRemoved(position)

        // update users points
        if (taskDone) {
            mUsr.points = mUsr.points + PointsCalculator.calcPoints(task.duration)
            Constants.databaseUsers.child(mUsr.uid)?.child("points")?.setValue(mUsr.points)?.addOnSuccessListener {
                SharedPrefsUtils.writeUserToSharedPref(context, mUsr)

                // maybe show fancy dialog
                mListener?.triggerShowPointsDialog(PointsCalculator.calcPoints(task.duration))
            }
        }

        // remove Item from firebase, on success add to firebase wg_done_tasks
        Constants.getTasksWGRef(mWg.uid)?.child(task.uid)?.removeValue()?.addOnSuccessListener {
            addToDoneTasks(task, taskDone)
        }

    }

    private fun addToDoneTasks(task: Task, taskDone: Boolean) {
        val queryAdd = Constants.getPastTasksWGRef(mWg.uid)?.child(task.uid)

        // duration 0 means: 0 points
        if (!taskDone) {
            val notDoneTask = task.copy(duration = 0)
            queryAdd?.setValue(notDoneTask)
        } else {
            queryAdd?.setValue(task)
        }
    }

    interface BuildEventHandler {
        fun triggerBuildHappened()
        fun triggerShowPointsDialog(points: Int)
    }

    class TaskHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private val fmt2 = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        init {
            v.setOnClickListener(this)
        }

        fun setTask(task: Task) {
            val points = PointsCalculator.calcPoints(task.duration).toString()
            view.secondLine.text = view.resources.getString(R.string.points, points)

            val taskDate = Date(task.time)
            val dateStr = fmt2.format(taskDate)

            view.firstLine.text = task.name + " - " + dateStr

            // set image according to activity
            val regex = Regex(pattern = "(toilet)")
            val matched = regex.containsMatchIn(input = task.name)
            if (matched) {
                view.imageview_task_icon.setImageResource(R.drawable.toilet)

                // for debugging purposes
                view.firstLine.text = "toilet toilet toilet"
            }
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

        companion object {
            private val Task_KEY = "TASK"
        }
    }
}