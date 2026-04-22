package UnitTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import model.devices.AbstractDevice;
import model.devices.concreteDevices.lights.Lights;
import model.devices.concreteDevices.sensors.TemperatureSensor;
import model.devices.deviceFlyweight.DeviceFlyweightFactory;
import model.eventCausers.phenomena.timeEntity.*;
import model.eventCausers.phenomena.TemperatureEntity;
import model.events.Event;
import model.house.Floor;
import model.house.House;
import model.house.Room;
import model.house.RoomType;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PhenomenaTests {

    private House house;
    private TimeEntitySingleton timeEntitySingleton;
    private TemperatureEntity temperatureEntity;

    @Before
    public void setUp() {
        house = new House("TestHouse", 1);
        house.generateRooms(4);
        timeEntitySingleton = TimeEntitySingleton.getInstance(house);
        temperatureEntity = new TemperatureEntity(house);
    }


    @Test
    public void testTimeEntitySingleton() {
        timeEntitySingleton.nextTimePeriod();
        assertNotNull(timeEntitySingleton.getSubscribedDevices());
        assertEquals(4, timeEntitySingleton.getSubscribedDevices().size());
        assertEquals(LocalTime.of(14, 0), timeEntitySingleton.getCurrentTime().getCurrentLocalTime());
        assertEquals(0, timeEntitySingleton.getCurrentTime().getCurrentDay());

        CurrentTime currentTime = timeEntitySingleton.getCurrentTime();
        AbstractDevice device = new Lights("TestLights", 1, new Room(RoomType.BEDROOM,1,house,new Floor(0,house)));
        timeEntitySingleton.getSubscribedDevices().add(device);

        List<Event> events = timeEntitySingleton.notifyAllTurnedOnSubscribers();
        assertNotNull(events);
        assertEquals(0, events.size());
    }
    @Test
    public void testCurrentTime() {
        CurrentTime currentTime = new CurrentTime();
        assertEquals(LocalTime.NOON, currentTime.getCurrentLocalTime());
        assertEquals(0, currentTime.getCurrentDay());

        currentTime.setCurrentLocalTime(LocalTime.MIDNIGHT);
        assertEquals(LocalTime.MIDNIGHT, currentTime.getCurrentLocalTime());

        currentTime.setCurrentDay(1);
        assertEquals(1, currentTime.getCurrentDay());
    }


    @Test
    public void testChangeTempRandom() {
        TemperatureEntity temperatureEntity = new TemperatureEntity(house);
        Room room = house.getRandomRoom();
        temperatureEntity.setCurrentRoom(room) ;
        room.setTemperature(25); // Set initial temperature

        TemperatureSensor sensor = new TemperatureSensor("TestSensor", 1, room);
        temperatureEntity.addSensor(sensor);

        for (int i = 0; i < 100; i++){
            temperatureEntity.changeTempRandom();

        }
        float newTemperature = room.getTemperature();
        assertTrue("Temperature should have changed", newTemperature != 25);

        temperatureEntity.changeTempRandom(); // Test coverage for the other branch

        // You can add more tests for different scenarios, edge cases, and ensure proper event generation.
    }

    @Test
    public void testMoveToARoom() {
        TemperatureEntity temperatureEntity = new TemperatureEntity(house);
        Room room1 = house.getRandomRoom();
        Room room2 = house.getRandomRoom();
        while (room2 == room1)
            room2 = house.getRandomRoom();

        temperatureEntity.moveToARoom(room1);
        assertEquals(room1, temperatureEntity.getCurrentRoom());

        temperatureEntity.moveToARoom(room2);
        assertEquals(room2, temperatureEntity.getCurrentRoom());

    }

}
