package model.eventCausers.phenomena;

import model.events.Event;
import model.devices.concreteDevices.sensors.TemperatureSensor;
import model.events.EventCauserInterface;
import model.events.EventSource;
import model.events.EventTarget;
import model.house.House;
import model.house.Room;
import simulation.SimulationLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemperatureEntity implements EventCauserInterface, EventTarget, EventSource {

    private Room currentRoom;
    //degrees C
    private List<TemperatureSensor> sensors = new ArrayList<>();

    Logger logger = LogManager.getLogger(TemperatureEntity.class);

    private HashMap<Room, Integer> roomVisitMap = new HashMap<>();
    private House house;
    public TemperatureEntity(House house){
        this.house = house;
        currentRoom =  house.getRandomRoom();
    }

    /**
     * Generates an event based on the current state of the entity
     * @return event that was generated
     */
    @Override
    public Event generateEvent() {
        //pick randomly from methods in this class and superclass

        //60% chance to change temperature
        Event tempChange =  changeTempRandom();
        if (tempChange != null)
            return tempChange;

        // 70% chance to move to a different room

        //move to random room which is not the current room
        //get random room while it is not the current room
        Room roomToMoveTo = currentRoom;

        if(roomToMoveTo == null)
            roomToMoveTo = house.getRandomRoom();


        if(house.getRooms().size() == 1) {
            Event event =new Event("Instability in temperature is moving to "+ roomToMoveTo.getRoomType(),this,new ArrayList<>(List.of(this)));
            SimulationLogger.logEventOrdered(event);
            logger.info("The Temperature may change in " + currentRoom.getName());

            return event;
        }


        while (roomToMoveTo == currentRoom)
            roomToMoveTo = house.getRandomRoom();

        moveToARoom(roomToMoveTo);

        Event event =new Event("Instability in temperature is moving to "+ roomToMoveTo.getRoomType(),this,new ArrayList<>(List.of(this)));
        SimulationLogger.logEventOrdered(event);
        logger.info("The Temperature may change in " + currentRoom.getName());
        return event;
    }



    /**
     * interacts with a random device in the room
     * @return an event explaining what interaction occured
     */
    @Override
    public Event interactWithDeviceInRoom() {
        return changeTempRandom();
    }


    /**
     * Moves the entity to a random room in the house. it also updates the roomVisitMap
     * @param room room to move to
     */
    @Override
    public void moveToARoom(Room room) {
        this.currentRoom = room;
        SimulationLogger.logActivityAndUsage("The Temperature may change in " + this.currentRoom.getName());
        logger.info("The Temperature may change in " + this.currentRoom.getName());

        if (this.roomVisitMap.containsKey(room)) {
            this.roomVisitMap.put(room, this.roomVisitMap.get(room) + 1);
        } else {
            this.roomVisitMap.put(room, 1);
        }

        //this will add a sensor to the list of sensor every heat man has,
        //so when the temperature changes, it will notify all the sensors in every room
        //he has visited
        this.addSensor(room.getTemperatureSensor());
    }


    /**
     * Changes the temperature in the room by a random amount
     * @return an event describing the change
     */
    public Event changeTempRandom(){
        //change between 1 and 5 degrees
        int change = (int) (Math.random() * 5);

        Event event = null;

        //1/3 chance that nothing changes 1/3 it gets colder, 1/3 hotter
        int changeType = (int) (Math.random() * 3);

        if (changeType == 0) {
            //lower temperature by change
            currentRoom.setTemperature(currentRoom.getTemperature() - change);
            notifySensors();
            event = new Event("A cold breeze is felt in the room and the temperature dropped by" + change + " degrees to " + currentRoom.getTemperature() + " degrees C",this,new ArrayList<>(List.of(currentRoom)));
            SimulationLogger.logEventOrdered(event);
            return event;



        }
        else if(changeType == 1){
            currentRoom.setTemperature(currentRoom.getTemperature() + change);
            notifySensors();
            event = new Event("A warm breeze is felt in the room and the temperature rose by" + change + " degrees to " + currentRoom.getTemperature() + " degrees C",this,new ArrayList<>(List.of(currentRoom)));
            SimulationLogger.logEventOrdered(event);
            return event;
        }

        //else nothing happens
        return null;

    }

    public void addSensor(TemperatureSensor sensor) {
        if(sensor == null)
            return;
        if (!this.sensors.contains(sensor)) {
            this.sensors.add(sensor);
        }
    }

    public void notifySensors() {
        for (TemperatureSensor sensor : this.sensors) {
            if (sensor != null) {
                sensor.update(currentRoom.getTemperature());
            }
        }
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public List<TemperatureSensor> getSensors() {
        return sensors;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }



}