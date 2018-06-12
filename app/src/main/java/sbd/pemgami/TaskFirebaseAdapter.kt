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
import java.text.DateFormat
import java.util.*


class TaskFirebaseAdapter(frag: TaskViewFragment, usr: User, wg: WG) : RecyclerView.Adapter<TaskFirebaseAdapter.TaskHolder>() {
    private var mListener: BuildEventHandler? = frag
    private val mUsr = usr
    private val tasks = mutableListOf<Task>()


    init {

        /*
         query gets last 20 tasks of wg_id
         filtering to custiom usr.uid is done in child added
        */
        val query = Constants.getTasksWGRef(wg.uid)?.orderByChild("time")?.limitToFirst(20)

        val childListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                // TODO add UID field to Task Class
                // makes it easier to follow this guide https://github.com/hyperoslo/firebase-recipes-demo-android/blob/master/app/src/main/kotlin/no/hyper/demos/recipes/ui/RecipesAdapter.kt
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val task = p0?.getValue(Task::class.java)

                // do not add if uid is different
                if (task?.user != mUsr.uid) return

                val index = if (tasks.count() != 0) tasks.count() - 1 else 0
                task.let {
                    tasks.add(index, task)
                    notifyItemInserted(index)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {

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
        private val fmt = DateFormat.getDateInstance(1, Locale.US)


        init {
            v.setOnClickListener(this)
        }

        // TODO change display of dateStr
        fun setTask(task: Task) {
            val points = PointsCalculator.calcPoints(task.duration).toString()
            view.secondLine.text = view.resources.getString(R.string.points, points)

            val taskDate = Date(task.time)
            val dateStr = fmt.format(taskDate)

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