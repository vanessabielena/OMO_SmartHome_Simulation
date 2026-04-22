package model.devices.concreteDevices.dishwasher;

public class DishwasherOffState implements DishwasherState {
    @Override
    public boolean turnOn(Dishwasher dishwasher) {
        dishwasher.setState(new DishwasherActiveState());
        System.out.println(dishwasher.getName() + " is now ON.");
        return true;

    }

    @Override
    public boolean turnOff(Dishwasher dishwasher) {
        System.out.println(dishwasher.getName() + " is already OFF.");
        return false;

    }

    @Override
    public void start(Dishwasher dishwasher) {
        System.out.println(dishwasher.getName() + " is off. Start after turning it on.");
    }

    @Override
    public void stop(Dishwasher dishwasher) {
        System.out.println(dishwasher.getName() + " is already OFF.");
    }
}
