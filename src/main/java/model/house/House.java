package model.house;

import model.eventCausers.beings.LivingBeing;
import model.eventCausers.beings.Person;
import model.devices.AbstractDevice;
import model.devices.DeviceFactory;
import model.devices.DeviceType;
import model.eventCausers.beings.Pet;
import model.eventCausers.phenomena.timeEntity.TimeEntitySingleton;
import model.events.EventTarget;
import model.sportEquipment.SportEquipment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;

public class House implements HouseComponent, EventTarget {
    String address ;

    ArrayList<Room> rooms;
    //floors have rooms and


    //floors is a hashmap length of floor count
    HashMap<Integer, Floor> floors;

    private int floorCount = 1;
    ArrayList<Person> people = new ArrayList<>();
    ArrayList<Pet> pets = new ArrayList<>();

    Logger logger = LogManager.getLogger(House.class);

    ArrayList<LivingBeing> livingBeings = new ArrayList<>();

    TimeEntitySingleton timeEntity ;

    DeviceFactory deviceFactory;

    private static final List<String> names = Arrays.asList("John", "Jane", "Bob", "Alice", "Joe", "Sally", "Jim", "Jill", "Jesse", "Walter", "Marie", "Gus", "Mike", "Saul", "Hildegarda", "Margaret", "Adolf", "Ophelia", "Phyllis", "Lucifer", "Fester", "Katniss", "Sirius", "Alucard", "Muhammad");

    Random random = new Random();





    SportEquipment SportEquipment = new SportEquipment();

//    EventCause eventCause = new EventCause(this);
    private int lastRoomIdCreated = -1;


    /**
     * Creates a house with a given address and number of floors
     * @param address the address of the house
     * @param floorCount the number of floors in the house
     */
    public House(String address, int floorCount){
        this.address = address;
        this.rooms = new ArrayList<>();
        this.people = new ArrayList<>();
        this.lastRoomIdCreated = 0;
        this.floorCount = floorCount;
        this.timeEntity = TimeEntitySingleton.getInstance(this);
        this.floors = new HashMap<>();
        this.deviceFactory = new DeviceFactory(this);

        for (int i = 0; i < floorCount; i++) {
            floors.put(i, new Floor(i,this));
        }

    }


    /**
     * Creates a room and adds it to the house
     * @param roomType the type of room to create
     * @param floorNum the floor to add the room in
     * @return the room created
     */
    public Room createRoom(RoomType roomType, int floorNum){
        if(floorNum > floorCount || floorNum < 0){
            logger.error("Floor number is out of bounds. Room not created.");
            System.out.println("Floor number is out of bounds. Room not created.");
            return null;
        }

        //find the floor to add the room in if it exists
        Floor floorToAddRoomIn = getFloor(floorNum);


        Room roomToCreate = new Room(roomType, lastRoomIdCreated,this,floorToAddRoomIn);
        rooms.add(roomToCreate);
        lastRoomIdCreated++;
        return roomToCreate;
    }

    /**
     * Removes a room from the house
     * @param roomToRemove the room to remove
     */
    public void removeRoom(Room roomToRemove){
        rooms.remove(roomToRemove);
    }




    /**
     * Creates a person and adds it to the house in a random room
     * @param birthday the person's birthday
     * @param name the person's name
     * @return the person created
     */
    public Person createAndAddPerson(LocalDate birthday, String name)  {

        logger.info("Person named "+ name+ " was created.");

        Person personToCreate = new Person(birthday, name, this);
        Room room = getRandomRoom();

        people.add(personToCreate);
        livingBeings.add(personToCreate);

        personToCreate.setCurrentRoom(room);
        personToCreate.addRoomVisit(room);

        room.addLivingBeing(personToCreate);
        room.addPerson(personToCreate);


        return personToCreate;
    }

    /**
     * Creates a pet and adds it to the house in a random room
     * @param birthday the pet's birthday
     * @param petName the pet's name
     * @return the pet created
     */
    public Pet createAndAddPet(LocalDate birthday, String petName) {

        logger.info("Pet named "+ petName+ " was created.");
        Pet petToCreate = new Pet(birthday, petName, this);
        Room room = getRandomRoom();

        pets.add(petToCreate);
        livingBeings.add(petToCreate);



        petToCreate.setCurrentRoom(room);
        petToCreate.addRoomVisit(room);

        room.addLivingBeing(petToCreate);
        room.addPet(petToCreate);

        return petToCreate;
    }

    /**
     * Sets the temperature in all the rooms in the house
     * @param temperature
     */
    @Override
    public void setTemperature(float temperature) {
        for (Room room : rooms) {
            room.setTemperature(temperature);
        }
        logger.info("The temperature in the house is now " + temperature);
    }

    /**
     * @return a list of all the people in the house
     */
    public List<Person> getPeople() {
        return people;
    }

    /**
     * @return a list of all the devices in the house
     */
    public List<AbstractDevice> getDevices() {
        ArrayList<AbstractDevice> devices = new ArrayList<>();
        for (Room room : rooms) {
            devices.addAll(room.getDevices());
        }
        return devices;

    }

    /**
     * @return a random room from the house
     */
    public Room getRandomRoom() {
        if (rooms.isEmpty()) {
            return null;
        }

        return rooms.get(random.nextInt(rooms.size()));
    }


    /**
     * Generates roomcount number of rooms for each floor
     * @param roomCount
     */
    public void generateRooms(int roomCount) {
        Supplier<RoomType> roomTypeSupplier = () -> RoomType.values()[random.nextInt(RoomType.values().length)];
        for (int i = 0; i < roomCount; i++) {
            //if a floor has no rooms, prioritize creating a room there starting from lower floors. If all floors have rooms, create a room on a random floor




            for (int j = 0; j < floorCount; j++) {

                //Garage cannot be created on any floor other than the first floor
                if (roomTypeSupplier.get() == RoomType.GARAGE && j != 0) {
                    continue;
                }

                if (floors.get(j).getRooms().isEmpty()) {
                    createRoom(roomTypeSupplier.get(), j);
                    break;
                } else if (j == floorCount - 1) {
                    createRoom(roomTypeSupplier.get(), random.nextInt(floorCount));
                }
            }
            logger.info("Created " + roomCount + " rooms.");
        }
    }


    private Floor getFloor(int floorNum) {
        return floors.get(floorNum);
    }


    /**
     * Generates a random number of devices for each room
     * @param deviceCount the max number of devices to generate for each room
     */
    public void generateUpToDeviceCountDevicesInEachRoom(int deviceCount) {
        //SUPPLIER
                int actualDeviceCountGenerated = 0;

        Supplier<DeviceType> deviceTypeSupplier = () -> DeviceType.values()[random.nextInt(DeviceType.values().length)];
        for (Room room : getRooms()) {

            //generate a random number of devices for each room btwn 1 and deviceCount
            for (int i = 0; i < random.nextInt(deviceCount) + 1; i++) {
                //get a random device type
                DeviceType currentDeviceType= deviceTypeSupplier.get();
                // Create a device with a unique name and add it to the room
                deviceFactory.createDevice(currentDeviceType, room.getName() + "_" + currentDeviceType.name() , room);
                actualDeviceCountGenerated++;
            }
        }
        logger.info("Created " + actualDeviceCountGenerated + " devices.");
    }


    public ArrayList<Room> getRooms() {
        return rooms;
    }


    /**
     * Generates a family of people with random names and birthdays
     * @param familySize the size of the family to generate
     * @return the list of people generated
     */

    public List<Person>  generateFamily(int familySize)  {
        Supplier<String> firstNameSupplier = () -> names.get(random.nextInt(names.size()));
        //birthday supplier
        Supplier<LocalDate> birthdaySupplier = () ->
                LocalDate.of(
                        random.nextInt(100) + 1900,
                        random.nextInt(12) + 1,
                        random.nextInt(28) + 1);


        List<Person> familyMembers = new ArrayList<>();

        for (int i = 0; i < familySize; i++) {
            familyMembers.add(createAndAddPerson(birthdaySupplier.get(), firstNameSupplier.get()));
        }

        logger.info("Created a family of " + familySize + " people.");

        return familyMembers;

    }




    public String getAddress() {
        return address;
    }



    public int getLastRoomIdCreated() {
        return lastRoomIdCreated;
    }


    public SportEquipment getSportEquipment() {
        return SportEquipment;
    }

    public TimeEntitySingleton getTimeEntity() {
        return timeEntity;
    }

    /**
     * Generates a random assortment of sport equipment for the house - 50% bikes, 50% skis
     * @param sportEquipmentCount the  number of sport equipment to generate
     */
    public void generateSportEquipment(int sportEquipmentCount) {
        SportEquipment sportEquipment = new SportEquipment();
        for (int i = 0; i < sportEquipmentCount; i++) {
            //50% chance to add bike 50% to add skis
//            sportEquipment.addEquipment(model.sportEquipment.SportEquipment.EquipmentType.BIKE);

            if (random.nextBoolean()) {
                sportEquipment.addEquipment(model.sportEquipment.SportEquipment.EquipmentType.BIKE);
            } else {
                sportEquipment.addEquipment(model.sportEquipment.SportEquipment.EquipmentType.SKIS);
            }

        }
    }

    public Floor getFloorByNumber(int floorNum){
        return floors.get(floorNum);
    }


    public List<Pet> generatePets(int petCount) {
        Supplier<String> firstNameSupplier = () -> names.get(random.nextInt(names.size()));
        //birthday supplier
        Supplier<LocalDate> birthdaySupplier = () ->
                LocalDate.of(
                        random.nextInt(100) + 1900,
                        random.nextInt(12) + 1,
                        random.nextInt(28) + 1);


        for (int i = 0; i < petCount; i++) {
            createAndAddPet(birthdaySupplier.get(), firstNameSupplier.get());
        }
        logger.info("Created " + petCount + " pets.");


        return pets;
    }

    public DeviceFactory getDeviceFactory() {
        return deviceFactory;
    }


    public AbstractDevice createRandomDevice(){
        Supplier<DeviceType> deviceTypeSupplier = () -> DeviceType.values()[random.nextInt(DeviceType.values().length)];
        AbstractDevice randomDevice = null;


        //get a random device type
        DeviceType randomDeviceType = deviceTypeSupplier.get();

        // Create a device with a unique name and add it to the room
        randomDevice = deviceFactory.createDeviceDontAddToRoom(randomDeviceType, randomDeviceType.name() + random.nextInt(1000));
        logger.info("Created random device, didn't add to any room.");

        return randomDevice;
    }

    public AbstractDevice generateDeviceOfType(DeviceType deviceType) {
        return deviceFactory.generateDeviceOfTypeInRandomRoom(deviceType);
    }

    public HashMap<Integer, Floor> getFloors() {
        return floors;
    }

    public List<Pet> getPets() {
        return pets;
    }
}
