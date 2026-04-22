package model.events;

import model.events.Event;
import model.house.Room;

import java.util.Random;

public interface EventCauserInterface {

    public Event generateEvent();
    public Event  interactWithDeviceInRoom();

    static Random random = new Random();

    public void moveToARoom(Room room);



}
