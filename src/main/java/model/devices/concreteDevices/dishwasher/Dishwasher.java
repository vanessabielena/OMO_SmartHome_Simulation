package model.devices.concreteDevices.dishwasher;

import model.devices.AbstractDevice;
import model.devices.concreteDevices.lights.LightsOffState;
import model.devices.concreteDevices.lights.LightsOnState;
import model.devices.concreteDevices.washing_machine.WashingMachineActiveState;
import model.devices.concreteDevices.washing_machine.WashingMachineOffState;
import model.eventCausers.beings.Person;
import model.events.Event;
import model.events.EventTarget;
import model.house.Room;
import simulation.SimulationLogger;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;

public class Dishwasher extends AbstractDevice {
    private DishwasherState state;

    private LocalTime lastTurnOnTimestamp;

    private boolean hasDishes = false;

    public Dishwasher(String name, int id, Room room) {
        super(name, id, room);
        this.state = new DishwasherIdleState();
        hasTimeFunction = true;
        this.lastTurnOnTimestamp = room.getHouse().getTimeEntity().getCurrentTime().getCurrentLocalTime();
    }

    public void setState(DishwasherState state) {
        this.state = state;
    }

    public DishwasherState getState() {
        return state;
    }

    public LocalTime getLastTurnOnTimestamp() {
        return lastTurnOnTimestamp;
    }

    public void start() {
        state.start(this);
    }

    public void stop() {
        state.stop(this);
    }

    @Override
    public boolean turnOn(LocalTime time , Person personTurningOn) {
        if (state instanceof DishwasherOffState) {
            lastTurnOnTimestamp = time;
        }
        return state.turnOn(this);
    }

    @Override
    public boolean turnOff(LocalTime time) {
        if (state instanceof DishwasherActiveState) { //state instanceof DishwasherIdleState &&
            System.out.println(this.getName() + " cannot be turned off, because it's active.");
            return false;
        } else {
            state.turnOff(this);
            this.powerConsumption += calculatePowerConsumption(Duration.between(time, lastTurnOnTimestamp).getSeconds());
            return true;
        }
    }

    @Override
    public Event performTimeAction(LocalTime currentTime) {
        if (state instanceof DishwasherActiveState) {
            state.stop(this);
            this.powerConsumption += 0.5f; //cca amount of consumed energy per one cycle
            this.waterConsumption += 22; // amount in litres
            //new arrayList of event targets with this included


            Event event = new Event(this.getName() + " has finished washing the dishes.", this, new ArrayList<EventTarget>(List.of(this)));
            SimulationLogger.logEventOrdered(event);
            return event;
        }
        return null;
    }

    public float calculatePowerConsumption(long durationInSeconds) {
        return abs(0.001f * durationInSeconds);
    }

    public boolean addDishes(Person person) {
        if (!hasDishes) {
            hasDishes = true;
            return true;
        }
        return false;
    }

    public boolean takeDishes(Person person) {
        if (hasDishes) {
            hasDishes = false;
            return true;
        }
        return false;
    }

    public boolean isHasDishes() {
        return hasDishes;
    }

    public void setHasDishes(boolean hasDishes) {
        this.hasDishes = hasDishes;
    }
}
