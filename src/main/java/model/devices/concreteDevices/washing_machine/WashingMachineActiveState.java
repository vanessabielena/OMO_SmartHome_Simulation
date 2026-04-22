package model.devices.concreteDevices.washing_machine;

import model.devices.concreteDevices.dishwasher.Dishwasher;
import model.devices.concreteDevices.dishwasher.DishwasherIdleState;

public class WashingMachineActiveState implements WashingMachineState{

    @Override
    public void start(WashingMachine washingMachine) {
        System.out.println(washingMachine.getName() + " is already running.");
    }

    @Override
    public void stop(WashingMachine washingMachine) {
        washingMachine.setState(new WashingMachineIdleState());
        System.out.println(washingMachine.getName() + " is now idle.");
    }

    @Override
    public boolean turnOn(WashingMachine washingMachine) {
        System.out.println(washingMachine.getName() + " is already running.");
        return false;
    }

    @Override
    public boolean turnOff(WashingMachine washingMachine) {
        washingMachine.setState(new WashingMachineIdleState());
        System.out.println(washingMachine.getName() + " is now idle.");
        return true;
    }

}
