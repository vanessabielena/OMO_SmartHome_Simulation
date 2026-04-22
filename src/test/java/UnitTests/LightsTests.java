package UnitTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import model.devices.concreteDevices.lights.*;
import model.devices.deviceFlyweight.DeviceFlyweightFactory;
import model.eventCausers.beings.Person;
import model.house.Floor;
import model.house.House;
import model.house.Room;
import model.house.RoomType;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.time.LocalTime;

public class LightsTests {

    private Lights lights;

    @Before
    public void setUp() {

        House house = new House("adresa",1);
        Room room = new Room(RoomType.BEDROOM,1,house,new Floor(0,house));
        lights = new Lights("TestLights", 1, room);
        lights.setState(new LightsOffState());
    }

    @Test
    public void testTurnOn_LightsOff_ReturnsTrue() {
        assertTrue(lights.turnOn(LocalTime.now(), mock(Person.class)));
        assertTrue(lights.getState() instanceof LightsOnState);
    }

    @Test
    public void testTurnOn_LightsOn_ReturnsFalse() {
        lights.setState(new LightsOnState());
        assertFalse(lights.turnOn(LocalTime.now(), mock(Person.class)));
    }

    @Test
    public void testTurnOff_LightsOn_ReturnsFalse() {
        lights.setLastTurnOnTimestamp(LocalTime.now());
        assertFalse(lights.turnOff(LocalTime.now()));
        assertTrue(lights.getState() instanceof LightsOffState);
    }

    @Test
    public void testTurnOff_LightsOff_ReturnsFalse() {
        assertFalse(lights.turnOff(LocalTime.now()));
    }

    @Test
    public void testSetBrightness_LightsOn_BrightnessSet() {
        lights.setState(new LightsOnState());
        int initialBrightness = lights.getBrightness();
        lights.setBrightness(75);
        assertEquals(75, lights.getBrightness());
    }

    @Test
    public void testCalculatePowerConsumption_LightsOn_PowerConsumptionCalculated() {
        lights.setState(new LightsOnState());
        lights.setBrightness(50);
        float powerConsumption = lights.calculatePowerConsumption(3600); // 1 hour
        assertEquals(9f, powerConsumption, 0.01f);
    }

    // Add more tests as needed for other scenarios and edge cases
}
