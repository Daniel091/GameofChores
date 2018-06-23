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
import java.text.SimpleDateFormat
import java.util.*


class TasksDoneFirebaseAdapter(frag: HomeFragment, usr: User, wg: WG) : RecyclerView.Adapter<TasksDoneFirebaseAdapter.DoneTaskHolder>() {

    var mListener: BuildEventHandler? = frag
    private val mUsr = usr
    private val mWg = wg
    private val tasks = mutableListOf<DoneTask>()
    private val context = frag.context

    init {
        // get last 10 done tasks
        val query = Constants.getPastTasksWGRef(wg.uid)?.orderByChild("doneAt")?.limitToFirst(6)

        val childListener = object : ChildEventListener {
            override fun onCancelled(snapshot: DatabaseError?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot?, preKey: String?) {
                // we do not plan to move childs
            }

            override fun onChildChanged(snapshot: DataSnapshot?, preKey: String?) {
                val index = tasks.indexOfFirst { it.uid == preKey }
                if (index != -1) {
                    val task = snapshot?.getValue(DoneTask::class.java)
                    task?.let {
                        tasks[index] = task
                        notifyItemChanged(index)
                    }
                }
            }

            // sorts added users by points
            override fun onChildAdded(snapshot: DataSnapshot?, preKey: String?) {
                val task = snapshot?.getValue(DoneTask::class.java)

                val index = if (tasks.count() != 0) tasks.count() - 1 else 0
                task?.let {
                    tasks.add(index, task)
                    tasks.sortBy { it.time }
                    notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot?) {
                val task = snapshot?.getValue(DoneTask::class.java)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoneTaskHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_layout, parent, false)

        // trigger data is there, because layout inflation happens
        mListener?.triggerBuildHappened()

        return DoneTaskHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.count()
    }

    override fun onBindViewHolder(holder: DoneTaskHolder, position: Int) {
        holder.setTask(tasks[position])
    }

    interface BuildEventHandler {
        fun triggerBuildHappened()
    }


    class DoneTaskHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private val fmt2 = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        init {
            v.setOnClickListener(this)
        }

        fun setTask(task: DoneTask) {
            val points = PointsCalculator.calcPoints(task.duration).toString()
            view.secondLine.text = view.resources.getString(R.string.points_for, points, task.doneBy)

            val taskDate = Date(task.doneAt)
            val dateStr = fmt2.format(taskDate)

            view.firstLine.text = task.name + " - " + dateStr

            val imageList = HashMap<String, Int>()
            imageList.put("[tT]oilet|[bB]athroom|([Ww][Cc])", R.drawable.toilet)
            imageList.put("[cC]lean.*(?![tT]oilet|[bB]athroom|[Ww][Cc])", R.drawable.clean)
            imageList.put("[dD]ishes| ([wW]ash up)| ([dD]ish wash)", R.drawable.dishes)
            imageList.put("[cC]all", R.drawable.call2)
            imageList.put("[gG]rocer[y|(ies)]|[sS]hopping|[sS]upermarket|[bB]uy", R.drawable.shopping)
            imageList.put("[rR]epair|[fF]ix", R.drawable.repair)
            imageList.put("[wW]aste|[tT]rash", R.drawable.waste)
            imageList.put("[lL]aundry|[cC]lothes", R.drawable.laundry)
            imageList.put("[Hh]oover|[vV]acuum clean", R.drawable.hoover)
            imageList.put("[pP]lant", R.drawable.water_plants)


            for ((key, value) in imageList) {
                val regex = Regex(pattern = key)
                val matched = regex.containsMatchIn(input = task.name)
                if (matched) {
                    view.imageview_task_icon.setImageResource(value)
                    return
                } else {
                    view.imageview_task_icon.setImageResource(R.drawable.ic_task_white)
                }
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