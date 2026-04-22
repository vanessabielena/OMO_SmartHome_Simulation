package model.devices.concreteDevices.washing_machine;

public class WashingMachineOffState implements WashingMachineState{
    @Override
    public boolean turnOn(WashingMachine washingMachine) {
        washingMachine.setState(new WashingMachineActiveState());
        System.out.println(washingMachine.getName() + " is now ON.");
        return true;
    }

    @Override
    public boolean turnOff(WashingMachine washingMachine) {
        System.out.println(washingMachine.getName() + " is already OFF.");
        return false;
    }

    @Override
    public void start(WashingMachine washingMachine) {
        System.out.println(washingMachine.getName() + " is off. Start after turning it on.");
    }

    @Override
    public void stop(WashingMachine washingMachine) {
        System.out.println(washingMachine.getName() + " is already OFF.");

    }
}
