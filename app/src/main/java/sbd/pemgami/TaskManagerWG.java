package sbd.pemgami;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskManagerWG {

    private static final String TAG = "TaskManagerWG";

    private List<TaskWG> tasks;
    private List<RotateableTaskWG> rotateableTasks;
    private DatabaseReference database;
    private String wgId;

    public TaskManagerWG(Context context) {
        this.database = FirebaseDatabase.getInstance().getReference();
        this.wgId = SharedPrefsUtils.INSTANCE.readLastWGFromSharedPref(context).getUid();
        this.tasks = new ArrayList<>();
        this.rotateableTasks = new ArrayList<>();
        fillTasks();
    }

    private void fillTasks() {
        //TODO: debug this bitch when interface ready. Also not sure if dataSnapshot gives single element or whole list. Change if needed.
        database.child("tasks").child(wgId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TaskWG firebaseTask = dataSnapshot.getValue(TaskWG.class);

                if (!tasks.contains(firebaseTask)) {
                    tasks.add(firebaseTask);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                TaskWG firebaseTask = dataSnapshot.getValue(TaskWG.class);

                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getUniqueID().equals(firebaseTask.getUniqueID())) {
                        tasks.set(i, firebaseTask);
                    }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                TaskWG firebaseTask = dataSnapshot.getValue(TaskWG.class);

                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getUniqueID().equals(firebaseTask.getUniqueID())) {
                        tasks.remove(i);
                    }
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //TODO: should the user be able to order the tasks in future? if so. Implement in here.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError);
            }
        });

        database.child("rotationtasks").child(wgId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RotateableTaskWG firebaseTask = dataSnapshot.getValue(RotateableTaskWG.class);

                if (!rotateableTasks.contains(firebaseTask)) {
                    rotateableTasks.add(firebaseTask);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                RotateableTaskWG firebaseTask = dataSnapshot.getValue(RotateableTaskWG.class);

                for (int i = 0; i < rotateableTasks.size(); i++) {
                    if (rotateableTasks.get(i).getUniqueID().equals(firebaseTask.getUniqueID())) {
                        rotateableTasks.set(i, firebaseTask);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                RotateableTaskWG firebaseTask = dataSnapshot.getValue(RotateableTaskWG.class);

                for (int i = 0; i < rotateableTasks.size(); i++) {
                    if (rotateableTasks.get(i).getUniqueID().equals(firebaseTask.getUniqueID())) {
                        rotateableTasks.remove(i);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError);
            }
        });

    }

    public List<TaskWG> getTasks() {
        return tasks;
    }

    public void createNewTask(String name, String creatorName, String description, int timeInMinutes, Date dueDate) {

        TaskWG taskWG = new TaskWG(name, creatorName, description, timeInMinutes, dueDate.getDate());
        tasks.add(taskWG);
        updateTasksOnFirebase();
    }

    public void createNewRotatableTask(String name, String creatorName, String description, int timeInMinutes, List<Integer> rotation){
        RotateableTaskWG rotateableTaskWG = new RotateableTaskWG(name, creatorName, description, timeInMinutes, rotation);
        tasks.addAll(rotateableTaskWG.createTasksFromRotation());
        rotateableTasks.add(rotateableTaskWG);
        updateTasksOnFirebase();

    }

    public void updateTasksOnFirebase() {
        database.child("tasks").child(wgId).setValue(tasks);
        database.child("rotationtasks").child(wgId).setValue(rotateableTasks);

    }

    /**
     * Call at the first day of each month
     */
    public void seasonIsOverNewSeasonStarts() {
        tasks.clear();
        for (RotateableTaskWG rotaty : rotateableTasks
             ) {
            tasks.addAll(rotaty.createTasksFromRotation());
        }

        updateTasksOnFirebase();
    }
}
