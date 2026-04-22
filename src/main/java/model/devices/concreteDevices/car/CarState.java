package model.devices.concreteDevices.car;

import model.eventCausers.beings.Person;

public interface CarState {



    boolean turnOnAndGoOutDriving(Car car, Person person);

    boolean turnOffAndComeBackHome(Car car, Person person);


}
