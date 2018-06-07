package sbd.pemgami.TasksPlanner

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_task_creation.*
import sbd.pemgami.Constants
import sbd.pemgami.R
import sbd.pemgami.SharedPrefsUtils
import java.text.DateFormat
import java.util.*


class TaskCreationActivity : AppCompatActivity() {
    private var whoStarts: String? = null
    private var startTime: Date? = null
    private var endTime: Date? = null

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
            if (wg == null) return@setOnClickListener
            createTasks(wg.users, wg.uid)
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
                startTime = c.time
                startDateText.text = dateStr
            } else {
                endTime = c.time
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

    private fun createTasks(users: MutableList<String>, wg_uid: String) {
        if (nameEditText.text.isEmpty() || timeEditText.text.isEmpty() ||
                startTime == null || endTime == null || whoStarts == null) {
            // TODO notify User
            return
        }
        val c = Calendar.getInstance()
        val time = timeEditText.text.toString().toInt()

        //1. Task Factory create Tasks, puts them out as an array
        val taskList = TaskFactory.createTasks(name = nameEditText.text.toString(), user = whoStarts
                ?: "", start_time = startTime ?: c.time, end_time = endTime
                ?: c.time, duration = time, rotatable = checkBox.isChecked, taskTime = task_times.text.toString(), users = users, context = applicationContext)

        // 2. tasks get send to firebase
        val wgReference = Constants.getTasksWGRef(wg_uid)
        wgReference?.setValue(taskList)?.addOnSuccessListener {
            Log.d("UploadTask_Tasks", "Task Upload Successful")
        }

    }
}
