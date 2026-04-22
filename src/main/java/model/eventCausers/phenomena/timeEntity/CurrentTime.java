package model.eventCausers.phenomena.timeEntity;

import java.time.LocalTime;

/**
 * This class represents the current time in the simulation.
 */
public class CurrentTime {
    LocalTime currentLocalTime = LocalTime.NOON;
    int currentDay = 0;

    public LocalTime getCurrentLocalTime() {
        return currentLocalTime;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentLocalTime(LocalTime currentLocalTime) {
        this.currentLocalTime = currentLocalTime;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }
}
