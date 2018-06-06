package sbd.pemgami.TasksPlanner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import sbd.pemgami.R
import kotlinx.android.synthetic.main.activity_task_creation.*


class TaskCreationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_creation)

        submitButton.setOnClickListener {
            createTask()
        }

    }



    private fun createTask() {

    }
}
