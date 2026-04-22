package model.house;

import model.devices.AbstractDevice;
import model.eventCausers.beings.Person;
import model.eventCausers.beings.Pet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class Floor implements HouseComponent{
    private int floorNumber;
    private List<Room> rooms = new ArrayList<>();
    private House house;
    private Random random = new Random();
    private Logger logger = LogManager.getLogger(Floor.class);


    public Floor(int floorNumber, House house) {
        this.floorNumber = floorNumber;
        this.rooms = rooms;
        this.house = house;
    }

    /**
     * Returns the floor number
     * @return floor number
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void addRoom(Room room) {
        rooms.add(room);
        house.rooms.add(room);
    }


    /**
     * Returns all devices in the floor
     * @return list of devices
     */
    @Override
    public List<AbstractDevice> getDevices() {
        List<AbstractDevice> devices = new ArrayList<>();
        for (Room room : rooms) {
            devices.addAll(room.getDevices());
        }
        return devices;
    }

    /**
     * sets the temperature in the entire floor
     * @param temperature temperature to set
     */
    @Override
    public void setTemperature(float temperature) {
        for (Room room : rooms) {
            room.setTemperature(temperature);
        }
    }

    /**
     * Returns all people in the floor
     * @return list of people
     */
    @Override
    public List<Person> getPeople() {
        List<Person> people = new ArrayList<>();
        for (Room room : rooms) {
            people.addAll(room.getPeople());
        }
        return people;
    }

    /**
     * creates and adds a person to the floor
     * @param birthday birthday
     * @param name name
     * @return the created person
     */
    @Override
    public Person createAndAddPerson(LocalDate birthday, String name) {

        Person personToCreate = new Person(birthday, name, this.house);

        Supplier<RoomType> roomTypeSupplier = () -> RoomType.values()[random.nextInt(RoomType.values().length)];


        //random room on this floor
        Room randomRoom = getRandomRoom();
        if(randomRoom == null){
            randomRoom = house.createRoom(roomTypeSupplier.get(),this.floorNumber);
        }

        personToCreate.setCurrentRoom(randomRoom);
        personToCreate.addRoomVisit(randomRoom);


        randomRoom.people.add(personToCreate);
        randomRoom.livingBeings.add(personToCreate);
        house.people.add(personToCreate);
        house.livingBeings.add(personToCreate);


        logger.info("Person named "+ name+ " was created.");

        return personToCreate;
    }

    /**
     * creates and adds a pet to the floor
     * @param birthday birthday
     * @param name name
     * @return the created pet
     */
    @Override
    public Pet createAndAddPet(LocalDate birthday, String name) {
        Pet petToCreate = new Pet(birthday, name, this.house);

        Supplier<RoomType> roomTypeSupplier = () -> RoomType.values()[random.nextInt(RoomType.values().length)];

        //random room on this floor
        Room randomRoom = getRandomRoom();
        if(randomRoom == null){
            randomRoom = house.createRoom(roomTypeSupplier.get(),this.floorNumber);
        }

        petToCreate.setCurrentRoom(randomRoom);
        petToCreate.addRoomVisit(randomRoom);

        randomRoom.pets.add(petToCreate);
        randomRoom.livingBeings.add(petToCreate);
        house.pets.add(petToCreate);
        house.livingBeings.add(petToCreate);

        logger.info("Pet named "+ name+ " was created.");

        return petToCreate;
    }

    /**
     * Returns a random room in the floor or null if there are no rooms
     * @return random room or null
     */

    public Room getRandomRoom() {
        if (rooms.isEmpty()) {
            return null;
        }
        int randomRoomIndex = random.nextInt(rooms.size());
        return rooms.get(randomRoomIndex);
    }
}
