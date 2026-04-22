package UnitTests;

import model.devices.concreteDevices.blinds.Blinds;
import model.devices.concreteDevices.blinds.BlindsStateClosed;
import model.devices.concreteDevices.blinds.BlindsStateOpen;
import model.events.Event;
import model.house.Floor;
import model.house.House;
import model.house.Room;
import model.house.RoomType;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlindsTest {

    @Test
    public void testOpenBlindsDuringDaytime() {
        House house = new House("adresa", 1);
        Room room = new Room(RoomType.LIVING_ROOM, 1, house, new Floor(0, house));
        Blinds blinds = new Blinds("Living Room Blinds", 1, room);

        blinds.setOpen(false); // Close blinds initially
        LocalTime daytime = LocalTime.of(12, 0);
        blinds.performTimeAction(daytime);

        assertTrue(blinds.isOpen());
    }

    @Test
    public void testCloseBlindsDuringNighttime() {
        House house = new House("adresa", 1);
        Room room = new Room(RoomType.BEDROOM, 2, house, new Floor(0, house));
        Blinds blinds = new Blinds("Bedroom Blinds", 2, room);

        blinds.setOpen(true); // Open blinds initially
        LocalTime nighttime = LocalTime.of(22, 0);
        blinds.performTimeAction(nighttime);

        assertFalse(blinds.isOpen());
    }

    @Test
    public void testNoActionDuringDaytimeIfBlindsAlreadyOpen() {
        House house = new House("adresa", 1);
        Room room = new Room(RoomType.KITCHEN, 3, house, new Floor(0, house));
        Blinds blinds = new Blinds("Kitchen Blinds", 3, room);

        blinds.setOpen(true); // Blinds already open
        LocalTime daytime = LocalTime.of(10, 0);
        assertNull(blinds.performTimeAction(daytime));
    }

    @Test
    public void testNoActionDuringNighttimeIfBlindsAlreadyClosed() {
        House house = new House("adresa", 1);
        Room room = new Room(RoomType.DINING_ROOM, 4, house, new Floor(0, house));
        Blinds blinds = new Blinds("Dining Room Blinds", 4, room);

        blinds.setOpen(false); // Blinds already closed
        LocalTime nighttime = LocalTime.of(2, 0);
        assertNull(blinds.performTimeAction(nighttime));
    }

    @Test
    public void testEventGeneratedWhenClosingBlindsAtNighttime() {
        House house = new House("adresa", 1);
        Room room = new Room(RoomType.LIBRARY, 5, house, new Floor(0, house));
        Blinds blinds = new Blinds("Study Room Blinds", 5, room);

        blinds.setOpen(true); // Open blinds initially
        LocalTime nighttime = LocalTime.of(21, 0);
        Event event = blinds.performTimeAction(nighttime);

        assertFalse(blinds.isOpen());
        assertEquals("All blinds close at nighttime. ", event.getDescription());
        assertEquals(blinds, event.getEventSource());
        assertEquals(List.of(blinds), event.getEventTargets());
    }
}
