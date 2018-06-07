package sbd.pemgami.TasksPlanner

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_task_creation.*
import sbd.pemgami.R
import sbd.pemgami.SharedPrefsUtils
import java.text.DateFormat
import java.util.*


class TaskCreationActivity : AppCompatActivity() {
    private var whoStarts: String? = null
    private var startTime: Long? = null
    private var endTime: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_creation)
    }

    override fun onStart() {
        super.onStart()

        val usr = SharedPrefsUtils.readLastUserFromSharedPref(applicationContext)
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(applicationContext)

        // user that creates tasks, starts as default
        whoStarts = usr?.uid
        whoTextView.text = usr?.name

        // default taskTime, is one time
        val res = resources
        val taskTimes = res.getStringArray(R.array.taskTimes)
        task_times.text = taskTimes[0]

        submitButton.setOnClickListener {
            createTask()
        }

        whoTextView.setOnClickListener {
            showListDialog(wg?.users)
        }

        startDateText.setOnClickListener {
            showDatePicker(start = true)
        }

        endDateText.setOnClickListener {
            showDatePicker(end = true)
        }

        task_times.setOnClickListener {
            showListDialogTimes()
        }
    }

    private fun showListDialogTimes() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose WG Member")
        var tmpTaskTime = task_times.text

        val res = resources
        val timesArray = res.getStringArray(R.array.taskTimes)
        val selected = timesArray.indexOf(tmpTaskTime)
        builder.setSingleChoiceItems(timesArray, selected, { _, which ->
            Log.d("TaskTimesDialog", timesArray[which])
            tmpTaskTime = timesArray[which]
        })

        // save choice
        builder.setPositiveButton("Ok", { _, id ->
            task_times.text = tmpTaskTime
        })

        // reset tmp var
        builder.setNegativeButton("Cancel", { _, id ->
            tmpTaskTime = task_times.text
        })

        val dialog = builder.create()
        dialog.show()
    }

    private fun showDatePicker(start: Boolean = false, end: Boolean = false) {
        val c = Calendar.getInstance()
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH)
        val d = c.get(Calendar.DAY_OF_MONTH)

        val dpd2 = DatePickerDialog(this)
        dpd2.updateDate(y, m, d)
        dpd2.setOnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            Log.d("Dialog", "$year $monthOfYear $dayOfMonth")
            c.set(year, monthOfYear, dayOfMonth, 0, 0, 0)

            val fmt = DateFormat.getDateInstance(1, Locale.US)
            val dateStr = fmt.format(c.time)

            if (start) {
                startTime = c.timeInMillis
                startDateText.text = dateStr
            } else {
                endTime = c.timeInMillis
                endDateText.text = dateStr
            }
        }
        dpd2.show()
    }

    // TODO show usr names instead of uids
    private fun showListDialog(users: MutableList<String>?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose WG Member")

        val usersArray = users?.toTypedArray() ?: return
        builder.setItems(usersArray, { _, which ->
            Log.d("Dialog", usersArray[which])
            whoStarts = usersArray[which]
        })
        val dialog = builder.create()
        dialog.show()
    }

    private fun createTask() {

    }
}
