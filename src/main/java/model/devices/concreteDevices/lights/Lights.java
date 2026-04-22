package model.devices.concreteDevices.lights;

import model.devices.AbstractDevice;
import model.eventCausers.beings.Person;
import model.house.Room;

import java.time.Duration;
import java.time.LocalTime;

import static java.lang.Math.abs;

public class Lights extends AbstractDevice {
    private int brightness;
    private LightsState state;
    private LocalTime lastTurnOnTimestamp;


    public Lights(String name, int id, Room room) {
        super(name,id,room);
        this.state = new LightsOnState();
        this.brightness = 50; // Default brightness
        hasTimeFunction = true;
        this.lastTurnOnTimestamp = room.getHouse().getTimeEntity().getCurrentTime().getCurrentLocalTime();

    }

    public void setState(LightsState state) {
        this.state = state;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
        System.out.println(getName() + " brightness set to " + brightness + "%.");
    }

    @Override
    public boolean turnOn(LocalTime time , Person personTurningOn) {
        if (state instanceof LightsOffState) {
            lastTurnOnTimestamp = time;
        }
        return state.turnOn(this);

    }

    @Override
    public boolean turnOff(LocalTime time) {
        if (state instanceof LightsOnState && lastTurnOnTimestamp != null) {
            //calculate duration in seconds and calculate power consumption based on duration
            this.powerConsumption += calculatePowerConsumption(Duration.between(time, lastTurnOnTimestamp).getSeconds());
            return state.turnOff(this);
        } else {
            System.out.println(this.getName() + " is already off.");
            return false;
        }
    }

    public float calculatePowerConsumption(float durationInSeconds) {
        return abs(((float) getBrightness() /100) * durationInSeconds * 0.005f);
    }

    public LightsState getState() {
        return state;
    }

    public LocalTime getLastTurnOnTimestamp() {
        return lastTurnOnTimestamp;
    }

    public void setLastTurnOnTimestamp(LocalTime lastTurnOnTimestamp) {
        this.lastTurnOnTimestamp = lastTurnOnTimestamp;
    }

//    @Override
//    public void performAction() {
//        System.out.println(getName() + " is emitting light.");
//    }


}

