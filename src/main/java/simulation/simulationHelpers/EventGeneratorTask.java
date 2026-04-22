package simulation.simulationHelpers;

import model.eventCausers.beings.Pet;
import model.eventCausers.phenomena.TemperatureEntity;
import model.events.Event;
import model.eventCausers.beings.Person;
import model.house.House;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is responsible for generating events in each cycle of the simulation.
 */
public class EventGeneratorTask extends TimerTask {
    private final SimulationConfig simulationConfig;
    private int currentCycle = 0;

    private final float simulationRunTime;
    private final float cycleSecondInterval;
    private final List<Person> family;
    private final House house;
    private final List<Event> loggedEvents;
    private final Timer timer;
    private final List<TemperatureEntity> temperatureEntities;

    private final List<Pet> pets;

    public EventGeneratorTask(SimulationConfig simulationConfig, Timer timer) {
        this.simulationConfig = simulationConfig;
        this.simulationRunTime = simulationConfig.getSimulationRunTime();
        this.cycleSecondInterval = simulationConfig.getCycleSecondInterval();
        this.loggedEvents = simulationConfig.getLoggedEvents();
        this.temperatureEntities = simulationConfig.getTemperatureEntities();

        this.pets = simulationConfig.getPets();


        this.family = simulationConfig.getFamily();
        this.house = simulationConfig.getHouse();
        this.timer = timer;
        this.currentCycle = currentCycle;

    }

    /**
     * This method is called periodically with the interval set in the constructor.
     * It dictates what happens in each cycle of the simulation.
     */
    @Override
    public void run() {
        System.out.println("New Cycle:\n");

        generateEvents();
        house.getTimeEntity().nextTimePeriod();
        currentCycle++;

        System.out.println("\n");

        if (currentCycle >= simulationRunTime / cycleSecondInterval) {
            timer.cancel();
            System.out.println("Simulation is going to halt now...");
        }
    }

    private void generateEvents() {
        for (Person person : family) {
            loggedEvents.add(person.generateEvent());
        }
        for(TemperatureEntity temperatureEntity : temperatureEntities)
        {
            loggedEvents.add(temperatureEntity.generateEvent());
        }
        for(Pet pet: pets)
        {
            loggedEvents.add(pet.generateEvent());
        }
        for (Event event : loggedEvents) {
            System.out.println(event);
        }
    }
}