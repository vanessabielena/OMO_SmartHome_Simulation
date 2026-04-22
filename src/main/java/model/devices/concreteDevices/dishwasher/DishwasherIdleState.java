package model.devices.concreteDevices.dishwasher;

public class DishwasherIdleState implements DishwasherState {
    @Override
    public void start(Dishwasher dishwasher) {
        dishwasher.setState(new DishwasherActiveState());
        System.out.println(dishwasher.getName() + " is now running.");
    }

    @Override
    public void stop(Dishwasher dishwasher) {
        System.out.println(dishwasher.getName() + " is now idle.");
    }

    @Override
    public boolean turnOn(Dishwasher dishwasher) {
        dishwasher.setState(new DishwasherActiveState());
        System.out.println(dishwasher.getName() + " is already on and idling.");
        return false;
    }

    @Override
    public boolean turnOff(Dishwasher dishwasher) {
        System.out.println(dishwasher.getName() + " is turned off.");
        return true;
    }
}
