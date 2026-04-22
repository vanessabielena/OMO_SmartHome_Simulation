package model.eventCausers.beings;

import model.devices.concreteDevices.PetFeeder.PetFeeder;
import model.devices.concreteDevices.blinds.Blinds;
import model.devices.concreteDevices.car.Car;
import model.devices.concreteDevices.dishwasher.Dishwasher;
import model.devices.concreteDevices.washing_machine.WashingMachine;
import model.events.Event;
import model.devices.AbstractDevice;
import model.devices.DeviceActivityState;
import model.devices.concreteDevices.Fridge;
import model.house.House;
import model.house.Room;
import model.sportEquipment.SportEquipment;
import simulation.SimulationLogger;


import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

public class Person extends LivingBeing {

    Random random = new Random();
    SportEquipment.EquipmentType currentEquipment = null;

    public Person(LocalDate birthdate, String firstName, House house) {
        super(birthdate, firstName, house);
    }



    //F5.	Jednotlivé osoby a zvířata mohou provádět aktivity(akce),
    // které mají nějaký efekt na zařízení nebo jinou osobu


    //Např. Plynovy_kotel_1[oteverny_plyn] + Otec.zavritPlyn(plynovy_kotel_1) -> Plynovy_kotel_1[zavreny_plyn].


    //goes exercise outside

    /**
     * Person goes to exercise
     * @param equipment equipment to exercise with
     * @return true if person has equipment to go out with, false otherwise
     */
    public boolean exercise(SportEquipment equipment){
        //check if bike or skis are available
        if (!equipment.isAnyEquipmentAvailable())
            return false;

        currentEquipment = equipment.getAnyEquipment().get();
        equipment.removeEquipment(currentEquipment);
        isIndisposed = true;

        return true;
    }


    /**
     * Interacts with a random device in the current room.
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    public Event interactWithDeviceInRoom() {
        List<AbstractDevice> devices = currentRoom.getDevices();

        if (devices.isEmpty()) {
            return null;
        }

        // Shuffle the devices to make the order random
        Collections.shuffle(devices, random);

        // Define a map of interaction methods and names
        Map<Function<AbstractDevice, Event>, String> interactionMethods = new HashMap<>();
        interactionMethods.put(this::turnOnDevice, "turned on");
        interactionMethods.put(this::turnOffDevice, "turned off");
        interactionMethods.put(this::eatFoodInFridge, "ate food from");
        interactionMethods.put(this::repairDevice, "repaired");
        interactionMethods.put(this::startDishwasher, "started a dishwasher");
        interactionMethods.put(this::turnOnAndGoOutDriving, "turned on car and went out driving");
        interactionMethods.put(this::openBlinds, "opened blinds in " + currentRoom.getName());
        interactionMethods.put(this::closeBlinds, "closed blinds in " + currentRoom.getName());
        interactionMethods.put(this::putFoodInFeeder, "put food in pet feeder");
        interactionMethods.put(this::putFoodInFridge, "put food in fridge");
        interactionMethods.put(this::addClothesToWashingMachine, "added clothes to washing machine");
        interactionMethods.put(this::takeClothesFromWashingMachine, "added clothes to washing machine");
        interactionMethods.put(this::addDishesToDishwasher, "added dishes to dishwasher");
        interactionMethods.put(this::takeDishesFromDishwasher, "added dishes to dishwasher");

//        interactionMethods.put(this::changeBrightnessOfLight, "changed brightness of light");
        // Add more methods as needed

        // Shuffle the list of interaction methods
        List<Function<AbstractDevice, Event>> methods = new ArrayList<>(interactionMethods.keySet());
        Collections.shuffle(methods, random);

        // Use streams to find the first successful interaction
        Event event = devices.stream()
                .flatMap(device -> methods.stream()
                        .map(interactionMethod -> interactionMethod.apply(device)))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);

        //increase the device the person interacted with in the map
        if (event != null) {
            AbstractDevice device = devices.stream()
                    .filter(d -> event.getDescription().contains(d.getName()))
                    .findAny().orElse(null);

            if (device != null) {
                this.deviceUseMap.merge(device, 1, Integer::sum);
            }
        }

        return event;
    }



    /**
     * Adds dishes to a dishwasher
     * @param abstractDevice dishwasher to add dishes to
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    private Event addDishesToDishwasher(AbstractDevice abstractDevice) {
        if (abstractDevice instanceof Dishwasher) {
            Dishwasher dishwasher = (Dishwasher) abstractDevice;
            if (dishwasher.addDishes(this)) {
                return new Event(this.firstName + " added dishes to " + dishwasher.getName(), this, new ArrayList<>(List.of(dishwasher)));
            }
        }
        return null;
    }


    /**
     * Takes dishes from a dishwasher
     * @param abstractDevice dishwasher to take dishes from
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    private Event takeDishesFromDishwasher(AbstractDevice abstractDevice) {
        if (abstractDevice instanceof Dishwasher) {
            Dishwasher dishwasher = (Dishwasher) abstractDevice;
            if (dishwasher.takeDishes(this)) {
                return new Event(this.firstName + " took dishes from " + dishwasher.getName(), this, new ArrayList<>(List.of(dishwasher)));
            }
        }
        return null;
    }

    /**
     * Adds clothes to a washing machine
     * @param abstractDevice washing machine to add clothes to
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    private Event addClothesToWashingMachine(AbstractDevice abstractDevice) {
        if (abstractDevice instanceof WashingMachine) {
            WashingMachine dishwasher = (WashingMachine) abstractDevice;
            if (dishwasher.addClothes(this)) {
                return new Event(this.firstName + " added clothes to " + dishwasher.getName(), this, new ArrayList<>(List.of(dishwasher)));
            }
        }
        return null;
    }

    /**
     * Takes clothes from a washing machine
     * @param abstractDevice washing machine to take clothes from
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    private Event takeClothesFromWashingMachine(AbstractDevice abstractDevice) {
        if (abstractDevice instanceof WashingMachine) {
            WashingMachine dishwasher = (WashingMachine) abstractDevice;
            if (dishwasher.takeClothes(this)) {
                return new Event(this.firstName + " take clothes from " + dishwasher.getName(), this, new ArrayList<>(List.of(dishwasher)));
            }
        }
        return null;
    }


    /**
     * Puts food in a fridge
     * @param abstractDevice fridge to put food in
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    private Event putFoodInFridge(AbstractDevice abstractDevice) {
        if (abstractDevice instanceof Fridge) {
            Fridge fridge = (Fridge) abstractDevice;
            fridge.addRandomFood();
            return new Event(this.firstName + " put food in " + fridge.getName(), this, new ArrayList<>(List.of(fridge)));
        }
        return null;
    }

    /**
     * Puts food in a pet feeder
     * @param abstractDevice pet feeder to put food in
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    private Event putFoodInFeeder(AbstractDevice abstractDevice) {
        if (abstractDevice instanceof PetFeeder) {
            PetFeeder petFeeder = (PetFeeder) abstractDevice;
            if (petFeeder.fillFeeder(this)) {
                return new Event(this.firstName + " put food in " + petFeeder.getName(), this, new ArrayList<>(List.of(petFeeder)));
            }
        }
        return null;
    }

    /**
     * Closes blinds
     * @param abstractDevice blinds to close
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    private Event closeBlinds(AbstractDevice abstractDevice) {
        if (abstractDevice instanceof Blinds) {
            Blinds blinds = (Blinds) abstractDevice;
            blinds.setOpen(false);
            return new Event(this.firstName + " closed blinds in " + currentRoom.getName(), this, new ArrayList<>(List.of(blinds)));
        }
        return null;
    }

    /**
     * Opens blinds
     * @param abstractDevice blinds to open
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    private Event openBlinds(AbstractDevice abstractDevice) {
        if (abstractDevice instanceof Blinds) {
            Blinds blinds = (Blinds) abstractDevice;
            blinds.setOpen(true);
            return new Event(this.firstName + " opened blinds in " + currentRoom.getName(), this, new ArrayList<>(List.of(blinds)));
        }
        return null;
    }

    /**
     * Turns on a car and goes out driving
     * @param abstractDevice car to turn on and go out driving
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    private Event turnOnAndGoOutDriving(AbstractDevice abstractDevice) {

        if (abstractDevice instanceof Car) {
            Car car = (Car) abstractDevice;
            return car.turnOnAndGoOutDriving(this);
        }
        return null;
    }


    /**
     * Turns on a device
     * @param device device to turn on
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    private Event turnOnDevice(AbstractDevice device) {
        if (device.getActivityState()== DeviceActivityState.ON)
            return null; // Interaction failed
        if (!(device instanceof Fridge)) {

            if(!device.turnOn(currentRoom.getHouse().getTimeEntity().getCurrentTime().getCurrentLocalTime(), this)){
                // Interaction failed, device was already on
                return null;
            }

            Event event = new Event("Person " + this.firstName + " turned on " + device.getName(), this, new ArrayList<>(List.of(device)));
            SimulationLogger.logEventOrdered(event);

            return event;
        } else {
            return null;
        }

    }


    /**
     * Turns off a device
     * @param device device to turn off
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    public Event turnOffDevice(AbstractDevice device){

        if (device.getActivityState()== DeviceActivityState.OFF)
            return null;
        if (!(device instanceof Fridge)) {

            if(!device.turnOff(currentRoom.getHouse().getTimeEntity().getCurrentTime().getCurrentLocalTime())){
                // Interaction failed, device was already off
                return null;
            }
            Event event = new Event(this.firstName + " turned off " + device.getName(), this, new ArrayList<>(List.of(device)));
            SimulationLogger.logEventOrdered(event);
            return event;
        } else {
            return null;
        }

    }


    /**
     * Eats food from a fridge
     * @param fridge fridge to eat from
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    public Event eatFoodInFridge(AbstractDevice fridge){
        if (!(fridge instanceof Fridge))
            return null;

        Fridge fridgeToEatFrom = (Fridge) fridge;
        //pick random food from fridge
        fridgeToEatFrom.eatRandomFood();
        Event event = new Event(this.firstName + " ate food from " + fridge.getName(),this,new ArrayList<>(List.of(fridge)));
        SimulationLogger.logEventOrdered(event);

        return event;
    }

    /**
     * Generates a random event for the person depending on which room they are in.
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    public Event generateEvent(){
        //pick randomly from methods in this class and superclass

        if(currentEquipment!=null){
            //stop exercising
            house.getSportEquipment().addEquipment(currentEquipment);
            currentEquipment = null;
            isIndisposed = false;
            logger.info(this.firstName + " stopped exercising");
            Event event = new Event(this.firstName + " stopped exercising", this, new ArrayList<>(List.of(this)));
            SimulationLogger.logEventOrdered(event);
            return event;
        }

        if(isIndisposed) {
            logger.info(this.firstName + " is busy.");
            Event event = new Event(this.firstName + " is indisposed. ", this, new ArrayList<>(List.of(this)));
            SimulationLogger.logEventOrdered(event);
            return event;
        }

        Event event = null;
        //50% chance to go exercising instead of doing anything else
        //via random bool
        if (random.nextBoolean())
        {
            if(exercise(house.getSportEquipment())) {
                logger.info(this.firstName + " is exercising");
                event = new Event(this.firstName + " is exercising", this, new ArrayList<>(List.of(this)));
                SimulationLogger.logEventOrdered(event);
                return event;

            }
            else
            {
                logger.info(this.firstName + " is waiting. No equipment available");
                event = new Event(this.firstName + " is waiting. No equipment available", this, new ArrayList<>(List.of(this)));
                SimulationLogger.logEventOrdered(event);
                return event;
            }
        }

        //interact with random device in room

        Event deviceEvent = interactWithDeviceInRoom();


        if(deviceEvent != null)
            return deviceEvent;

        //move to random room which is not the current room
        //get random room while it is not the current room
        Room roomToMoveTo = currentRoom;

        if(house.getRooms().size() == 1) {
            logger.info("Only one room in the house. " + this.firstName + " is waiting in " + currentRoom.getRoomType());
            event = new Event(this.firstName + " is waiting in " + currentRoom.getRoomType(), this, new ArrayList<>(List.of(this)));
            SimulationLogger.logEventOrdered(event);

            return event;
        }

        while (roomToMoveTo == currentRoom)
            roomToMoveTo = house.getRandomRoom();

        moveToARoom(roomToMoveTo);

        event = new Event(this.firstName +" is moving to "+ roomToMoveTo.getRoomType(),this,new ArrayList<>(List.of(roomToMoveTo)));
        SimulationLogger.logEventOrdered(event);

        return event;
    }



    public Optional<SportEquipment.EquipmentType> getCurrentEquipment() {
        return Optional.ofNullable(currentEquipment);
    }

    public void setCurrentEquipment(SportEquipment.EquipmentType currentEquipment) {
        this.currentEquipment = currentEquipment;
    }

    /**
     * Repairs a device
     * @param device device to repair
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    public Event repairDevice(AbstractDevice device) {
        if (device.getDegradation() != 100) {
            device.repairDevice();
            logger.info("Person " +this.firstName + " is fixing " + device.getName() + ".");
            Event event = new Event(this.firstName + " is fixing " + device.getName() + ".",this,new ArrayList<>(List.of(device)));
            SimulationLogger.logEventOrdered(event);
            return event;
        }
        return null;
    }

    /**
     * Starts a dishwasher
     * @param device dishwasher to start
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    public Event startDishwasher(AbstractDevice device) {
        for (AbstractDevice device1 : this.currentRoom.getDevices()) {
            if (device1 instanceof Dishwasher) {
                ((Dishwasher) device1).start();
                Event event = new Event(this.firstName + " loaded and started the " + device1.getName(), this, new ArrayList<>(List.of(device1)));
                return event;
            }
        }
        return null;
    }



    @Override
    public String toString() {
        return "Person born " + birthdate + ", named " + firstName;
    }
}
