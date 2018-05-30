package sbd.pemgami;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TaskManagerWG {

    private List<TaskWG> tasks;
    private List<RotateableTaskWG> rotateableTasks;
    private DatabaseReference database;
    private String wgId;
    private Gson gson;

    public TaskManagerWG(Context context) {
        this.gson = new Gson();
        this.database = FirebaseDatabase.getInstance().getReference();
        this.wgId = SharedPrefsUtils.INSTANCE.readLastWGFromSharedPref(context).getUid();
        this.tasks = new ArrayList<>();
        this.rotateableTasks = new ArrayList<>();
        fillTasks();

    }

    private void fillTasks() {
        //TODO: debug this bitch when interface ready
        TaskWG[] taskArray = gson.fromJson(String.valueOf(database.child("tasks").child(wgId)), TaskWG[].class);
        RotateableTaskWG[] rotateableTaskArray = gson.fromJson(String.valueOf(database.child("rotationtasks").child(wgId)), RotateableTaskWG[].class);

        tasks.addAll(Arrays.asList(taskArray));
        rotateableTasks.addAll(Arrays.asList(rotateableTaskArray));

    }

    public List<TaskWG> getTasks() {
        return tasks;
    }

    public void createNewTask(String name, String creatorName, String description, int timeInMinutes, Date dueDate) {
        TaskWG taskWG = new TaskWG(name, creatorName, description, timeInMinutes, dueDate);
        tasks.add(taskWG);
        updateTasksOnFirebase();
    }

    public void updateTasksOnFirebase() {
        //TODO: update tasks to firebase here
        database.child("tasks").child(wgId).setValue(tasks);
        database.child("rotationtasks").child(wgId).setValue(rotateableTasks);

    }

    /**
     * Call at the end of the month
     */
    public void deleteTasks() {
        tasks.clear();
        updateTasksOnFirebase();
    }
}
