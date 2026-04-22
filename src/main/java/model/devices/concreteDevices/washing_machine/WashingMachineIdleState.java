package model.devices.concreteDevices.washing_machine;

import model.devices.concreteDevices.dishwasher.Dishwasher;
import model.devices.concreteDevices.dishwasher.DishwasherActiveState;

public class WashingMachineIdleState implements WashingMachineState {
    @Override
    public void start(WashingMachine washingMachine) {
        washingMachine.setState(new WashingMachineActiveState());
        System.out.println(washingMachine.getName() + " is now running.");
    }

    @Override
    public void stop(WashingMachine washingMachine) {
        System.out.println(washingMachine.getName() + " is already idle.");
    }

    @Override
    public boolean turnOn(WashingMachine washingMachine) {
        washingMachine.setState(new WashingMachineActiveState());
        System.out.println(washingMachine.getName() + " is already ON.");
        return false;
    }

    @Override
    public boolean turnOff(WashingMachine washingMachine) {
        System.out.println(washingMachine.getName() + " is now OFF.");
        return true;
    }
}
