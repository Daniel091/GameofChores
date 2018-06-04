package sbd.pemgami;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TaskWG {

    private String name;
    private String creatorName;
    private String description;
    private int timeInMinutes;
    private int bonusPoints;

    private int dueDate;

    private boolean taskDone;
    private boolean taskOverdue;
    private String uniqueID = UUID.randomUUID().toString();

    public TaskWG() {
        //default constructor needed for snapshot
    }

    public TaskWG(String name, String creatorName, String description, int timeInMinutes, int dueDateAsDayOfTheMonth) {
        this.name = name;
        this.creatorName = creatorName;
        this.description = description;
        this.timeInMinutes = timeInMinutes;
        this.bonusPoints = setBonusPoints(timeInMinutes);
        this.dueDate = dueDateAsDayOfTheMonth;
        this.taskDone = false;
        this.taskOverdue = false;
        overdueCheckAndSet();
    }

    private int setBonusPoints(int timeInMinutes) {
        if (timeInMinutes > 0) {
            return 1 + timeInMinutes / 10;
        }
        return 0;
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

    public int getBonusPoints() {
        return bonusPoints;
    }

    /**
     * Day of the month 1 - max 31
     * @return dueDate
     */
    public int getDueDate() {
        return dueDate;
    }

    public boolean isTaskDone() {
        return taskDone;
    }

    public boolean isTaskOverdue() {
        overdueCheckAndSet();

        return taskOverdue;
    }

    private void overdueCheckAndSet() {
        int today = Calendar.getInstance().getTime().getDay();

        if (dueDate < today) {
            taskOverdue = true;
        }
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
        if (taskDone) {
            taskOverdue = false;
        }
    }

    public String getUniqueID() {
        return uniqueID;
    }
}
