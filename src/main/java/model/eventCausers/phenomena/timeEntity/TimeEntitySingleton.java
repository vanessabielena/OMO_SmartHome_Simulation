package model.eventCausers.phenomena.timeEntity;

import model.events.Event;
import model.devices.AbstractDevice;
import model.devices.DeviceActivityState;
import model.house.House;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the time entity in the simulation.
 */
public class TimeEntitySingleton  {

    private static TimeEntitySingleton instance;
    ArrayList<Event> triggeredEvents = new ArrayList<>();
    House house;

    int timeIncrement = 2;
    ArrayList<AbstractDevice> subscribedDevices = new ArrayList<>();




    public CurrentTime getCurrentTime() {
        return currentTime;
    }

    CurrentTime currentTime = new CurrentTime();

    Logger logger = LogManager.getLogger(TimeEntitySingleton.class);
    int startingDay= 0;
    int startingMonth = 0;
    int startingYear = 0;
    LocalTime startingTime = LocalTime.NOON;



//    private TimeSensor sensor;

    private TimeEntitySingleton(House house){
        this.house = house;
        this.subscribedDevices.addAll(house.getDevices());
//        this.sensor = new TimeSensor("DefaultTimeSensor", 1, house.getRandomRoom()); // Initialize the sensor
        this.notifyAllTurnedOnSubscribers();
//        Room randomRoom = house.getRandomRoom();
//        if (randomRoom != null) {
//            this.sensor = new TimeSensor("MainTimeSensor", 1, randomRoom); // Initialize the sensor
//            this.notifyAllTurnedOnSubscribers();
//        } else {
            // Handle the case where no valid room is available
            // You might want to log a warning or throw an exception depending on your requirements.
//            SimulationLogger.logEvent("No valid room available for TimeSensor initialization");
//        }
    }

    //static method to get the singleton instance

    /**
     * Returns the singleton instance of the time entity
     * @param house house
     * @return singleton instance of the time entity
     */
    public static TimeEntitySingleton getInstance(House house) {
        if (instance == null) {
            instance = new TimeEntitySingleton(house);
        }
        return instance;
    }

    /**
     * notifies all devices that are turned on to signify a new time change
     */
    public List<Event> notifyAllTurnedOnSubscribers(){
        if(subscribedDevices.isEmpty()){
            return null;
        }

        for (AbstractDevice device : subscribedDevices) {

            if (!device.getActivityState().equals(DeviceActivityState.OFF)) {

                Event event = device.performTimeAction(currentTime.getCurrentLocalTime());
                triggeredEvents.add(event);


            }
        }

        //notify time sensors
//        notifyTimeSensor();

        return triggeredEvents;
    }
    //make a new trigger map for each time increment

    /**
     * Increments the time in the simulation by the time increment
     */
    public void nextTimePeriod(){

        int newTimeHour = currentTime.getCurrentLocalTime().getHour()+timeIncrement;

        while(newTimeHour >= 24){
            currentTime.setCurrentDay(currentTime.getCurrentDay()+1);
            newTimeHour = newTimeHour - 24;
        }
        LocalTime newTime = LocalTime.of(newTimeHour, 0);
        currentTime.setCurrentLocalTime(newTime);
        triggeredEvents.clear();


        //if any new devices are in the house, add them to the list of subscribed devices (watch out for any that might've been deleted)
        List<AbstractDevice> devices = this.house.getDevices();
        for (AbstractDevice device : devices) {
            if (!subscribedDevices.contains(device)) {
                subscribedDevices.add(device);
            }


            if(device.isHasTimeFunction()){
                device.performTimeAction(currentTime.getCurrentLocalTime());
            }
        }

        notifyAllTurnedOnSubscribers();




        degradeAllDevices();

        //        what day and time is it?
        logger.info("New Cycle; \n Current time is: " + currentTime.getCurrentLocalTime().toString() + " on day " + currentTime.getCurrentDay());

    }


    /**
     * degrades all devices in the house
     */
    public void degradeAllDevices() {
        List<AbstractDevice> devices = this.house.getDevices();
        for (AbstractDevice device : devices) {
            device.degradeDevice(5); // degrades a device by 5 percent
            if (device.getDegradation() <= 0) {
                device.getRoom().removeDevice(device);
                logger.info("Device " + device.getName() + " is broken and deleted.");
            }
        }
    }

    public ArrayList<AbstractDevice> getSubscribedDevices() {
        return subscribedDevices;
    }
}
