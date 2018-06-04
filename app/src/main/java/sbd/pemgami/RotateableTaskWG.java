package sbd.pemgami;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class RotateableTaskWG {

    private String name;
    private String creatorName;
    private String description;
    private int timeInMinutes;

    private List<Integer> rotation;

    private String uniqueID = UUID.randomUUID().toString();

    public RotateableTaskWG() {
        //default constructor is needed for snapshot
    }

    public RotateableTaskWG(String name, String creatorName, String description, int timeInMinutes, List<Integer> rotation) {
        this.name = name;
        this.creatorName = creatorName;
        this.description = description;
        this.timeInMinutes = timeInMinutes;
        this.rotation = rotation;
    }

    public String getName() {
        return name;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getDescription() {
        return description;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }

    /**
     * TODO: change to DayOfTheWeek with higher API level.
     * @return 0 for sunday .... 6 for saturday
     */
    public List<Integer> getRotation() {
        return rotation;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public List<TaskWG> createTasksFromRotation(){
        int today = Calendar.getInstance().getTime().getDay();
        int todayDate = Calendar.getInstance().getTime().getDate();
        int totalDaysOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        List<TaskWG> taskWGList = new ArrayList<>();

        for (int i = 0; todayDate <= totalDaysOfMonth; todayDate++) {
            if (today == 7){
                today = 0;
            }

            if(rotation.contains(today)){
                taskWGList.add(new TaskWG(name, creatorName, description, timeInMinutes, todayDate));
            }
        }

        return taskWGList;

    }
}
