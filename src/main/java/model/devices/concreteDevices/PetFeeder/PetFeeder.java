package model.devices.concreteDevices.PetFeeder;

import model.devices.AbstractDevice;
import model.eventCausers.beings.Person;
import model.events.Event;
import model.house.Room;

import java.time.LocalTime;

public class PetFeeder  extends AbstractDevice {


    private boolean hasFood = false;
    public PetFeeder(String name, int id, Room room) {
        super(name, id, room);
        hasTimeFunction = true;

    }


    public boolean fillFeeder(Person person){
        if(!hasFood){
            hasFood = true;
            return true;
        }
        return false;

    }

    public boolean feedPet(){
        if(hasFood){
            hasFood = false;
            return true;
        }
        return false;
    }

    @Override
    public Event performTimeAction(LocalTime currentTime) {
        powerConsumption += 5;
        return null;
    }

    public boolean isHasFood() {
        return hasFood;
    }

    public void setHasFood(boolean hasFood) {
        this.hasFood = hasFood;
    }
}
