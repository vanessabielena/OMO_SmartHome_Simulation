package UnitTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import model.devices.concreteDevices.car.Car;
import model.devices.concreteDevices.car.CarParkedState;
import model.devices.concreteDevices.car.CarState;
import model.devices.deviceFlyweight.DeviceFlyweightFactory;
import model.eventCausers.beings.Person;
import model.events.Event;
import model.house.Floor;
import model.house.House;
import model.house.Room;
import model.house.RoomType;
import org.junit.Before;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.time.LocalTime;

public class CarTest {

    private Car car;

    @Before
    public void setUp() {
        House house = new House("adresa",1);
        car = new Car("TestCar", 1, new Room(RoomType.GARAGE,1 ,house,new Floor(0,house)));
        car.setState(new CarParkedState());
    }

    @Test
    public void testTurnOn_ValidInput_ReturnsTrue() {
        Person person = mock(Person.class);
        when(person.getFirstName()).thenReturn("John");

        assertTrue(car.turnOn(LocalTime.now(), person));
        assertTrue(car.isOutDriving());
    }

    @Test
    public void testTurnOff_DoesNothing() {
        Person person = mock(Person.class);
        when(person.getFirstName()).thenReturn("John");
        car.turnOnAndGoOutDriving(person);

        assertFalse(car.turnOff(LocalTime.now()));
    }

    @Test
    public void testTurnOnAndGoOutDriving_CarParked_ReturnsTrue() {
        Person person = mock(Person.class);
        when(person.getFirstName()).thenReturn("John");

        assertNotNull(car.turnOn(LocalTime.NOON,person));
        assertTrue(car.isOutDriving());
    }

}

