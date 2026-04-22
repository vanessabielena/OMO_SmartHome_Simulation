package UnitTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import model.devices.concreteDevices.washing_machine.*;
import model.eventCausers.beings.Person;
import model.events.Event;
import model.house.Floor;
import model.house.House;
import model.house.Room;
import model.house.RoomType;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

public class WashingMachineTest {

    private WashingMachine washingMachine;

    @Before
    public void setUp() {
        House house = new House("address", 1);
        washingMachine = new WashingMachine("TestWashingMachine", 1, new Room(RoomType.LAUNDRYROOM, 1, house, new Floor(0, house)));
    }

    @Test
    public void testTurnOn_WashingMachineOff_ReturnsTrue() {
        assertTrue(washingMachine.turnOn(LocalTime.now(), mock(Person.class)));
        assertTrue(washingMachine.getState() instanceof WashingMachineActiveState);
    }

    @Test
    public void testTurnOn_WashingMachineActive_ReturnsFalse() {
        washingMachine.setState(new WashingMachineActiveState());
        assertFalse(washingMachine.turnOn(LocalTime.now(), mock(Person.class)));
    }



    @Test
    public void testTurnOff_WashingMachineIdle_ReturnsFalse() {
        washingMachine.setState(new WashingMachineIdleState());
        assertTrue(washingMachine.turnOff(LocalTime.now()));
    }

    @Test
    public void testPerformTimeAction_WashingMachineActive_ReturnsNotNullEvent() {
        washingMachine.setState(new WashingMachineActiveState());
        assertNotNull(washingMachine.performTimeAction(LocalTime.now()));
        assertTrue(washingMachine.getState() instanceof WashingMachineIdleState);
    }

    @Test
    public void testCalculatePowerConsumption_ReturnsCorrectValue() {
        long durationInSeconds = 3600; // 1 hour
        float expectedPowerConsumption = 0.01f * durationInSeconds;
        assertEquals(expectedPowerConsumption, washingMachine.calculatePowerConsumption(durationInSeconds), 0.001);
    }

    @Test
    public void testAddClothes_NoClothes_ReturnsTrue() {
        assertTrue(washingMachine.addClothes(mock(Person.class)));
        assertTrue(washingMachine.isHasClothes());
    }

    @Test
    public void testAddClothes_AlreadyHasClothes_ReturnsFalse() {
        washingMachine.addClothes(mock(Person.class));
        assertFalse(washingMachine.addClothes(mock(Person.class)));
    }

    @Test
    public void testTakeClothes_HasClothes_ReturnsTrue() {
        washingMachine.addClothes(mock(Person.class));
        assertTrue(washingMachine.takeClothes(mock(Person.class)));
        assertFalse(washingMachine.isHasClothes());
    }

    @Test
    public void testTakeClothes_NoClothes_ReturnsFalse() {
        assertFalse(washingMachine.takeClothes(mock(Person.class)));
    }
}
