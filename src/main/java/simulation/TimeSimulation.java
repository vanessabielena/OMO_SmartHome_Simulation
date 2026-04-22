package simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.eventCausers.beings.Pet;
import model.eventCausers.phenomena.TemperatureEntity;
import model.events.Event;
import model.devices.AbstractDevice;
//import model.devices.concreteDevices.sensors.TimeSensor;
import model.eventCausers.beings.Person;
import model.eventCausers.phenomena.timeEntity.TimeEntitySingleton;
import model.house.House;
import simulation.simulationHelpers.EventGeneratorTask;
import simulation.simulationHelpers.SimulationConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * This class is responsible for the simulation of the house.
 */
public class TimeSimulation {

    static final List<String> addresses = Arrays.asList("123 Ulice", "456 Kratka", "789 Dlha", "101112 Stredna", "131415 Velka", "161718 Mala", "192021 Nova", "222324 Stara", "252627 Hlavna", "282930 Dolna");
    private static final Logger logger = LogManager.getLogger(TimeSimulation.class);
    private static final Random random = new Random();

    public static final SimulationConfig simulationConfig = new SimulationConfig();


    public static final int LOWER_INT_LIMIT = 1;
    public static final float LOWER_FLOAT_LIMIT = 0.001f;

    private long systemStartTime;
    private Timer timer = new Timer();


    public static void main(String[] args) {
        // Configure log4j2
        System.setProperty("log4j.configurationFile", "path/to/log4j2.xml");

        System.out.println("Starting house configuration.");
        houseConfig();

        Reports.generateEventReport();
        Reports.generateHouseConfigReport();
        Reports.generateActivityAndUsageReport();
        Reports.generateConsumptionReport();
        Reports.generateEventReportOrdered();

        TimeSimulation timeSimulation = new TimeSimulation();
        timeSimulation.startSimulation();

        try {
            Thread.sleep((long) (simulationConfig.getSimulationRunTime() * 1000));
        } catch (InterruptedException e) {
            logger.error("Thread sleep interrupted", e);
        }

        ArrayList<AbstractDevice> list = getHouse().getTimeEntity().getSubscribedDevices();

        // turn off all devices, so that the consumption gets updated
        getHouse().getDevices().forEach(device -> device.turnOff(getHouse().getTimeEntity().getCurrentTime().getCurrentLocalTime()));

        timeSimulation.timer.cancel();
        System.out.println("Simulation stopped");



        timeSimulation.fillOutConsumptionReport();


        SimulationLogger.finishLogging();

        System.exit(0);
    }


    /**
     * Configures the house.
     */
    public static void houseConfig() {

        SimulationLogger.clearSimulationLogs();
        SimulationLogger.logStartTimeInAllLogs();


        logger.info("Log files cleared.");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Do you want to input custom values? (y/n): ");
        String userInput = scanner.next().toLowerCase();

        // Check if the input is neither "y" nor "n"
        while (!userInput.equals("y") && !userInput.equals("n")) {
            System.out.println("Invalid input. Please enter 'y' or 'n': ");
            userInput = scanner.nextLine().toLowerCase();
        }


        // Inside houseConfig() method
        if (userInput.equals("y")) {

            System.out.println("Do you want to load the input from the JSON file? (y/n): ");
            userInput = scanner.next().toLowerCase();

            while (!userInput.equals("y") && !userInput.equals("n")) {
                System.out.println("Invalid input. Please enter 'y' or 'n': ");
                userInput = scanner.nextLine().toLowerCase();
            }

            if (userInput.equals("y")) {

                //////////////////////////
                /// JSON CONFIGURATION
                //////////////////////////
                if(!loadConfigFromJSON()){


                    // If the JSON file is invalid, get the input from the user
                    logger.error("Invalid JSON file. Getting input from the user.");
                    System.out.println("Invalid JSON file. Getting input from the user.");
                    manualInputConfig(scanner);
                }

            } else {
                // Get the input from the user
                manualInputConfig(scanner);
            }
        }


        House house = new House(addresses.get(random.nextInt(addresses.size())),simulationConfig.getFloorCount());

        simulationConfig.setHouse(house);

        SimulationLogger.logHouseConfig("House created with some configuration.");

        int roomCount = simulationConfig.getRoomCount();
        house.generateRooms(roomCount);
        SimulationLogger.logHouseConfig("Number of rooms: " + roomCount);

        int deviceCountForEachRoom = simulationConfig.getDeviceCountForEachRoom();
        house.generateUpToDeviceCountDevicesInEachRoom(deviceCountForEachRoom);
        SimulationLogger.logHouseConfig("Number of devices: " + deviceCountForEachRoom);

        int familySize = simulationConfig.getFamilySize();
        simulationConfig.setFamily(house.generateFamily(familySize));

        int petCount = simulationConfig.getPetCount();
        List<Pet> pets = house.generatePets(petCount);

        simulationConfig.setPets(pets);
        simulationConfig.setPetCount(petCount);
        SimulationLogger.logHouseConfig("Number of pets: " + petCount);

        SimulationLogger.logHouseConfig("Size of the family: " + familySize);

        int sportEquipmentCount = simulationConfig.getSportEquipmentCount();
        house.generateSportEquipment(sportEquipmentCount);
        SimulationLogger.logHouseConfig("Number of sport equipment: " + sportEquipmentCount);
        SimulationLogger.logHouseConfig("Equipment created: \n" + house.getSportEquipment().getEquipmentAvailabilityMap().toString());

        float cycleSecondInterval = simulationConfig.getCycleSecondInterval();
        SimulationLogger.logHouseConfig("Frequency at which events are generated: " + cycleSecondInterval + " sec");

        TimeEntitySingleton timeEntity = TimeEntitySingleton.getInstance(house);


        System.out.println(house.getRandomRoom());


        generateTemperatureEntities(house);

        System.out.println("House: " + house);
        System.out.println("Rooms: " + house.getRooms());
        System.out.println("Occupants: " + house.getPeople());
        System.out.println("Devices: " + house.getDevices());
        System.out.println("Family: " + simulationConfig.getFamily());
    }


    /**
     * Generates temperature entities. half of the rooms have a temperature entity
     * @param house house
     */

    private static void generateTemperatureEntities(House house) {
        int roomCount = house.getRooms().size();
        for (int i = 0; i < roomCount/2; i++) {
            TemperatureEntity temperatureEntity = new TemperatureEntity(house);
            simulationConfig.getTemperatureEntities().add(temperatureEntity);
        }
    }

    /**
     * Gets the input from the user.
     * @param scanner scanner to get the input from
     */
    private static void manualInputConfig(Scanner scanner) {
        boolean validInput = false;
        while (!validInput) {
            try {

                //floors,
                //rooms,
                //pets
                //family members
                //sport equipment
                //frequency
                //gas price
                //water price
                //electricity price
                //simulation run time


                System.out.println("Enter the number of floors in the house: ");
                simulationConfig.setFloorCount(validateNumericInput(scanner, LOWER_INT_LIMIT, SimulationConfig.DEFAULT_FLOOR_LIMIT));

                System.out.println("Enter the number of rooms in the house: ");
                simulationConfig.setRoomCount(validateNumericInput(scanner, LOWER_INT_LIMIT, SimulationConfig.DEFAULT_ROOM_LIMIT));


                System.out.println("Enter the number of pets in the house: ");
                simulationConfig.setPetCount(validateNumericInput(scanner, 0, SimulationConfig.DEFAULT_PET_LIMIT));

                System.out.println("Enter the number of family members: ");
                simulationConfig.setFamilySize(validateNumericInput(scanner, LOWER_INT_LIMIT, SimulationConfig.DEFAULT_FAMILY_LIMIT));

                System.out.println("Enter the number of sport equipment in the house: ");
                simulationConfig.setSportEquipmentCount(validateNumericInput(scanner, 0, SimulationConfig.DEFAULT_SPORT_EQUIPMENT_LIMIT));


                System.out.println("Enter the interval at which simulation cycles happen in seconds: ");

                int cycleSecondInterval = validateNumericInput(scanner, 1, SimulationConfig.DEFAULT_FREQUENCY_LIMIT);

                simulationConfig.setCycleSecondInterval(cycleSecondInterval);



                System.out.println("Enter the gas price per litre in USD: ");
                simulationConfig.setLiterOfGasPrice(validatePositiveNumericInput(scanner, LOWER_FLOAT_LIMIT, SimulationConfig.DEFAULT_PRICE_LIMIT));

                System.out.println("Enter the water price per litre in USD: ");
                simulationConfig.setLiterOfWaterPrice(validatePositiveNumericInput(scanner, LOWER_FLOAT_LIMIT, SimulationConfig.DEFAULT_PRICE_LIMIT));

                System.out.println("Enter the electricity price per kWh in USD: ");
                simulationConfig.setKwhPrice(validatePositiveNumericInput(scanner, LOWER_FLOAT_LIMIT,SimulationConfig.DEFAULT_PRICE_LIMIT));

                System.out.println("Enter the simulation run time in seconds: ");

                //RUNTIME HAS TO BE BIGGER THAN THE CYCLE TIME
                simulationConfig.setSimulationRunTime(validatePositiveNumericInput(scanner, cycleSecondInterval, SimulationConfig.DEFAULT_RUNTIME_LIMIT));


                validInput = true; // Input is valid, exit the loop
            } catch (InputMismatchException e) {
                logger.warn("Invalid input. Please enter numeric values.", e);
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }


    /**
     * Loads the configuration from the JSON file.
     * @return true if the configuration was loaded successfully, false otherwise
     */
    private static boolean loadConfigFromJSON() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SimulationConfig loadedConfig = objectMapper.readValue(new File(simulationConfig.getJSON_FILE_PATH()), SimulationConfig.class);

            System.out.println(loadedConfig);
            logger.info(loadedConfig);

            // Perform additional validation checks on loadedConfig


            // Check pet count
            if (loadedConfig.getPetCount() < 0 || loadedConfig.getPetCount() > SimulationConfig.DEFAULT_PET_LIMIT) {
                logger.error("Invalid pet count in the JSON file. It has to be bigger than 0 and smaller than" + SimulationConfig.DEFAULT_PET_LIMIT + ".");
                return false;
            }

            // Check floor count
            if (loadedConfig.getFloorCount() < LOWER_INT_LIMIT || loadedConfig.getFloorCount() > SimulationConfig.DEFAULT_FLOOR_LIMIT) {
                logger.error("Invalid floor count in the JSON file. It has to be bigger than 0 and smaller than" + SimulationConfig.DEFAULT_FLOOR_LIMIT + ".");
                return false;
            }
            // Check simulation run time
            if (loadedConfig.getSimulationRunTime() <= LOWER_INT_LIMIT || loadedConfig.getSimulationRunTime() > SimulationConfig.DEFAULT_RUNTIME_LIMIT) {
                logger.error("Invalid simulation run time in the JSON file. It has to be bigger than 0 and smaller than" + SimulationConfig.DEFAULT_RUNTIME_LIMIT + ".");
                return false;
            }

            // Check cycle second interval
            if (loadedConfig.getCycleSecondInterval() < LOWER_FLOAT_LIMIT || loadedConfig.getCycleSecondInterval() > SimulationConfig.DEFAULT_RUNTIME_LIMIT) {
                logger.error("Invalid cycle second interval in the JSON file, it has to be bigger than "+ LOWER_FLOAT_LIMIT +" and smaller than " + SimulationConfig.DEFAULT_RUNTIME_LIMIT + ".");
                return false;
            }

            // Check other properties
            if (loadedConfig.getRoomCount() < LOWER_INT_LIMIT || loadedConfig.getRoomCount() > SimulationConfig.DEFAULT_ROOM_LIMIT) {
                logger.error("Invalid room count in the JSON file. It has to be bigger than 0 and smaller than" + SimulationConfig.DEFAULT_ROOM_LIMIT + ".");
                return false;
            }



            if (loadedConfig.getFamilySize() < LOWER_INT_LIMIT || loadedConfig.getFamilySize() > SimulationConfig.DEFAULT_FAMILY_LIMIT) {
                logger.error("Invalid family size in the JSON file. It has to be bigger than 0 and smaller than" + SimulationConfig.DEFAULT_FAMILY_LIMIT + ".");
                return false;
            }

            if (loadedConfig.getSportEquipmentCount() < 0 || loadedConfig.getSportEquipmentCount() > SimulationConfig.DEFAULT_SPORT_EQUIPMENT_LIMIT) {
                logger.error("Invalid sport equipment count in the JSON file. It has to be bigger than 0 and smaller than" + SimulationConfig.DEFAULT_SPORT_EQUIPMENT_LIMIT + ".");
                return false;
            }


            // Set the loaded configuration to your simulationConfig instance
            simulationConfig.setRoomCount(loadedConfig.getRoomCount());
            simulationConfig.setFamilySize(loadedConfig.getFamilySize());
            simulationConfig.setSimulationRunTime(loadedConfig.getSimulationRunTime());
            simulationConfig.setSportEquipmentCount(loadedConfig.getSportEquipmentCount());
            simulationConfig.setKwhPrice(loadedConfig.getKwhPrice());
            simulationConfig.setLiterOfWaterPrice(loadedConfig.getLiterOfWaterPrice());
            simulationConfig.setLiterOfGasPrice(loadedConfig.getLiterOfGasPrice());
            simulationConfig.setFamily(loadedConfig.getFamily());
            simulationConfig.setCycleSecondInterval(loadedConfig.getCycleSecondInterval());

            logger.info("Configuration loaded from JSON file.");
            System.out.println("Configuration loaded from JSON file.");
            return true;

        } catch (IOException e) {
            System.out.println("Error loading configuration from JSON file.");
            logger.error("Error loading configuration from JSON file.", e);
            return false;
        }
        catch (NoSuchElementException e){
            System.out.println("Error loading configuration from JSON file.");
            logger.error("Error loading configuration from JSON file.", e);
            return false;
        }
    }

    /**
     * Gets the elapsed simulation time in hours.
     * @return elapsed simulation time in hours
     */
    public static long getElapsedSimulationTimeInHours() {
        House  house = simulationConfig.getHouse();
        return house.getTimeEntity().getCurrentTime().getCurrentLocalTime().getHour() - 12 + 24 * house.getTimeEntity().getCurrentTime().getCurrentDay();
    }


    /**
     * Validates the numeric input.
     * @param scanner scanner to get the input from
     * @param lowerLimit lower limit of the input
     * @param upperLimit upper limit of the input
     * @return validated input
     */
    public static int validateNumericInput(Scanner scanner, int lowerLimit, int upperLimit) {

        int value = 0;
        try {
            value = scanner.nextInt();
        }

        //todo is it REALLY OKAY EVEN IF ITS CAUGHT IF IT DOESNT CRASH BUT STILL SHOWS ERROR
        catch (NoSuchElementException e) {
            System.out.println("Invalid input. Please enter a numeric value between " + lowerLimit + " and " + upperLimit + ".");
            logger.error("Invalid user input.");
            throw new InputMismatchException();
        }

        if (value < lowerLimit || value > upperLimit) {
            System.out.println("Invalid input. Please enter a value between " + lowerLimit + " and " + upperLimit + ".");
            logger.error("Invalid user input.");

            throw new InputMismatchException();
        }
        return value;
    }

    /**
     * Validates the positive numeric input.
     * @param scanner scanner to get the input from
     * @param upperLimit upper limit of the input
     * @return validated input
     */

    public static float validatePositiveNumericInput(Scanner scanner,float lowerFloatLimit, float upperLimit) {
        float value = scanner.nextFloat();
        if (value < 0 || value > upperLimit) {
            System.out.println("Invalid input. Please enter a positive value between "+ lowerFloatLimit  +" and " + upperLimit + ".");

            logger.error("Invalid user input.");
            throw new InputMismatchException();
        }
        return value;
    }


    /**
     * sets the interval at which events happen and time is moved forward during the simulation
     * @param cycleSecondInterval  interval at which events happen and time is moved forward during the simulation
     */
    public static void setCycleSecondInterval(float cycleSecondInterval) {
        simulationConfig.setCycleSecondInterval(cycleSecondInterval) ;
    }


    /**
     * @return logged events during the simulation
     */
    public static List<Event> getLoggedEvents() {
        return simulationConfig.getLoggedEvents();
    }

    public static House getHouse() {
        return simulationConfig.getHouse();
    }

    public static List<Person> getFamily() {
        return simulationConfig.getFamily();
    }

    /**
     * Starts the simulation.
     */
    public void startSimulation() {
        systemStartTime = System.currentTimeMillis();
        timer.scheduleAtFixedRate(new EventGeneratorTask(simulationConfig,timer), 0, (long) (simulationConfig.getCycleSecondInterval() * 1000));
    }

    public Timer getTimer() {
        return timer;
    }

    /**
     * Fills out the consumption report.
     */
    public void fillOutConsumptionReport() {
        for (AbstractDevice device : simulationConfig.getHouse() .getDevices()) {

            SimulationLogger.logConsumption(device.getName() + ": ");
            SimulationLogger.logConsumption("       Power consumption  " + device.getPowerConsumption()+ " kWh" );
            SimulationLogger.logConsumption("       Water consumption  " + device.getWaterConsumption()+ " liters");
            SimulationLogger.logConsumption("       Gas consumption "+ device.getGasConsumption() + " liters");
            SimulationLogger.logConsumption("------------------------");
            SimulationLogger.logConsumption("       Price of electricity: " + simulationConfig.getKwhPrice() + " USD/kWh");
            SimulationLogger.logConsumption("       Price of water: " + simulationConfig.getLiterOfWaterPrice() + " USD/liter");
            SimulationLogger.logConsumption("       Price of gas: " + simulationConfig.getLiterOfGasPrice() + " USD/liter");
            SimulationLogger.logConsumption("------------------------");
            SimulationLogger.logConsumption("       Total price of electricity for this device: " + device.getPowerConsumption() * simulationConfig.getKwhPrice() + " USD");
            SimulationLogger.logConsumption("       Total price of water for this device: " + device.getWaterConsumption() * simulationConfig.getLiterOfWaterPrice() + " USD");
            SimulationLogger.logConsumption("       Total price of gas for this device: " + device.getGasConsumption() * simulationConfig.getLiterOfGasPrice() + " USD");
            SimulationLogger.logConsumption("=====================================");
            SimulationLogger.logConsumption("=====================================\n");
        }
    }


    public SimulationConfig getSimulationConfig() {
        return simulationConfig;
    }
}
