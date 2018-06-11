package sbd.pemgami

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.android.synthetic.main.row_layout.view.*
import sbd.pemgami.TasksPlanner.PointsCalculator
import sbd.pemgami.TasksPlanner.Task
import sbd.pemgami.TasksPlanner.TaskViewFragment
import java.text.DateFormat
import java.util.*


class TaskFirebaseAdapter(options: FirebaseRecyclerOptions<Task>, frag: TaskViewFragment) : FirebaseRecyclerAdapter<Task, TaskFirebaseAdapter.TaskHolder>(options) {

    var mListener: BuildEventHandler? = frag

    interface BuildEventHandler {
        fun triggerBuildHappened()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_layout, parent, false)

        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int, model: Task) {
        holder.setTask(model)
    }

    override fun onDataChanged() {
        super.onDataChanged()

        mListener?.triggerBuildHappened()
    }


    class TaskHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private val fmt = DateFormat.getDateInstance(1, Locale.US)


        init {
            v.setOnClickListener(this)
        }

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