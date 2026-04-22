package UnitTests;

import model.devices.deviceFlyweight.DeviceFlyweightFactory;
import model.events.Event;
import model.devices.AbstractDevice;
import model.devices.DeviceActivityState;
import model.devices.DeviceFactory;
import model.devices.DeviceType;
import model.eventCausers.beings.Person;
import model.eventCausers.beings.Pet;
import model.house.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class LivingBeingAndHouseAndDeviceUnitTests {


    @Test
    void livingBeingTest() {
        LocalDate birthday = LocalDate.of(2000, 2, 2);
        LocalDate birthday2 = LocalDate.of(1111, 2, 2);
        House house = new House("fakeaddress",1);

        Room bathroom = house.createRoom(RoomType.BATHROOM,0);
        DeviceFactory deviceFactory = new DeviceFactory(house);

        bathroom.addDevice(deviceFactory.createDevice(DeviceType.LIGHT, "light1", bathroom));

        Person person = house.createAndAddPerson(birthday, "Doe");
        Pet pet = house.createAndAddPet(birthday, "dogname");

        // Test all getters and setters
        assertEquals(birthday, person.getBirthdate());
        assertEquals("Doe", person.getFirstName());
        assertFalse(person.getCurrentEquipment().isPresent());
        assertEquals(bathroom, person.getCurrentRoom());

        System.out.println(bathroom);

        assertTrue(person.getRoomVisitMap().containsKey(bathroom));


        assertEquals(1, person.getRoomVisitMap().get(bathroom));
        assertEquals(1, person.getRoomVisitMap().size());
        assertEquals(house, person.getHouse());
        assertTrue(person.getDeviceUseMap().isEmpty());

        // Set person's variables again
        person.setBirthdate(birthday2);
        person.setFirstName("guy");
        assertEquals(birthday2, person.getBirthdate());
        assertEquals("guy", person.getFirstName());

        // More assertions for Pet
        assertEquals(birthday, pet.getBirthdate());
        assertEquals("dogname", pet.getFirstName());
        assertEquals(bathroom, pet.getCurrentRoom());
        assertEquals(birthday, pet.getBirthdate());
        assertEquals("dogname", pet.getFirstName());
        assertEquals(house, pet.getHouse());
        pet.setHouse(null);
        assertNull(pet.getHouse());
        pet.setHouse(house);
        assertEquals(house, pet.getHouse());
        pet.setCurrentRoom(bathroom);
        assertEquals(bathroom, pet.getCurrentRoom());

        Event petEvent = pet.generateEvent();
        assertEquals("Pet dogname is playing in BATHROOM", petEvent.getDescription());

    }




    @Test
    void createRoomCheckDevice() {

        House house = new House("123 Main St",1);
        Room livingRoom = house.createRoom(RoomType.LIVING_ROOM,0);

        assertNotNull(livingRoom);
        assertEquals(1, house.getRooms().size());

        System.out.printf("Living room has %d devices\n", livingRoom.getDevices().size());
        assertEquals(1, livingRoom.getDevices().size());
    }



    @Test
    void createPersonAndGenerateFamily() {

        House house = new House("123 Main St",1);
        LocalDate birthday = LocalDate.of(1985, 8, 10);
        house.generateRooms(5);
        Person person = house.createAndAddPerson(birthday, "John");

        assertNotNull(person);
        assertEquals("John", person.getFirstName());
        assertEquals(birthday, person.getBirthdate());
        assertEquals(house, person.getHouse());

        // Generate a family of size 3
        List<Person> family = house.generateFamily(3);

        assertNotNull(family);
        assertEquals(3, family.size());

        // Check if people are correctly added to the house
        assertEquals(4, house.getPeople().size()); // 1 initial person + 3 generated family members
    }


    //testing Composite pattern
    @Test
    void houseTemperatureAndRoomTemperature() {
        House house = new House("123 Main St",1);
        house.generateRooms(2);
        house.setTemperature(25.0f);

        for (Room room : house.getRooms()) {
            assertEquals(25.0f, room.getTemperature());
        }
    }

    @Test
    void houseAndRoomComponentList() {
        House house = new House("123 Main St",1);
        house.generateRooms(3);
        house.generateUpToDeviceCountDevicesInEachRoom(2);

        List<AbstractDevice> houseDevices = house.getDevices();
        List<AbstractDevice> roomDevices = house.getRandomRoom().getDevices();

        assertNotNull(houseDevices);
        assertNotNull(roomDevices);
        assertFalse(houseDevices.isEmpty());
        assertFalse(roomDevices.isEmpty());
        assertTrue(houseDevices.size() >= 2);
        assertTrue(roomDevices.size() >= 2);
    }

    // A subclass for testing purposes, providing necessary overrides
    private class TestDevice extends AbstractDevice {
        public TestDevice(String name, int id, Room room) {
            super(name, id, room);
        }

    }

    @Test
    public void testDeviceInitialization() {
        House house = new House("123 Main St",1);
        house.generateRooms(3);
        String DEVICE_NAME = "Test Device";
        int DEVICE_ID = 100;


        Floor floor = new Floor(0, house);
        Room testRoom = new Room(RoomType.LIVING_ROOM,1,house,floor );
        AbstractDevice testDevice = new TestDevice(DEVICE_NAME, DEVICE_ID, testRoom);


        assert  (house instanceof HouseComponent);
        assert  (testRoom instanceof HouseComponent);
        assert  (floor instanceof HouseComponent);

        // assertions
        assertEquals(DEVICE_NAME, testDevice.getName());
        assertEquals(DEVICE_ID, testDevice.getDeviceId());
        assertEquals(DeviceActivityState.OFF, testDevice.getActivityState());
    }

    @Test
    public void testTurnOn() {
        House house = new House("123 Main St",1);
        Floor floor = new Floor(0, house);
        house.generateRooms(1);
        AbstractDevice testDevice = new TestDevice("TestDevice", 100, house.getRandomRoom());

        Person person = house.createAndAddPerson(LocalDate.of(1990, 1, 1), "John");

        // Turn on the device and check the activity state
        testDevice.turnOn(LocalTime.NOON,person);
        assertEquals(DeviceActivityState.ON, testDevice.getActivityState());
    }

    @Test
    public void testTurnOff() {
        House house = new House("123 Main St",1);
        Floor floor = new Floor(0, house);
        Room testRoom = new Room(RoomType.LIVING_ROOM,100,house,floor );
        AbstractDevice testDevice = new TestDevice("TestDevice", 100, testRoom);

        // Turn off the device and check the activity state
        testDevice.turnOff(LocalTime.NOON);
        assertEquals(DeviceActivityState.OFF, testDevice.getActivityState());
    }


    @Test
    public void testDegradeAndRepair() {
        House house = new House("123 Main St",1);
        Floor floor = new Floor(0, house);
        Room testRoom = new Room(RoomType.LIVING_ROOM,100,house,floor );
        AbstractDevice testDevice = new TestDevice("TestDevice", 100, testRoom);

        testDevice.degradeDevice(50);

        assertEquals(0, testDevice.getDegradation());

        // Repair the device and check if it's back to normal
        testDevice.repairDevice();
        assertEquals(100, testDevice.getDegradation());
    }


    @Test
    void testGetFloorNumber() {
        House house = new House("123 Main St", 1);
        Floor floor = new Floor(1, house);
        assertEquals(1, floor.getFloorNumber());
    }

    @Test
    void testAddRoom() {
        House house = new House("123 Main St", 1);
        Floor floor = new Floor(1, house);

        Room room = new Room(RoomType.LIVING_ROOM, 1,house, floor);
        floor.addRoom(room);

        assertEquals(1, floor.getRooms().size());
        assertTrue(floor.getRooms().contains(room));
    }

    @Test
    void testGetDevices() {
        House house = new House("123 Main St", 1);
        Floor floor = new Floor(1, house);

        Room room1 = new Room(RoomType.BEDROOM, 1, house, floor);
        Room room2 = new Room(RoomType.KITCHEN, 2, house, floor);


        house.getDeviceFactory().createDevice(DeviceType.LIGHT, "Light1", room1);
        house.getDeviceFactory().createDevice(DeviceType.LIGHT, "Light2", room2);

        floor.addRoom(room1);
        floor.addRoom(room2);

//        list all devices in the floor
        floor.getDevices().forEach(System.out::println);

        assertEquals(4, floor.getDevices().size());
    }

    @Test
    void testSetTemperature() {
        House house = new House("123 Main St", 1);
        Floor floor = new Floor(1, house);
        Room room1 = new Room(RoomType.BEDROOM, 1, house, floor);
        Room room2 = new Room(RoomType.KITCHEN, 2, house, floor);

        floor.addRoom(room1);
        floor.addRoom(room2);

        floor.setTemperature(25.0f);

        assertEquals(25.0f, room1.getTemperature());
        assertEquals(25.0f, room2.getTemperature());
    }

    @Test
    void testGetPeople() {
        House house = new House("123 Main St", 1);
        Floor floor = new Floor(1, house);

        Room room1 = new Room(RoomType.BEDROOM, 1, house, floor);
        Room room2 = new Room(RoomType.KITCHEN, 2, house, floor);

        floor.addRoom(room1);
        floor.addRoom(room2);

        floor.createAndAddPerson(LocalDate.of(1990, 1, 1), "John");
        floor.createAndAddPerson(LocalDate.of(1985, 5, 10), "Alice");

        assertEquals(2, floor.getPeople().size());
    }

    @Test
    void testGetRandomRoom() {
        House house = new House("123 Main St", 1);
        Floor floor = new Floor(1, house);

        assertNull(floor.getRandomRoom());

        Room room1 = new Room(RoomType.BEDROOM, 1, house, floor);
        Room room2 = new Room(RoomType.KITCHEN, 2, house, floor);

        floor.addRoom(room1);
        floor.addRoom(room2);

        assertTrue(floor.getRooms().contains(floor.getRandomRoom()));
    }










}
