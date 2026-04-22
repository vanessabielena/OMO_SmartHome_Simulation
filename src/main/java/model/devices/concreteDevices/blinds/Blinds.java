package model.devices.concreteDevices.blinds;

import model.devices.AbstractDevice;
import model.events.Event;
import model.house.Room;

import java.time.LocalTime;
import java.util.List;

public class Blinds extends AbstractDevice {

    private BlindsState state;
    private boolean isOpen = true;


    public Blinds(String name, int id, Room room) {
        super(name, id, room);
        hasTimeFunction = true;
        state = new BlindsStateOpen();
    }

    public BlindsState getState() {
        return state;
    }

    public void setState(BlindsState state) {
        this.state = state;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }


    @Override
    public Event performTimeAction(LocalTime currentTime) {
        //increase consumption
        //blinds are always active
        this.powerConsumption += calculatePowerConsumption(50);

        //if it's nighttime hours, close blinds
        if (isOpen) {
            if (currentTime.isAfter(LocalTime.of(20, 0)) || currentTime.isBefore(LocalTime.of(6, 0))) {
                    state.close(this);
                    isOpen = false;
                return new Event("All blinds close at nighttime. ", this, List.of(this));
            }
        }
        else
        {
            if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(20, 0))) {
                state.open(this);
                isOpen = true;
                return new Event("All blinds open at daytime. ", this, List.of(this));
            }
        }


        return null;
    }

}
