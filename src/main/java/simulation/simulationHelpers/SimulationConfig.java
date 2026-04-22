package simulation.simulationHelpers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.eventCausers.beings.Pet;
import model.eventCausers.phenomena.TemperatureEntity;
import model.events.Event;
import model.eventCausers.beings.Person;
import model.house.House;

import java.util.*;


/**
 * This class is used to store all the simulation configuration parameters.
 */
public class SimulationConfig {
    public static final int DEFAULT_FLOOR_LIMIT = 100;
    public static final int DEFAULT_PET_LIMIT = 20;
    // Add fields for all simulation parameters in your JSON file

    @JsonIgnore
    final List<String> addresses = Arrays.asList("123 Ulice", "456 Kratka", "789 Dlha", "101112 Stredna", "131415 Velka", "161718 Mala", "192021 Nova", "222324 Stara", "252627 Hlavna", "282930 Dolna");
    @JsonIgnore
    private String JSON_FILE_PATH = "JsonDataFiles/default_simulation_data.json";
    @JsonIgnore
    private House house;
    @JsonIgnore
    List<Event> loggedEvents = new ArrayList<>();
    @JsonIgnore
    private List<Person> family = new ArrayList<>();
    @JsonIgnore
    private List<Pet> pets = new ArrayList<>();
    @JsonIgnore
    private List<TemperatureEntity> temperatureEntities = new ArrayList<>();


    private int petCount = 3;
    private int roomCount = 5;
    private int deviceCountForEachRoom = 20;
    private int familySize = 5;
    private float simulationRunTime = 5; // in seconds
    private int sportEquipmentCount = 3;
    private float kwhPrice = 0.1331f;
    private float literOfWaterPrice = 0.2f;
    private float literOfGasPrice = 1.77f;
    private float cycleSecondInterval = 1; // in seconds

    private int floorCount = 1;




    @JsonIgnore
    public static final int DEFAULT_ROOM_LIMIT = 100;
    @JsonIgnore
    public static final int DEFAULT_DEVICE_LIMIT = 100;
    @JsonIgnore
    public static final int DEFAULT_FAMILY_LIMIT = 10;
    @JsonIgnore
    public static final int DEFAULT_SPORT_EQUIPMENT_LIMIT = 50;
    @JsonIgnore
    public static final int DEFAULT_FREQUENCY_LIMIT = 3600;
    @JsonIgnore
    public static final float DEFAULT_PRICE_LIMIT = 100.0f;
    @JsonIgnore
    public static final int DEFAULT_RUNTIME_LIMIT = 9999999;



    //GETTERS SETTERS




    public List<String> getAddresses() {
        return addresses;
    }


    public String getJSON_FILE_PATH() {
        return JSON_FILE_PATH;
    }

    public List<Event> getLoggedEvents() {
        return loggedEvents;
    }

    public void setLoggedEvents(List<Event> loggedEvents) {
        this.loggedEvents = loggedEvents;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public int getDeviceCountForEachRoom() {
        return deviceCountForEachRoom;
    }

    public void setDeviceCountForEachRoom(int deviceCountForEachRoom) {
        this.deviceCountForEachRoom = deviceCountForEachRoom;
    }

    public int getFamilySize() {
        return familySize;
    }

    public void setFamilySize(int familySize) {
        this.familySize = familySize;
    }

    public float getSimulationRunTime() {
        return simulationRunTime;
    }

    public void setSimulationRunTime(float simulationRunTime) {
        this.simulationRunTime = simulationRunTime;
    }

    public int getSportEquipmentCount() {
        return sportEquipmentCount;
    }

    public void setSportEquipmentCount(int sportEquipmentCount) {
        this.sportEquipmentCount = sportEquipmentCount;
    }

    public float getKwhPrice() {
        return kwhPrice;
    }

    public void setKwhPrice(float kwhPrice) {
        this.kwhPrice = kwhPrice;
    }

    public float getLiterOfWaterPrice() {
        return literOfWaterPrice;
    }

    public void setLiterOfWaterPrice(float literOfWaterPrice) {
        this.literOfWaterPrice = literOfWaterPrice;
    }

    public float getLiterOfGasPrice() {
        return literOfGasPrice;
    }

    public void setLiterOfGasPrice(float literOfGasPrice) {
        this.literOfGasPrice = literOfGasPrice;
    }



    public List<Person> getFamily() {
        return family;
    }

    public void setFamily(List<Person> family) {
        this.family = family;
    }

    public float getCycleSecondInterval() {
        return cycleSecondInterval;
    }

    public void setCycleSecondInterval(float eventFrequency) {
        this.cycleSecondInterval = eventFrequency;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }



    public void setJSON_FILE_PATH(String JSON_FILE_PATH) {
        this.JSON_FILE_PATH = JSON_FILE_PATH;
    }


    @Override
    public String toString() {
        return "SimulationConfig{" +
                "addresses=" + addresses +
                ", JSON_FILE_PATH='" + JSON_FILE_PATH + '\'' +
                ", house=" + house +
                ", loggedEvents=" + loggedEvents +
                ", family=" + family +
                ", roomCount=" + roomCount +
                ", deviceCountForEachRoom=" + deviceCountForEachRoom +
                ", familySize=" + familySize +
                ", simulationRunTime=" + simulationRunTime +
                ", sportEquipmentCount=" + sportEquipmentCount +
                ", kwhPrice=" + kwhPrice +
                ", literOfWaterPrice=" + literOfWaterPrice +
                ", literOfGasPrice=" + literOfGasPrice +
                ", cycleSecondInterval=" + cycleSecondInterval +
                '}';
    }

    public int getFloorCount() {
        return floorCount;
    }

    public void setFloorCount(int i) {
        //has to be positive
        if(i > 0){
            this.floorCount = i;
        }
    }

    public int getPetCount() {
        return petCount;
    }

    public void setPetCount(int petCount) {
        this.petCount = petCount;
    }


    public List<TemperatureEntity> getTemperatureEntities() {
        return temperatureEntities;
    }

    public void setTemperatureEntities(List<TemperatureEntity> temperatureEntities) {
        this.temperatureEntities = temperatureEntities;
    }


    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
}
