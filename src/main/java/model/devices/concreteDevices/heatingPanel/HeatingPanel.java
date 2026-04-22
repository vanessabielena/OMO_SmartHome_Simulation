package model.devices.concreteDevices.heatingPanel;

import model.devices.AbstractDevice;
import model.eventCausers.beings.Person;
import model.house.Room;

import java.time.Duration;
import java.time.LocalTime;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

public class HeatingPanel extends AbstractDevice {
    private HeatingPanelState state;
    private float temperature;

    private LocalTime lastTurnOnTimestamp;

    public HeatingPanel(String name, int id, Room room) {
        super(name, id, room);
        this.state = new HeatingPanelOnState();
        this.temperature = room.getTemperature(); // Initialize with the room's current temperature
        hasTimeFunction = true;
        this.lastTurnOnTimestamp = room.getHouse().getTimeEntity().getCurrentTime().getCurrentLocalTime();


    }

    public void setState(HeatingPanelState state) {
        this.state = state;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
        System.out.println(getName() + " temperature set to " + temperature + "°C.");
    }

    @Override
    public boolean turnOn(LocalTime time, Person personTurningOn) {
        if (state instanceof HeatingPanelOffState) {
            lastTurnOnTimestamp = time;
        }

        return state.turnOn(this);

    }

    @Override
    public boolean turnOff(LocalTime time) {
        if (state instanceof HeatingPanelOnState) {
            //calculate duration in seconds and calculate power consumption based on duration
            this.powerConsumption += calculatePowerConsumption(Duration.between(lastTurnOnTimestamp, time).getSeconds());
            this.gasConsumption += calculateGasConsumption(Duration.between(lastTurnOnTimestamp, time).getSeconds());
            //maybe should log something? idk
        }

        return state.turnOff(this);
    }

    private float calculateGasConsumption(long seconds) {
        return abs(seconds * (temperature/1000));
    }

    public void increaseTemperature(float amount) {
        state.increaseTemperature(this, amount);
    }

    public void decreaseTemperature(float amount) {
        state.decreaseTemperature(this, amount);
    }

    //i've just made up a calculation, so feel free to change that
    public float calculatePowerConsumption(long durationInSeconds) {
        return abs(durationInSeconds * (temperature/1000));
    }

    public void performAction() {
        state.performAction(this);
    }


    public HeatingPanelState getState() {
        return state;
    }
}
