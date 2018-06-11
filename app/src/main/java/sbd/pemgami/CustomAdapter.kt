package sbd.pemgami

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.row_layout.view.*
import sbd.pemgami.TasksPlanner.PointsCalculator
import sbd.pemgami.TasksPlanner.Task


class TaskHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
    private var view: View = v

    init {
        v.setOnClickListener(this)
    }

    fun setTask(task: Task) {
        view.firstLine.text = task.name

        val points = PointsCalculator.calcPoints(task.duration).toString()
        view.secondLine.text = view.resources.getString(R.string.points, points)

    }

    override fun onClick(v: View) {
        Log.d("RecyclerView", "CLICK!")
    }

    companion object {
        private val Task_KEY = "TASK"
    }
}
