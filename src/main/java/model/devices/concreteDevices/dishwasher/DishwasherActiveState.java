package model.devices.concreteDevices.dishwasher;

public class DishwasherActiveState implements DishwasherState {
    @Override
    public void start(Dishwasher dishwasher) {
        System.out.println(dishwasher.getName() + " is already running.");
    }

    @Override
    public void stop(Dishwasher dishwasher) {
        dishwasher.setState(new DishwasherIdleState());
        System.out.println(dishwasher.getName() + " is now idle.");
    }

    @Override
    public boolean turnOn(Dishwasher dishwasher) {
        System.out.println(dishwasher.getName() + " is already running.");
        return false;
    }

    @Override
    public boolean turnOff(Dishwasher dishwasher) {
        dishwasher.setState(new DishwasherIdleState());
        System.out.println(dishwasher.getName() + " is turned off.");
        return false;

    }
}
