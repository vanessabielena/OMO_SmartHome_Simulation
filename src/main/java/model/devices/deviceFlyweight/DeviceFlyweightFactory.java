package model.devices.deviceFlyweight;

import model.devices.DeviceActivityState;
import model.house.Room;

import java.util.HashMap;
import java.util.Map;

public class DeviceFlyweightFactory {

    private static final Map<String, DeviceRoomActivityFlyweight> flyweightPool = new HashMap<>();

    public static DeviceRoomActivityFlyweight getDeviceFlyweight(DeviceActivityState activityState, Room room) {
        String key = activityState + "_" + room.getRoomType();
        return flyweightPool.computeIfAbsent(key, k -> new DeviceRoomActivityFlyweight(activityState, room));
    }


    public static void clearFlyweightPool() {
        flyweightPool.clear();
    }

}
