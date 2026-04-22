package UnitTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import model.devices.concreteDevices.heatingPanel.*;
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

public class HeatingPanelTest {

    private HeatingPanel heatingPanel;

    @Before
    public void setUp() {
        House house = new House("adresa",1);
        Room room = new Room(RoomType.BEDROOM,1,house,new Floor(0,house));
        room.setTemperature(20.0f);
        heatingPanel = new HeatingPanel("TestHeatingPanel",1 ,room);
        heatingPanel.setState(new HeatingPanelOffState());
    }

    @Test
    public void testTurnOn_HeatingPanelOff_ReturnsTrue() {
        assertTrue(heatingPanel.turnOn(LocalTime.now(), mock(Person.class)));
        assertTrue(heatingPanel.getState() instanceof HeatingPanelOnState);
    }

    @Test
    public void testTurnOn_HeatingPanelOn_ReturnsFalse() {
        heatingPanel.setState(new HeatingPanelOnState());
        assertFalse(heatingPanel.turnOn(LocalTime.now(), mock(Person.class)));
    }


    @Test
    public void testTurnOff_HeatingPanelOff_ReturnsFalse() {
        assertFalse(heatingPanel.turnOff(LocalTime.now()));
    }

    @Test
    public void testIncreaseTemperature_HeatingPanelOn_TemperatureIncreased() {
        heatingPanel.setState(new HeatingPanelOnState());
        float initialTemperature = heatingPanel.getTemperature();
        heatingPanel.increaseTemperature(2.0f);
        assertEquals(initialTemperature + 2.0f, heatingPanel.getTemperature(), 0.01f);
    }

    @Test
    public void testDecreaseTemperature_HeatingPanelOn_TemperatureDecreased() {
        heatingPanel.setState(new HeatingPanelOnState());
        float initialTemperature = heatingPanel.getTemperature();
        heatingPanel.decreaseTemperature(2.0f);
        assertEquals(initialTemperature - 2.0f, heatingPanel.getTemperature(), 0.01f);
    }

}
