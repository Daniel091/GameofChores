package sbd.pemgami

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import sbd.pemgami.TasksPlanner.Task

class CustomAdapter(val taskList: ArrayList<Task>): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.txtName?.text = taskList[position].name
        holder?.txtTitle?.text = taskList[position].user

    }

    fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.row_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.secondLine)
        val txtTitle = itemView.findViewById<TextView>(R.id.firstLine)

    }

}