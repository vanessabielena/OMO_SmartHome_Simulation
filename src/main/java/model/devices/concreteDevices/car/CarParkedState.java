package model.devices.concreteDevices.car;

import model.eventCausers.beings.Person;

public class CarParkedState implements CarState{


    @Override
    public boolean turnOnAndGoOutDriving(Car car, Person person) {
        car.setOutDriving(true);
        System.out.println(car.getName() + " is now out driving.");
        person.setIndisposed(true);
        return true;
    }

    @Override
    public boolean turnOffAndComeBackHome(Car car, Person person) {
        System.out.println(car.getName() + " is already home.");
        return false;
    }
}
