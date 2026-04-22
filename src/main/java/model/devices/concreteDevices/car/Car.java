package model.devices.concreteDevices.car;

import model.devices.AbstractDevice;
import model.devices.concreteDevices.washing_machine.WashingMachineActiveState;
import model.eventCausers.beings.Person;
import model.events.Event;
import model.events.EventTarget;
import model.house.Room;
import model.house.RoomType;
import simulation.SimulationLogger;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Car extends AbstractDevice{

        private CarState state;

        private LocalTime lastTurnOnTimestamp;


        Person personDriving;
        private boolean isOutDriving = false;


    public Car(String name, int id, Room room){
        super(name, id, room);
        this.state = new CarParkedState();
        hasTimeFunction = true;
    }


    public void setState(CarState state) {
            this.state = state;
        }


    @Override
    public boolean turnOn(LocalTime time, Person personToGoDriving) {

        if (state instanceof CarParkedState) {
            lastTurnOnTimestamp = time;
        }
        return state.turnOnAndGoOutDriving(this, personToGoDriving);

    }

    @Override
    public boolean turnOff(LocalTime time) {
        return false;
    }

    public Event turnOnAndGoOutDriving(Person personDriving) {
        //we assume people keep gas topped off

        if(isOutDriving){
            System.out.println("Car is already out driving.");
            return null;
        }

        //add gas and energy consumption
        this.gasConsumption += 1000f;


        Event event = new Event(personDriving.getFirstName() + " has gone out driving.", this, new ArrayList<EventTarget>(List.of(this)));
        SimulationLogger.logEventOrdered(event);
        return event;

    }

    public Event turnOffAndGoBackHome(Person personDriving) {
        //we assume people keep gas topped off

        if(!isOutDriving){
            System.out.println("Car is already at home.");
            return null;
        }

        //70% to go back home, 30% to not come back
        if(Math.random() < 0.3){
            System.out.println("Car is not coming back home yet.");
            this.gasConsumption += 1000f;

            return new Event(personDriving.getFirstName() + " is not coming back home yet.", this, new ArrayList<EventTarget>(List.of(this)));
        }

        //add gas and energy consumption
        this.gasConsumption += 1000f;

        Event event = new Event(personDriving.getFirstName() + " has gone back home.", this, new ArrayList<EventTarget>(List.of(this)));
        SimulationLogger.logEventOrdered(event);
        isOutDriving = false;
        personDriving.setIndisposed(false);
        return event;

    }


    public CarState getState() {
        return state;
    }

    public LocalTime getLastTurnOnTimestamp() {
        return lastTurnOnTimestamp;
    }

    public void setLastTurnOnTimestamp(LocalTime lastTurnOnTimestamp) {
        this.lastTurnOnTimestamp = lastTurnOnTimestamp;
    }

    public boolean isOutDriving() {
        return isOutDriving;
    }

    public void setOutDriving(boolean outDriving) {
        isOutDriving = outDriving;
    }


    @Override
    public Event performTimeAction(LocalTime currentTime) {

        if(!isOutDriving){
            return null;
        }


        return turnOffAndGoBackHome(personDriving);

    }


}
