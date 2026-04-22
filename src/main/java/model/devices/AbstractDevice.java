package model.devices;

import model.eventCausers.beings.Person;
import model.events.Event;
import model.devices.deviceFlyweight.DeviceFlyweightFactory;
import model.devices.deviceFlyweight.DeviceRoomActivityFlyweight;
import model.events.EventSource;
import model.events.EventTarget;
import model.house.Room;
import simulation.SimulationLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalTime;
import java.util.Random;

public abstract class   AbstractDevice  implements EventSource, EventTarget {
    private String deviceName;
    protected int deviceId;
    private int degradation; //in percentage - 100% fully functional

    //flyweight for this and room

//    private DeviceActivityState activityState;

    DeviceRoomActivityFlyweight flyweight;


    //use static public logger of time simulation
    Logger logger = LogManager.getLogger(AbstractDevice.class);
    //in kWh
    protected float powerConsumption = 0;
    protected float waterConsumption = 0;
    protected float gasConsumption = 0;
    protected float deviceCondition = 50.0f;



    protected boolean hasTimeFunction= false;


    public AbstractDevice(String name, int id, Room room) {
        this.deviceName = name;
        this.deviceId = id;
        this.degradation = 50;
//        this.room = room;
        room.addDevice(this);

//        this.activityState = DeviceActivityState.OFF; // Default to off


        flyweight =  DeviceFlyweightFactory.getDeviceFlyweight(DeviceActivityState.OFF, room);


        SimulationLogger.logHouseConfig("New device - " + name + " was created in " + room);

        this.powerConsumption = 0;
        this.waterConsumption = 0;
        this.gasConsumption = 0;
        this.deviceCondition = 50.0f;

        Random random = new Random();
//
//        if (hasTimeFunction) {
////            room.getHouse().getTimeEntity().getSensor().subscribe(this);
//            room.getHouse().getTimeEntity().addSubscribed(this);
//        }

        logger.info("New device - " + name + " was created in " + room);

    }



    public String getName() {
        return deviceName;
    }


    /**
     * Turns the device on.
     * @param time time
     *             @return true if the device was turned on, false if it was already on
     */
    public boolean turnOn(LocalTime time, Person personTurningOn) {

        DeviceActivityState activityState = flyweight.getActivityState();

        if(activityState == DeviceActivityState.ON){
            logger.info(deviceName + " is already ON.");
            return false;
        }

        flyweight = DeviceFlyweightFactory.getDeviceFlyweight(DeviceActivityState.ON, flyweight.getRoom());

//        activityState = DeviceActivityState.ON;
        logger.info(deviceName + " is now ON.");
        return true;
    }

    /**
     * Turns the device off..
     * @param time time
     * @return true if the device was turned off, false if it was already off
     */
    public boolean turnOff(LocalTime time) {
//        activityState = DeviceActivityState.OFF;
        DeviceActivityState activityState = flyweight.getActivityState();

        if(activityState == DeviceActivityState.OFF){
            logger.info(deviceName + " is already OFF.");
            return false;
        }

        flyweight = DeviceFlyweightFactory.getDeviceFlyweight(DeviceActivityState.OFF, flyweight.getRoom());

        logger.info(deviceName + " is now OFF.");
        return true;
    }


    @Override
    public String toString() {
        return "device called " + deviceName +" with an id " + deviceId + " in room: " + flyweight.getRoom().getName();
    }



    public float getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(float powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    public float calculatePowerConsumption(long durationInSeconds) {
        return 0.0f;
    }

    public float getWaterConsumption() {
        return waterConsumption;
    }

    public void setWaterConsumption(float waterConsumption) {
        this.waterConsumption = waterConsumption;
    }

    public float getGasConsumption() {
        return gasConsumption;
    }

    public void setGasConsumption(float gasConsumption) {
        this.gasConsumption = gasConsumption;
    }

    public float getDeviceCondition() {
        return deviceCondition;
    }

    public void setDeviceCondition(float deviceCondition) {
        this.deviceCondition = deviceCondition;
    }

    public Event performTimeAction(LocalTime currentTime) {
        return null;
    }

    public DeviceActivityState getActivityState() {
        return flyweight.getActivityState();
    }

    public Room getRoom() {
        return flyweight.getRoom();
    }



    public int getDegradation() {
        return this.degradation;
    }

    public void degradeDevice(int percentage) {
        this.degradation -= percentage;
    }

    public void repairDevice() {
        this.degradation = 100;
    }

    public int getDeviceId() {
        return deviceId;
    }


    public boolean isHasTimeFunction() {
        return hasTimeFunction;
    }

    public void setHasTimeFunction(boolean hasTimeFunction) {
        this.hasTimeFunction = hasTimeFunction;
    }

}
