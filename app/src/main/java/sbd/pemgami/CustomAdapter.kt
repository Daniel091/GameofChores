package sbd.pemgami

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_layout.view.*
import sbd.pemgami.TasksPlanner.Task


class CustomAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<CustomAdapter.TaskHolder>() {

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val task = taskList[position]
        holder.setTask(task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val inflatedView = parent.inflate(R.layout.row_layout, false)
        return TaskHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class TaskHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var task: Task? = null

        init {
            v.setOnClickListener(this)
        }

        fun setTask(task: Task) {
            view.secondLine.text = task.name
            view.firstLine.text = task.user
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

        companion object {
            private val Task_KEY = "TASK"
        }
    }
}