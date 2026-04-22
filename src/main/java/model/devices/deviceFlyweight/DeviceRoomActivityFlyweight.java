package model.devices.deviceFlyweight;

import model.devices.DeviceActivityState;
import model.house.Room;

public class DeviceRoomActivityFlyweight {

    //pokud nestaci, pridat treba name
    private DeviceActivityState activityState;
    private Room room;

    public DeviceRoomActivityFlyweight(DeviceActivityState activityState, Room room) {
        this.activityState = activityState;
        this.room = room;
    }

    public DeviceActivityState getActivityState() {
        return activityState;
    }

    public Room getRoom() {
        return room;
    }
}
