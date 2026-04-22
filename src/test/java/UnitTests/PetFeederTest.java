package UnitTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import model.devices.concreteDevices.PetFeeder.PetFeeder;
import model.eventCausers.beings.Person;
import model.events.Event;
import model.house.Floor;
import model.house.House;
import model.house.Room;
import model.house.RoomType;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

public class PetFeederTest {

    private PetFeeder petFeeder;

    @Before
    public void setUp() {
        House house = new House("address", 1);
        petFeeder = new PetFeeder("TestPetFeeder", 1, new Room(RoomType.KITCHEN, 1, house, new Floor(0, house)));
    }

    @Test
    public void testFillFeeder_NoFood_ReturnsTrue() {
        assertTrue(petFeeder.fillFeeder(mock(Person.class)));
        assertTrue(petFeeder.isHasFood());
    }

    @Test
    public void testFillFeeder_AlreadyHasFood_ReturnsFalse() {
        petFeeder.fillFeeder(mock(Person.class));
        assertFalse(petFeeder.fillFeeder(mock(Person.class)));
    }

    @Test
    public void testFeedPet_HasFood_ReturnsTrue() {
        petFeeder.fillFeeder(mock(Person.class));
        assertTrue(petFeeder.feedPet());
        assertFalse(petFeeder.isHasFood());
    }

    @Test
    public void testFeedPet_NoFood_ReturnsFalse() {
        assertFalse(petFeeder.feedPet());
    }

    @Test
    public void testPerformTimeAction_IncreasesPowerConsumption() {
        float initialPowerConsumption = petFeeder.getPowerConsumption();
        petFeeder.performTimeAction(LocalTime.now());
        assertEquals((int)initialPowerConsumption + 5, (int)petFeeder.getPowerConsumption());
    }
}
