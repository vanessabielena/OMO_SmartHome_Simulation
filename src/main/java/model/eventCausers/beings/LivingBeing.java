package model.eventCausers.beings;

//import model.events.EventCauseType;
import model.events.EventCauserInterface;
import model.devices.AbstractDevice;
import model.events.EventSource;
import model.events.EventTarget;
import model.house.House;
import model.house.Room;
import simulation.SimulationLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class represents a living being in the house. It can be a person or a pet.
 */
public abstract class LivingBeing implements EventCauserInterface, EventTarget, EventSource{

    LocalDate birthdate;
    String firstName;
    Room currentRoom = null;

    boolean isIndisposed = false;
    Logger logger = LogManager.getLogger(LivingBeing.class);

    House house;
    Random random = new Random();

    //how many times has this living being been in each room
    Map<Room, Integer> roomVisitMap = new HashMap<>();
    //how many times has this living being used each device
    Map<AbstractDevice, Integer> deviceUseMap = new HashMap<>();



    public LivingBeing(LocalDate birthdate, String firstName, House house) {
        this.birthdate = birthdate;
        this.firstName = firstName;
        this.house = house;
        this.currentRoom = house.getRandomRoom();

        SimulationLogger.logHouseConfig("Person named "+ firstName+ " was created.");
        SimulationLogger.logActivityAndUsage(firstName + " is currently in the " + currentRoom.getRoomType());

        logger.info("Person named "+ firstName+ " was created.");

    }

    /**
     * Moves the living being to a random room in the house. it also updates the roomVisitMap
     * @param room room to move to
     */
    public void moveToARoom(Room room) {

        this.currentRoom = room;

        SimulationLogger.logActivityAndUsage(this.firstName + " moved to " + this.currentRoom.getName());
        logger.info(this.firstName + " moved to " + this.currentRoom.getName());

        if (this.roomVisitMap.containsKey(room)) {
            this.roomVisitMap.put(room, this.roomVisitMap.get(room) + 1);
        } else {
            this.roomVisitMap.put(room, 1);
        }
    }

    @Override
    public String toString() {
        return "\n\n" +
                getClass().getSimpleName() + "{" + "\n"+
                "   firstName= " + firstName + ",\n"+
                "   birthdate= " + birthdate +",\n"+
                "   currentRoom= " + currentRoom +"\n" +
                "}" ;
    }




    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }


    public Map<Room, Integer> getRoomVisitMap() {
        return roomVisitMap;
    }

    public void setRoomVisitMap(Map<Room, Integer> roomVisitMap) {
        this.roomVisitMap = roomVisitMap;
    }

    public void addRoomVisit(Room room){
        if (this.roomVisitMap.containsKey(room)) {
            this.roomVisitMap.put(room, this.roomVisitMap.get(room) + 1);
        } else {
            this.roomVisitMap.put(room, 1);
        }
    }

    public Map<AbstractDevice, Integer> getDeviceUseMap() {
        return deviceUseMap;
    }

    public void setDeviceUseMap(Map<AbstractDevice, Integer> deviceUseMap) {
        this.deviceUseMap = deviceUseMap;
    }

    public boolean isIndisposed() {
        return isIndisposed;
    }

    public void setIndisposed(boolean indisposed) {
        isIndisposed = indisposed;
    }
}
