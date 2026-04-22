package model.devices.concreteDevices.car;

import model.devices.concreteDevices.washing_machine.WashingMachine;
import model.devices.concreteDevices.washing_machine.WashingMachineIdleState;
import model.eventCausers.beings.Person;

public class CarDrivingState implements CarState{


    @Override
    public boolean turnOnAndGoOutDriving(Car car, Person person) {

        if(car.isOutDriving()){
            System.out.println(car.getName() + " is already out driving.");
            return false;
        }

        //add gas and energy consumption
        car.setOutDriving(true);
        System.out.println(car.getName() + " is now out driving.");
        person.setIndisposed(true);
        return true;
    }

    @Override
    public boolean turnOffAndComeBackHome(Car car, Person person) {
        car.setOutDriving(false);
        System.out.println(car.getName() + " is now home.");

        person.setIndisposed(false);
        return false;
    }
}
