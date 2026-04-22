package UnitTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import model.devices.concreteDevices.dishwasher.*;
import model.devices.deviceFlyweight.DeviceFlyweightFactory;
import model.eventCausers.beings.Person;
import model.events.Event;
import model.house.Floor;
import model.house.House;
import model.house.Room;
import model.house.RoomType;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

public class DishWasherUnitTest {

    private Dishwasher dishwasher;

    @Before
    public void setUp() {
        House house = new House("adresa",1);
        dishwasher = new Dishwasher("TestDishwasher", 1, new Room( RoomType.KITCHEN,1,house,new Floor(0,house)));
        dishwasher.setState(new DishwasherOffState());
    }

    @Test
    public void testTurnOn_DishwasherOff_ReturnsTrue() {
        assertTrue(dishwasher.turnOn(LocalTime.now(), mock(Person.class)));
        assertTrue(dishwasher.getState() instanceof DishwasherActiveState);
    }

    @Test
    public void testTurnOn_DishwasherActive_ReturnsFalse() {
        dishwasher.setState(new DishwasherActiveState());
        assertFalse(dishwasher.turnOn(LocalTime.now(), mock(Person.class)));
    }

    @Test
    public void testTurnOff_DishwasherActive_ReturnsFalse() {
        dishwasher.setState(new DishwasherActiveState());
        assertFalse(dishwasher.turnOff(LocalTime.now()));
    }

    @Test
    public void testTurnOff_DishwasherIdle_ReturnsTrue() {
        dishwasher.setState(new DishwasherIdleState());
        assertTrue(dishwasher.turnOff(LocalTime.now()));
        assertTrue(dishwasher.getState() instanceof DishwasherIdleState);
    }

    @Test
    public void testPerformTimeAction_DishwasherActive_ReturnsNotNullEvent() {
        dishwasher.setState(new DishwasherActiveState());
        assertNotNull(dishwasher.performTimeAction(LocalTime.now()));
        assertTrue(dishwasher.getState() instanceof DishwasherIdleState);
    }


    @Test
    public void testPerformTimeAction_DishwasherIdle_ReturnsNullEvent() {
        dishwasher.setState(new DishwasherIdleState());
        assertNull(dishwasher.performTimeAction(LocalTime.now()));
    }

    @Test
    public void testCalculatePowerConsumption_ValidDuration_ReturnsCorrectPowerConsumption() {
        long durationInSeconds = 3600; // 1 hour
        float powerConsumption = dishwasher.calculatePowerConsumption(durationInSeconds);

        assertEquals(0.001f * durationInSeconds, powerConsumption, 0.001);
    }

    @Test
    public void testAddDishes_NoDishes_ReturnsTrue() {
        assertTrue(dishwasher.addDishes(mock(Person.class)));
        assertTrue(dishwasher.isHasDishes());
    }

    @Test
    public void testAddDishes_AlreadyHasDishes_ReturnsFalse() {
        dishwasher.addDishes(mock(Person.class));
        assertFalse(dishwasher.addDishes(mock(Person.class)));
    }

    @Test
    public void testTakeDishes_HasDishes_ReturnsTrue() {
        dishwasher.addDishes(mock(Person.class));
        assertTrue(dishwasher.takeDishes(mock(Person.class)));
        assertFalse(dishwasher.isHasDishes());
    }

    @Test
    public void testTakeDishes_NoDishes_ReturnsFalse() {
        assertFalse(dishwasher.takeDishes(mock(Person.class)));
    }

}
