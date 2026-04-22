package model.house;

import model.devices.AbstractDevice;
import model.eventCausers.beings.Person;
import model.eventCausers.beings.Pet;

import java.time.LocalDate;
import java.util.List;


//COMPOSITE PATTERN


public interface HouseComponent {

    List<AbstractDevice> getDevices();
    void setTemperature(float temperature);

    List<Person> getPeople();

    //create pet and person

    Person createAndAddPerson(LocalDate birthday, String name);

    Pet createAndAddPet(LocalDate birthday, String name);
//    void setBlindsState(BlindsState blindsState);



}
