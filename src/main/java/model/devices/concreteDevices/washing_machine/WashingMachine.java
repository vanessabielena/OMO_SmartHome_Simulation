package model.devices.concreteDevices.washing_machine;

import model.devices.AbstractDevice;
import model.eventCausers.beings.Person;
import model.events.Event;
import model.house.Room;
import simulation.SimulationLogger;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class WashingMachine extends AbstractDevice {

    private WashingMachineState state;

    private LocalTime lastTurnOnTimestamp;
    private boolean hasClothes = false;

    public WashingMachine(String name, int id, Room room) {
        super(name, id, room);
        this.state = new WashingMachineOffState();
        hasTimeFunction = true;
        this.lastTurnOnTimestamp = room.getHouse().getTimeEntity().getCurrentTime().getCurrentLocalTime();

    }

    public void start() {
        state.start(this);
    }

    public void stop() {
        state.stop(this);
    }

    @Override
    public boolean turnOn(LocalTime time, Person personTurningOn) {
        if (state instanceof WashingMachineOffState) {
            lastTurnOnTimestamp = time;
        }
        return state.turnOn(this);
    }

    @Override
    public boolean turnOff(LocalTime time) {
        if (state instanceof WashingMachineOffState) {
            System.out.println(this.getName() + " cannot be turned off, already off.");
            return false;
        } else {
            this.powerConsumption += calculatePowerConsumption(Duration.between(time, lastTurnOnTimestamp).getSeconds());
            this.waterConsumption += calculateWaterConsumption(Duration.between(time, lastTurnOnTimestamp).getSeconds());
            return state.turnOff(this);
        }
//        state.turnOff(this);
    }

    @Override
    public Event performTimeAction(LocalTime currentTime) {
        if (state instanceof WashingMachineActiveState) {
            state.stop(this);
            this.powerConsumption += 0.5f; //cca amount of consumed energy per one cycle
            this.waterConsumption += 70; // amount in litres

            Event event = new Event(this.getName() + " has finished washing the clothes.", this, new ArrayList<>(List.of(this)));
            SimulationLogger.logEventOrdered(event);
            return event;
        }
        return null;
    }

    public float calculatePowerConsumption(long durationInSeconds) {
        return abs(0.01f * durationInSeconds);
    }

    public float calculateWaterConsumption(long durationInSeconds) {
        return abs(0.06f * durationInSeconds);
    }

    public boolean addClothes(Person person) {
        if (!hasClothes) {
            hasClothes = true;
            return true;
        }
        return false;

    }

    public boolean takeClothes(Person person) {
        if (hasClothes) {
            hasClothes = false;
            return true;
        }
        return false;
    }

    public WashingMachineState getState() {
        return state;
    }

    public void setState(WashingMachineState state) {
        this.state = state;
    }

    public boolean isHasClothes() {
        return hasClothes;
    }

    public void setHasClothes(boolean hasClothes) {
        this.hasClothes = hasClothes;
    }

    public LocalTime getLastTurnOnTimestamp() {
        return lastTurnOnTimestamp;
    }

    public void setLastTurnOnTimestamp(LocalTime lastTurnOnTimestamp) {
        this.lastTurnOnTimestamp = lastTurnOnTimestamp;
    }
}
