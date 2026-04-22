package model.eventCausers.beings;

import model.devices.AbstractDevice;
import model.devices.concreteDevices.PetFeeder.PetFeeder;
import model.events.Event;
import model.house.House;
import model.house.Room;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simulation.SimulationLogger;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

public class Pet extends LivingBeing {
    Logger logger = LogManager.getLogger(Pet.class);


    public Pet(LocalDate birthdate, String firstName, House house) {
        super(birthdate, firstName, house);
    }

    /**
     * Interacts with a random device in the current room.
     *
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
        interactionMethods.put(this::eatFoodFromPetFeeder, "ate food from pet feeder");
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
     * Eats food from a pet feeder
     *
     * @param feeder feeder to eat from
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    public Event eatFoodFromPetFeeder(AbstractDevice feeder) {
        if (feeder instanceof PetFeeder) {

            if(((PetFeeder) feeder).feedPet())
            {
            Event event = new Event("Pet " + this.firstName + " ate food from pet feeder", this, new ArrayList<>(List.of(feeder)));
            SimulationLogger.logEventOrdered(event);
            return event;}
            {
                Event event = new Event("Pet " + this.firstName + " is big sad because pet feeder is empty", this, new ArrayList<>(List.of(feeder)));
                SimulationLogger.logEventOrdered(event);
                return event;
            }
        }


        return null;

    }


    /**
     * Generates an event for the pet. The event can be an interaction with a device in the room, or moving to another room.
     *
     * @return an Event describing the interaction, or null if no interaction was possible
     */
    public Event generateEvent() {
        //pick randomly from methods in this class and superclass
        Event event = null;



        //50% chance to go wait in room instead of doing anything else
        if (Math.random() > 0.5) {
            event = new Event("Pet " + this.firstName + " is playing in " + currentRoom.getRoomType(), this, new ArrayList<>(List.of(this)));
            SimulationLogger.logEventOrdered(event);
            return event;
        }

        Event interactEvent = interactWithDeviceInRoom();
        //interact with random device in room
        if (interactEvent != null)
            return interactEvent;

        //move to random room which is not the current room

        //get random room while it is not the current room

        if (house.getRooms().size() == 1) {
            event = new Event("Pet " + this.firstName + " is playing in " + currentRoom.getRoomType(), this, new ArrayList<>(List.of(this)));
            SimulationLogger.logEventOrdered(event);
            return event;
        }

        Room roomToMoveTo = currentRoom;
        while (roomToMoveTo == currentRoom)
            roomToMoveTo = house.getRandomRoom();

        moveToARoom(roomToMoveTo);
        logger.info(this.firstName + " is moving to " + roomToMoveTo.getRoomType());

        event = new Event("Pet " + this.firstName + " is moving to " + currentRoom.getRoomType(), this, new ArrayList<>(List.of(this)));
        SimulationLogger.logEventOrdered(event);
        return event;
    }


}
