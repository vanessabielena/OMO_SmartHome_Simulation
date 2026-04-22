package model.devices.concreteDevices.sensors;

//import model.events.EventType;
import model.devices.AbstractDevice;
//import model.eventCausers.TemperatureEntity;
import model.devices.concreteDevices.heatingPanel.HeatingPanel;
import model.events.Event;
import model.house.Room;
import simulation.SimulationLogger;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TemperatureSensor extends AbstractDevice {
    private Room room;
    private List<AbstractDevice> subscribedDevices = new ArrayList<>();

    public TemperatureSensor(String name, int id, Room room) {
        super(name, id, room);
        this.room = room;
        SimulationLogger.logHouseConfig("New temperature sensor - " + name + " was created.");
        hasTimeFunction = true;
    }

    public void update(float newTemperature) {
        if (newTemperature > 28) {
            handleHighTemperature(newTemperature);
        } else if (newTemperature < 20) {
            handleLowTemperature(newTemperature);
        }
    }

    /**
     * Handles a high temperature event
     * @param temperature the current temperature
     */
    public void handleHighTemperature(float temperature) {
        // Logic to handle temperature change event

        SimulationLogger.logEvent("Temperature sensor received event: temperature has risen to" + temperature + " °C");
        HeatingPanel heatingPanel = findHeatingPanelInRoom();
        if (heatingPanel != null) {
            float adjustment = calculateCoolingAdjustment(temperature);
            heatingPanel.decreaseTemperature(adjustment);
        }
//        System.out.println("Thermostat settings adjusted based on temperature change.");
    }

    /**
     * Handles a low temperature event
     * @param temperature the current temperature
     */
    public void handleLowTemperature(float temperature) {
        // Logic to handle temperature change event
        SimulationLogger.logEvent("Temperature sensor received event: temperature has fallen to" + temperature + " °C");
        HeatingPanel heatingPanel = findHeatingPanelInRoom();
        if (heatingPanel != null) {
            float adjustment = calculateHeatingAdjustment(temperature);
            heatingPanel.increaseTemperature(adjustment);
        }
//        System.out.println("Thermostat settings adjusted based on temperature change.");
    }
    /**
     * Calculates the adjustment to the heating panel's temperature setting based on the severity of the low temperature event
     * @param temperature the current temperature
     * @return the adjustment to the heating panel's temperature setting
     */
    public float calculateCoolingAdjustment(float temperature) {
        // Calculate the adjustment based on the severity of the high temperature event
        return (temperature - 28) / 5; // Example adjustment value, assuming 1°C adjustment for every 5°C above 28°C
    }

    /**
     * Calculates the adjustment to the heating panel's temperature setting based on the severity of the low temperature event
     * @param temperature the current temperature
     * @return the adjustment to the heating panel's temperature setting
     */
    public float calculateHeatingAdjustment(float temperature) {
        // Calculate the adjustment based on the severity of the low temperature event
        return (20 - temperature) / 5; // Example adjustment value, assuming 1°C adjustment for every 5°C below 20°C
    }

    /**
     * Finds a heating panel in the room
     * @return the heating panel if found, null otherwise
     */
    public HeatingPanel findHeatingPanelInRoom() {
        //iterate over devices in the room to find a heating panel
        for (AbstractDevice device : room.getDevices()) {
            if (device instanceof HeatingPanel) {
                return (HeatingPanel) device;
            }
        }
        return null;
    }

    @Override
    public Event performTimeAction(LocalTime currentTime) {
        powerConsumption += 50f;
        return null;
    }

    // useless at the moment, there is a possible use maybe with fridge or something
    public void subscribe(AbstractDevice device) {
        this.subscribedDevices.add(device);
    }
}
