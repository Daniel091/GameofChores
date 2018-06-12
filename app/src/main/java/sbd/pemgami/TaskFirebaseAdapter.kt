package sbd.pemgami

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.row_layout.view.*
import sbd.pemgami.TasksPlanner.PointsCalculator
import sbd.pemgami.TasksPlanner.Task
import sbd.pemgami.TasksPlanner.TaskViewFragment
import java.text.SimpleDateFormat
import java.util.*


class TaskFirebaseAdapter(frag: TaskViewFragment, usr: User, wg: WG) : RecyclerView.Adapter<TaskFirebaseAdapter.TaskHolder>() {
    private var mListener: BuildEventHandler? = frag
    private val mUsr = usr
    private val tasks = mutableListOf<Task>()


    init {

        val query = Constants.getTasksWGRef(wg.uid)?.orderByChild("user")?.equalTo(mUsr.uid)?.limitToFirst(20)

        val childListener = object : ChildEventListener {
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

    interface BuildEventHandler {
        fun triggerBuildHappened()
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
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

        companion object {
            private val Task_KEY = "TASK"
        }
    }
}