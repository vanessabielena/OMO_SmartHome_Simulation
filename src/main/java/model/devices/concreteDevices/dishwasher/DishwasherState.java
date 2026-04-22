package model.devices.concreteDevices.dishwasher;

public interface DishwasherState {
    void start(Dishwasher dishwasher);

    void stop(Dishwasher dishwasher);

    boolean turnOn(Dishwasher dishwasher);

    boolean turnOff(Dishwasher dishwasher);
}
