package UnitTests;
import model.devices.concreteDevices.blinds.Blinds;
import model.devices.concreteDevices.blinds.BlindsStateClosed;
import model.devices.concreteDevices.blinds.BlindsStateOpen;
import model.devices.concreteDevices.heatingPanel.HeatingPanel;
import model.devices.concreteDevices.sensors.TemperatureSensor;
import model.events.Event;
import model.house.Floor;
import model.house.House;
import model.house.Room;
import model.house.RoomType;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TempSensorTest {

    private Room room;
    private HeatingPanel heatingPanel;
    private TemperatureSensor temperatureSensor;

    @Before
    public void setUp() {
        House house = new House("adresa", 1);
        Room room = new Room(RoomType.LIVING_ROOM, 1, house, new Floor(0, house));
        heatingPanel = mock(HeatingPanel.class);
        temperatureSensor = new TemperatureSensor("TestSensor", 1, room);
        room.addDevice(heatingPanel);
        temperatureSensor.subscribe(heatingPanel);
    }

    @Test
    public void testUpdateHighTemperature() {
        float highTemperature = 30.0f;
        temperatureSensor.update(highTemperature);

        verify(heatingPanel, times(1)).decreaseTemperature(anyFloat());
    }

    @Test
    public void testUpdateLowTemperature() {
        float lowTemperature = 18.0f;
        temperatureSensor.update(lowTemperature);

        verify(heatingPanel, times(1)).increaseTemperature(anyFloat());
    }

    @Test
    public void testCalculateCoolingAdjustment() {
        float temperature = 30.0f;
        float adjustment = temperatureSensor.calculateCoolingAdjustment(temperature);

        assertEquals(0.4f, adjustment, 0.01);
    }

    @Test
    public void testCalculateHeatingAdjustment() {
        float temperature = 18.0f;
        float adjustment = temperatureSensor.calculateHeatingAdjustment(temperature);

        assertEquals(0.4f, adjustment, 0.01);
    }

    @Test
    public void testFindHeatingPanelInRoom() {
        HeatingPanel foundHeatingPanel = temperatureSensor.findHeatingPanelInRoom();

        assertEquals(heatingPanel, foundHeatingPanel);
    }
}

