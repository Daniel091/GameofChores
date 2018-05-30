package sbd.pemgami;

import java.time.DayOfWeek;
import java.util.List;

public class RotateableTaskWG {

    private String name;
    private String creatorName;
    private String description;
    private int timeInMinutes;

    private List<DayOfWeek> rotation;

    public RotateableTaskWG() {
        //default constructor is needed for some reason
    }

    public RotateableTaskWG(String name, String creatorName, String description, int timeInMinutes, List<DayOfWeek> rotation) {
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

    public List<DayOfWeek> getRotation() {
        return rotation;
    }

}
