package model.house;

import model.devices.AbstractDevice;
import model.devices.DeviceFactory;
import model.devices.DeviceType;
import model.devices.concreteDevices.sensors.TemperatureSensor;
import model.eventCausers.beings.LivingBeing;
import model.eventCausers.beings.Person;
import model.eventCausers.beings.Pet;
import model.events.EventTarget;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * This class represents a room in the house.
 */
public class Room implements HouseComponent, EventTarget {

    private ArrayList<AbstractDevice> devices = new ArrayList<>();
    private RoomType roomType;
    private String name;
    private int id;
    List<LivingBeing> livingBeings = new ArrayList<>();
    List<Pet> pets = new ArrayList<>();
    private float temperature = 22.0f;
    private TemperatureSensor temperatureSensor;
    List<Person> people = new ArrayList<>();

    private House house;

    private Window window;
    private Floor floor;
    Logger logger = LogManager.getLogger(Room.class);


    /**
     * Constructor for Room
     * @param roomType type of room
     * @param id id of room
     * @param house house
     * @param floor floor
     */
    public Room( RoomType roomType, int id, House house, Floor floor) {
        this.name = roomType.toString().toLowerCase() + id;
        this.roomType = roomType;
        this.id = id;
        this.house = house;
        this.floor = floor;

        //room has a 35% chance of having a window
        if (Math.random() < 0.35) {
            if(Math.random() < 0.5){
                //create blinds
                house.getDeviceFactory().createDeviceOfTypeInRoom(DeviceType.BLINDS, this);
            }
            this.window = new Window();
        }


        house.getDeviceFactory().createDeviceOfTypeInRoom(DeviceType.TEMPERATURE_SENSOR, this);
    }
    public void addDevice(AbstractDevice device) {
        devices.add(device);
    }




    public void removeAllDevices() {
        devices.clear();
    }

    /**
     * Returns the devices in the room
     * @return devices in the room
     */
    public ArrayList<AbstractDevice> getDevices() {
        return devices;
    }


    public RoomType getRoomType() {
        return roomType;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    @Override
    public List<Person> getPeople() {
        return people;
    }


    public House getHouse() {
        return house;
    }


    public void setHouse(House house) {
        this.house = house;
    }

    /**
     * Creates and adds a person to the house and sets him to be in the current room
     * @param birthday birthday of person
     * @param name name of person
     * @return person created
     */
    @Override
    public Person createAndAddPerson(LocalDate birthday, String name) {


        Person personToCreate = new Person(birthday, name, this.house);

        personToCreate.setCurrentRoom(this);
        personToCreate.addRoomVisit(this);

        house.people.add(personToCreate);
        house.livingBeings.add(personToCreate);

        people.add(personToCreate);
        livingBeings.add(personToCreate);

        logger.info("Person named "+ name+ " was created.");

        return personToCreate;

    }

    /**
     * Creates and adds a pet to the house and sets him to be in the current room
     * @param birthday birthday of pet
     * @param name name of pet
     * @return pet created
     */
    @Override
    public Pet createAndAddPet(LocalDate birthday, String name) {

        Pet petToCreate = new Pet(birthday, name, this.house);


        petToCreate.setCurrentRoom(this);
        petToCreate.addRoomVisit(this);

        house.pets.add(petToCreate);
        house.livingBeings.add(petToCreate);

        pets.add(petToCreate);
        livingBeings.add(petToCreate);

        return petToCreate;

    }

    public void removeDevice(AbstractDevice device) {
        this.devices.remove(device);
    }

    public TemperatureSensor getTemperatureSensor() {
        return temperatureSensor;
    }

    public void addLivingBeing(LivingBeing livingBeing){
        livingBeings.add(livingBeing);
    }

    public void addPerson(Person person){
        people.add(person);
    }

    public void addPet(Pet pet){
        pets.add(pet);
    }


    public void setLivingBeings(List<LivingBeing> livingBeings) {
        this.livingBeings = livingBeings;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public void setTemperatureSensor(TemperatureSensor temperatureSensor) {
        this.temperatureSensor = temperatureSensor;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setDevices(ArrayList<AbstractDevice> devices) {
        this.devices = devices;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }


    public Window getWindow() {
        return window;
    }


}
