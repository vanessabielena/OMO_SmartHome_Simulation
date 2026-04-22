package model.devices.concreteDevices.washing_machine;

import model.devices.concreteDevices.dishwasher.Dishwasher;

public interface WashingMachineState {
    void start(WashingMachine washingMachine);

    void stop(WashingMachine washingMachine);

    boolean turnOn(WashingMachine washingMachine);

    boolean turnOff(WashingMachine washingMachine);
}
